package prir.zad1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ManagementWithProblems implements ManagementInterface {
    public static final int N_THREADS = 16;
    private Lock unregisterLock = new ReentrantLock();
    private Executor executor = Executors.newFixedThreadPool(N_THREADS);
    private Map<ProcessingEngineInterface, Processing> processingEngines = new ConcurrentHashMap<>();
    private LinkedBlockingQueue<EventInterface> eventsToCheck = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<EventInterface> eventsToProcess = new LinkedBlockingQueue<>();
    AtomicInteger eventIdGenerator = new AtomicInteger();

    @Override
    public void registerProcessingEngine(final ProcessingEngineInterface pei) {
        processingEngines.put(pei, new Processing(pei));
    }

    @Override
    public void deregisterProcessingEngine(ProcessingEngineInterface pei) {
        unregisterLock.lock();
        processingEngines.remove(pei);
        unregisterLock.unlock();

    }

    @Override
    public void newEvent(final EventInterface ei) {
        int eventId = eventIdGenerator.incrementAndGet();
        eventsToCheck.add(ei);
        try {
            if(unregisterLock.tryLock(1, TimeUnit.DAYS)) {
                for (final Processing processing : processingEngines.values()) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            processing.isImportantLock.lock();
                            boolean result = processing.pei.isItImportant(ei);
                            if (result) {
                                processing.isImportantLock.unlock();
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        processing.proccesEventLock.lock();
                                        processing.pei.processEvent(ei);
                                        processing.proccesEventLock.unlock();
                                    }
                                });
                            } else {
                                processing.isImportantLock.unlock();
                            }
                        }
                    });

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
