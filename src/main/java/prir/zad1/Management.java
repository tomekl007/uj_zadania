package prir.zad1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Management implements ManagementInterface {
    public static final int N_THREADS = 16;
    private BlockingDeque<EventInterface> blockingDeque = new LinkedBlockingDeque<>();
    private Executor executor = Executors.newFixedThreadPool(N_THREADS);
    private Map<ProcessingEngineInterface, Processing> processingEngines = new HashMap<>();


    @Override
    public void registerProcessingEngine(final ProcessingEngineInterface pei) {
        processingEngines.put(pei, new Processing(pei));
    }

    @Override
    public void deregisterProcessingEngine(ProcessingEngineInterface pei) {
        processingEngines.remove(pei);
    }

    @Override
    public void newEvent(final EventInterface ei) {
        blockingDeque.offerFirst(ei);
        final EventInterface event = blockingDeque.pollLast();
        for (final Processing processing : processingEngines.values()) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    processing.isImportantLock.lock();
                    boolean result = processing.pei.isItImportant(event);
                    if (result) {
                        processing.isImportantLock.unlock();
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                processing.proccesEventLock.lock();
                                processing.pei.processEvent(event);
                                processing.proccesEventLock.unlock();
                            }
                        });
                    }else{
                        processing.isImportantLock.unlock();
                    }
                }
            });

        }
    }
}

class Processing {


    ProcessingEngineInterface pei;
    Lock isImportantLock;
    Lock proccesEventLock;

    public Processing(ProcessingEngineInterface pei) {
        isImportantLock = new ReentrantLock();
        proccesEventLock = new ReentrantLock();
        this.pei = pei;
    }
}
