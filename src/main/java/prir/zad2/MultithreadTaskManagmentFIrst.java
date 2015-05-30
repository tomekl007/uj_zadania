package prir.zad2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

class MultithreadTaskManagmentFIrst implements MultithreadTaskManagementInterface{

    PriorityBlockingQueue<TaskInterface> priorityBlockingQueue = new PriorityBlockingQueue<>(100, new Comparator<TaskInterface>() {
        @Override
        public int compare(TaskInterface o1, TaskInterface o2) {
            return o1.getNiceLevel() - o2.getNiceLevel();
        }
    });
    public MultithreadTaskManagmentFIrst(){}


    Map<Integer, Executor> executorForNiceLevel = new HashMap<>();
    Executor executor = Executors.newSingleThreadExecutor();
    @Override
    public void setNumberOfAvailableThreads(int threads) {
        executor = Executors.newFixedThreadPool(threads);
    }

    @Override
    public void setMaximumThreadsPerNiceLevel(int[] threadsLimit) {
        int i = 0;
        for (; i < threadsLimit.length; i+= 2) {
            executorForNiceLevel.put(threadsLimit[i], Executors.newFixedThreadPool(threadsLimit[i+1]));
        }
        executorForNiceLevel.put(threadsLimit[i-1]++, Executors.newSingleThreadExecutor());
    }

    @Override
    public void newTask(TaskInterface ti) {
        priorityBlockingQueue.add(ti);
        final TaskInterface poll = priorityBlockingQueue.poll();
        Executor executorForNiceLevel = this.executorForNiceLevel.get(ti.getNiceLevel());

        if(executorForNiceLevel == null){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    poll.execute();
                }
            });
        }
        else {
            executorForNiceLevel.execute(new Runnable() {
                @Override
                public void run() {
                    poll.execute();
                }
            });
        }

    }
}