package prir.zad2;

import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadTaskManagementFinal implements MultithreadTaskManagementInterface {

    private TaskManager taskManager = new TaskManager();

    @Override
    public void setNumberOfAvailableThreads(int threads) {
        taskManager.setMaxThreads(threads);
    }

    @Override
    public void setMaximumThreadsPerNiceLevel(int[] threadsLimit) {
        for (int i = 0; i < threadsLimit.length; ++i)
            taskManager.addLimit(new NiceLevelWithLimit(i, threadsLimit[i]));
    }

    @Override
    public void newTask(TaskInterface ti) {
        taskManager.submitTask(ti);
    }

    class TaskManager {
        private final SortedMap<Integer, NiceLevelProperties> taskMap =
                Collections.synchronizedSortedMap(new TreeMap<Integer, NiceLevelProperties>());
        private AtomicInteger maxThreads = new AtomicInteger(1);
        private AtomicInteger usedThreads = new AtomicInteger(0);
        private final GlobalThreadsInformation globalThreadsInformation = new GlobalThreadsInformation(usedThreads);
        private Thread taskExecutor;


        public void addLimit(NiceLevelWithLimit niceLevelWithLimit) {
            synchronized (taskMap) {
                taskMap.put(niceLevelWithLimit.niceLevel,
                        new NiceLevelProperties(
                                niceLevelWithLimit.threadLimit, globalThreadsInformation));
            }
        }

        public void setMaxThreads(int limit) {
            maxThreads.set(limit);
        }

        public void submitTask(TaskInterface ti) {
            synchronized (taskMap) {
                if (!taskMap.containsKey(ti.getNiceLevel()))
                    taskMap.put(ti.getNiceLevel(), new NiceLevelProperties(1, globalThreadsInformation));
                taskMap.get(ti.getNiceLevel()).getQueue().add(ti);
            }
            startTaskExecutorIfIsNotAlreadyStarted();
        }


        public void startTaskExecutorIfIsNotAlreadyStarted() {
            if (taskExecutor != null && taskExecutor.isAlive())
                return;
            taskExecutor = new Thread(new TaskExecutorPreconditonsChecker(taskMap, globalThreadsInformation, usedThreads, maxThreads));
            taskExecutor.start();
        }
    }


    class NiceLevelWithLimit {


        public final int niceLevel;
        public final int threadLimit;

        public NiceLevelWithLimit(int niceLevel, int threadLimit) {

            this.niceLevel = niceLevel;
            this.threadLimit = threadLimit;
        }
    }


    class GlobalThreadsInformation {
        private AtomicInteger usedThreads;

        GlobalThreadsInformation(AtomicInteger usedThreads) {
            this.usedThreads = usedThreads;
        }

        public void decrementUsedThreads() {
            usedThreads.decrementAndGet();
        }

        public void incrementUsedThreads() {
            usedThreads.incrementAndGet();
        }
    }


    class NiceLevelProperties {
        private Integer limit;
        private AtomicInteger used;
        private ConcurrentLinkedQueue<TaskInterface> queue;
        private final GlobalThreadsInformation globalThreadsInformation;

        NiceLevelProperties(int limit, GlobalThreadsInformation globalThreadsInformation) {
            this.limit = limit;
            this.globalThreadsInformation = globalThreadsInformation;
            this.used = new AtomicInteger(0);
            queue = new ConcurrentLinkedQueue<>();
        }

        public ConcurrentLinkedQueue<TaskInterface> getQueue() {
            return queue;
        }

        public boolean thereIsNoFreeThreads() {
            return used.get() >= limit;
        }

        public void runJob() {
            final TaskInterface ti = queue.poll();
            globalThreadsInformation.incrementUsedThreads();
            used.incrementAndGet();
            new Thread(new TaskExecutorForNiceLevel(globalThreadsInformation, ti, used)).start();
        }

        

    }

    
    class TaskExecutorForNiceLevel implements Runnable{

        private final GlobalThreadsInformation globalThreadsInformation;
        private final TaskInterface ti;
        private final AtomicInteger used;

        public TaskExecutorForNiceLevel(GlobalThreadsInformation globalThreadsInformation, TaskInterface ti, AtomicInteger used) {
            this.globalThreadsInformation = globalThreadsInformation;
            this.ti = ti;
            this.used = used;
        }

        @Override
            public void run() {
                ti.execute();
                globalThreadsInformation.decrementUsedThreads();
                synchronized (globalThreadsInformation) {
                    notifyThatThereIsAvailableThreadToExectueTask();
                }
                used.decrementAndGet();
            }

        private void notifyThatThereIsAvailableThreadToExectueTask() {
            globalThreadsInformation.notifyAll();
        }
    }

    class TaskExecutorPreconditonsChecker implements Runnable {


        private final SortedMap<Integer, NiceLevelProperties> taskMap;
        private final GlobalThreadsInformation globalThreadsInformation;
        private final AtomicInteger usedThreads;
        private AtomicInteger maxThreads;

        public TaskExecutorPreconditonsChecker(SortedMap<Integer, NiceLevelProperties> taskMap, GlobalThreadsInformation globalThreadsInformation, AtomicInteger usedThreads, AtomicInteger maxThreads) {

            this.taskMap = taskMap;
            this.globalThreadsInformation = globalThreadsInformation;
            this.usedThreads = usedThreads;
            this.maxThreads = maxThreads;
        }

        private boolean tryToRunTaskForNiceLevel() throws InterruptedException {
            synchronized (taskMap) {
                for (NiceLevelProperties l : taskMap.values()) {
                    if (thereIsNoAvailableThreads()) {
                        return false;
                    }
                    if (couldExecuteJobForGivenNiceLevel(l))
                        l.runJob();
                }
                return true;
            }
        }

        private boolean thereIsNoAvailableThreads() {
            return usedThreads.get() >= maxThreads.get();
        }

        private boolean couldExecuteJobForGivenNiceLevel(NiceLevelProperties l) {
            return !l.thereIsNoFreeThreads() && thereIsTashToExecuteForGivenNiceLevel(l);
        }

        private boolean thereIsTashToExecuteForGivenNiceLevel(NiceLevelProperties l) {
            return !l.getQueue().isEmpty();
        }

        @Override
        public void run() {
            while (thereAreTasksToExecute()) {
                try {
                    if (taskCouldNotBeTriggeredForGivenNIceLevel())
                        synchronized (globalThreadsInformation) {
                            globalThreadsInformation.wait();
                        }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean taskCouldNotBeTriggeredForGivenNIceLevel() throws InterruptedException {
            return !tryToRunTaskForNiceLevel();
        }

        boolean thereAreTasksToExecute(){
            return !thereIsNotTasksToExecute();
        }

        private boolean thereIsNotTasksToExecute() {
            synchronized (taskMap) {
                for (NiceLevelProperties l : taskMap.values())
                    if (l.getQueue().size() > 0)
                        return false;
                return true;
            }
        }
    }

}