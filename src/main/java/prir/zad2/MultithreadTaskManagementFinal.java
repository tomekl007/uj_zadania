package prir.zad2;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

class MultithreadTaskManagementFinal implements MultithreadTaskManagementInterface {

    private Manager manager = new Manager();

    @Override
    public void setNumberOfAvailableThreads(int threads) {
        manager.setMaximumThreads(threads);
    }

    @Override
    public void setMaximumThreadsPerNiceLevel(int[] threadsLimit) {
        for (int i = 0; i < threadsLimit.length; ++i)
            manager.AddLimit(new NiceLevelWithLimit(i, threadsLimit[i]));
    }

    @Override
    public void newTask(TaskInterface ti) {
        manager.submitTask(ti);
    }

    class Manager {
        private final SortedSet<NiceLevelProperties> tasks = Collections.synchronizedSortedSet(new TreeSet<NiceLevelProperties>(new Comparator<NiceLevelProperties>() {
            @Override
            public int compare(NiceLevelProperties o1, NiceLevelProperties o2) {
                return o1.niceLevel.compareTo(o2.niceLevel);
            }
        }));

        private AtomicInteger MaximumThreads = new AtomicInteger(1),CurrentThreads = new AtomicInteger(0);
        private final GlobalThreadsInformation globalThreadsInformation = new GlobalThreadsInformation(CurrentThreads);
        private Thread taskExecutor;


        public void AddLimit(NiceLevelWithLimit niceLevelWithLimit) {
            synchronized (tasks){
                tasks.add
                        (new NiceLevelProperties(
                                niceLevelWithLimit.threadLimit, globalThreadsInformation, niceLevelWithLimit.niceLevel));
            }
        }

        public void setMaximumThreads(int limit) {
            MaximumThreads.set(limit);
        }

        public void submitTask(TaskInterface ti) {
            synchronized (tasks) {
                boolean added = false;
                for(NiceLevelProperties niceLevel : tasks){
                    if(niceLevel.niceLevel == ti.getNiceLevel()){
                        added = true;
                        niceLevel.Queue.add(ti);
                    }
                }
                if(!added){
                    NiceLevelProperties n = new NiceLevelProperties(1, globalThreadsInformation, ti.getNiceLevel());
                    n.Queue.add(ti);
                    tasks.add(n);
                }
            }

            startTaskExecutorIfIsNotAlreadyStarted();
        }


        public void startTaskExecutorIfIsNotAlreadyStarted() {
            if (taskExecutor != null && taskExecutor.isAlive())
                return;
            taskExecutor = new Thread(new TaskExecutorPreconditonsChecker(tasks, globalThreadsInformation, CurrentThreads, MaximumThreads));
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

        @Override
        public String toString() {
            return "GlobalThreadsInformation{" +
                    "CurrentThreads=" + usedThreads +
                    '}';
        }

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


    class NiceLevelProperties implements Comparable<NiceLevelProperties> {
        private final Integer limit;
        private AtomicInteger used;
        public ConcurrentLinkedQueue<TaskInterface> Queue;
        private final GlobalThreadsInformation globalThreadsInformation;
        public final Integer niceLevel;

        @Override
        public String toString() {
            return "NiceLevelProperties{" +
                    "limit=" + limit +
                    ", used=" + used +
                    ", Queue=" + Queue +
                    ", globalThreadsInformation=" + globalThreadsInformation +
                    ", niceLevel=" + niceLevel +
                    '}';
        }

        NiceLevelProperties(int limit, GlobalThreadsInformation globalThreadsInformation, int niceLevel) {
            this.limit = limit;
            this.globalThreadsInformation = globalThreadsInformation;
            this.niceLevel = niceLevel;
            this.used = new AtomicInteger(0);
            Queue = new ConcurrentLinkedQueue<>();
        }

        public boolean thereIsNoFreeThreads() {
            return used.get() >= limit;
        }

        public void run() {
            final TaskInterface ti = Queue.poll();
            globalThreadsInformation.incrementUsedThreads();
            used.incrementAndGet();
            new Thread(new TaskExecutorForNiceLevel(globalThreadsInformation, ti, used)).start();
        }


        @Override
        public int compareTo(NiceLevelProperties o) {
            return this.niceLevel.compareTo(o.niceLevel);
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


        private final SortedSet<NiceLevelProperties> tasks;
        private final GlobalThreadsInformation globalThreadsInformation;
        private final AtomicInteger CurrentThreads;
        private AtomicInteger MaximumThreads;

        public TaskExecutorPreconditonsChecker(SortedSet<NiceLevelProperties> tasks, GlobalThreadsInformation globalThreadsInformation, AtomicInteger CurrentThreads, AtomicInteger MaximumThreads) {

            this.tasks = tasks;
            this.globalThreadsInformation = globalThreadsInformation;
            this.CurrentThreads = CurrentThreads;
            this.MaximumThreads = MaximumThreads;
        }

        private boolean tryToRunTaskForNiceLevel() throws InterruptedException {
            synchronized (tasks) {
                //Collections.sort(tasks);
                for (NiceLevelProperties l : tasks) {
                    if (thereIsNoAvailableThreads()) {
                        return false;
                    }
                    if (couldExecuteJobForGivenNiceLevel(l))
                        l.run();
                }
                return true;
            }
        }

        private boolean thereIsNoAvailableThreads() {
            return CurrentThreads.get() >= MaximumThreads.get();
        }

        private boolean couldExecuteJobForGivenNiceLevel(NiceLevelProperties l) {
            return !l.thereIsNoFreeThreads() && thereIsTashToExecuteForGivenNiceLevel(l);
        }

        private boolean thereIsTashToExecuteForGivenNiceLevel(NiceLevelProperties l) {
            return !l.Queue.isEmpty();

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
            synchronized (tasks) {
                for (NiceLevelProperties l : tasks)
                    if (l.Queue.size() > 0)
                        return false;
                return true;
            }
        }
    }

}