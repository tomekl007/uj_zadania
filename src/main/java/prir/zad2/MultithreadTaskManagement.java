package prir.zad2;

import com.sun.javafx.tk.Toolkit;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

class MultithreadTaskManagement implements MultithreadTaskManagementInterface {
    private LinkedBlockingQueue<TaskInterface> tasksReadyToProcess = new LinkedBlockingQueue<TaskInterface>();
    private Map<Integer, Integer> threadsPerNiceLevel = new HashMap<>();
    private Executor globalExecutor;
    private int threads;
    private CyclicBarrier barrier;
    private CountDownLatch countDownLatch;
    private AtomicInteger counter = new AtomicInteger(0);

    public MultithreadTaskManagement() {
    }

    @Override
    public void setNumberOfAvailableThreads(int threads) {
        System.out.println("setNumberOfAvailableThreads : " + threads);
        this.threads = threads;
        PriorityBlockingQueue<Runnable> pq = new PriorityBlockingQueue<>(20, new ComparePriority());
        globalExecutor = new ThreadPoolExecutor(threads, threads, 10, TimeUnit.SECONDS, pq);


    }


    private static class ComparePriority<T extends RunnableWithNiceLevel> implements Comparator<T> {

        @Override
        public int compare(T o1, T o2) {
            int n1 = o1.getNiceLevel();
            int n2 = o2.getNiceLevel();

            if (n1 == n2)
                return 0;
            else if (n1 > n2)
                return 1;
            else
                return -1;
        }

    }
    
    @Override
    public void setMaximumThreadsPerNiceLevel(int[] threadsLimit) {
        for (int i = 0; i < threadsLimit.length; i++) {
            threadsPerNiceLevel.put(i, threadsLimit[i]);
        }


        new Thread(
                new DispatcherThread(tasksReadyToProcess, threadsPerNiceLevel,
                        threads, globalExecutor)).start();
        //}
        System.out.println("setMaximumThreadsPerNiceLevel " + threadsPerNiceLevel);
    }

    @Override
    public void newTask(TaskInterface ti) {
        try {
            tasksReadyToProcess.put(ti);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


class DispatcherThread implements Runnable {

    private LinkedBlockingQueue<TaskInterface> tasksReadyToProcess;
    private Map<Integer, Integer> threadsPerNiceLevel;
    private final int threads;

    private Executor globalExecutor;


    public DispatcherThread(LinkedBlockingQueue<TaskInterface> tasksReadyToProcess, Map<Integer, Integer> threadsPerNiceLevel, int threads, Executor globalExecutor) {
        this.tasksReadyToProcess = tasksReadyToProcess;
        this.threadsPerNiceLevel = threadsPerNiceLevel;


        this.threads = threads;

        this.globalExecutor = globalExecutor;
    }


    @Override
    public void run() {
        while (true) {
            try{
                final TaskInterface taskInterface  = tasksReadyToProcess.take();
                globalExecutor.execute(new RunnableWithNiceLevel(taskInterface) {
                    @Override
                    public void run() {
                        taskInterface.execute();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

abstract class RunnableWithNiceLevel implements Runnable {

    private TaskInterface taskInterface;

    public RunnableWithNiceLevel(TaskInterface taskInterface) {
        this.taskInterface = taskInterface;
    }

    public TaskInterface getTaskInterface() {
        return taskInterface;
    }
    
    public int getNiceLevel(){
        return taskInterface.getNiceLevel();
    }
    
}
