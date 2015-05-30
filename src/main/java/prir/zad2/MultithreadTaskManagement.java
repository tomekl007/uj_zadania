package prir.zad2;

import prir.prir3.GameInterface;
import prir.zad1.EventInterface;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

class MultithreadTaskManagement implements MultithreadTaskManagementInterface{
    private Map<Integer, LinkedBlockingQueue<TaskInterface>> tasksReadyToProcess = new HashMap<>();
    private Map<Integer, Integer> threadsPerNiceLevel = new HashMap<>();
    private Executor globalExecutor;
    private int threads;

    public MultithreadTaskManagement(){}

    @Override
    public void setNumberOfAvailableThreads(int threads) {
        this.threads = threads;
        globalExecutor = Executors.newFixedThreadPool(threads);
    }

    @Override
    public void setMaximumThreadsPerNiceLevel(int[] threadsLimit) {
        int maxNiceLevel = 10;
        for (int i = 0; i < maxNiceLevel; i++) {
            threadsPerNiceLevel.put(i, 1);
        }
        
        for (int i = 0; i < threadsLimit.length; i++ ) {
            threadsPerNiceLevel.put(i, threadsLimit[i]);
        }

        Set<Map.Entry<Integer, Integer>> entries = threadsPerNiceLevel.entrySet();
        for(Map.Entry<Integer,Integer> entry : entries){
            new Thread(new DispatcherThread(entry.getKey(), entry.getValue(), tasksReadyToProcess, threadsPerNiceLevel, globalExecutor)).start();
        }
    }

    @Override
    public void newTask(TaskInterface ti) {
        LinkedBlockingQueue<TaskInterface> taskInterfaces = tasksReadyToProcess.get(ti.getNiceLevel());
        if(taskInterfaces == null){
            LinkedBlockingQueue<TaskInterface> taskInterfaces1 = new LinkedBlockingQueue<>();
            taskInterfaces1.add(ti);
            tasksReadyToProcess.put(ti.getNiceLevel(), taskInterfaces1);
        }else {
            taskInterfaces.add(ti);
            tasksReadyToProcess.put(ti.getNiceLevel(), taskInterfaces);
        }
    }
}


class DispatcherThread implements Runnable{

    private final Integer niceLeven;
    private final Integer numberOfThreads;
    private Map<Integer, LinkedBlockingQueue<TaskInterface>> tasksReadyToProcess;
    private Map<Integer, Integer> threadsPerNiceLevel;
    private Executor globalExecutor;
    private Executor executor;

    public DispatcherThread(Integer niceLevel, Integer numberOfThreads, Map<Integer, LinkedBlockingQueue<TaskInterface>> tasksReadyToProcess, Map<Integer, Integer> threadsPerNiceLevel, Executor globalExecutor){
        this.niceLeven = niceLevel;
        this.numberOfThreads = numberOfThreads;
        this.tasksReadyToProcess = tasksReadyToProcess;
        this.threadsPerNiceLevel = threadsPerNiceLevel;
        this.globalExecutor = globalExecutor;
        executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    @Override
    public void run() {
        while(threadsPerNiceLevel.containsKey(niceLeven)){
            LinkedBlockingQueue<TaskInterface> taskInterfaces = tasksReadyToProcess.get(niceLeven);
            if(taskInterfaces == null){
                continue;
            }
            final TaskInterface taskInterface;
            try {
                taskInterface = taskInterfaces.take();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        taskInterface.execute();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
           
            
        }
        System.out.println("exit for "+ niceLeven);
    }
}
