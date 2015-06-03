package prir.zad1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class Management implements ManagementInterface {
    public static final int N_THREADS = 16;
    private Executor executor = Executors.newFixedThreadPool(N_THREADS);
    private AtomicInteger processingEngineIdGenerator = new AtomicInteger();
    private Map<Integer, ProcessingEngineInterface> processingEngines = new HashMap<>();
    private Map<Integer, LinkedBlockingQueue<EventInterface>> messagesToCheckIsImportant = new HashMap<>();
    private Map<Integer, LinkedBlockingQueue<EventInterface>> messagesReadyToProcess = new HashMap<>();


    @Override
    public void registerProcessingEngine(ProcessingEngineInterface pei) {
        int peiId = processingEngineIdGenerator.incrementAndGet();
        LinkedBlockingQueue<EventInterface> messagesToCheck = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<EventInterface> messagesReadyToProcess = new LinkedBlockingQueue<>();
        processingEngines.put(peiId, pei);
        this.messagesToCheckIsImportant.put(peiId, messagesToCheck);
        this.messagesReadyToProcess.put(peiId, messagesReadyToProcess);

        executor.execute(new IsImportantChecker(peiId, processingEngines, this.messagesToCheckIsImportant, this.messagesReadyToProcess, pei));
        executor.execute(new ProcessMessage(peiId, processingEngines, this.messagesReadyToProcess, pei));
    }

    @Override
    public void deregisterProcessingEngine(ProcessingEngineInterface pei) {
        for (Map.Entry<Integer, ProcessingEngineInterface> entry : processingEngines.entrySet()) {
            if (containtProcessingEngine(pei, entry)) {
                int procId = entry.getKey();
                removeProcessingEngineFromListeningOnEvents(procId);
            }
        }
    }

    private void removeProcessingEngineFromListeningOnEvents(int procId) {
        processingEngines.remove(procId);
        messagesToCheckIsImportant.remove(procId);
        messagesReadyToProcess.remove(procId);
    }

    private boolean containtProcessingEngine(ProcessingEngineInterface pei, Map.Entry<Integer, ProcessingEngineInterface> entry) {
        return pei.equals(entry.getValue());
    }

    @Override
    public void newEvent(EventInterface ei) {
        for (Map.Entry<Integer, ProcessingEngineInterface> entry : processingEngines.entrySet()) {
            Integer key = entry.getKey();
            addToMessagesToCheckIsImportant(ei, key);
        }
    }

    private void addToMessagesToCheckIsImportant(EventInterface ei, Integer key) {
        BlockingQueue<EventInterface> preQueue = messagesToCheckIsImportant.get(key);
        try {
            preQueue.put(ei);
        } catch (InterruptedException e) {
        }
    }
}


class IsImportantChecker implements Runnable {
    private int peId;
    private ProcessingEngineInterface processingEngineInterface;
    private final Map<Integer, ProcessingEngineInterface> allProcessingEngines;
    LinkedBlockingQueue<EventInterface> messagesToCheck;
    LinkedBlockingQueue<EventInterface> messagesToProcess;

    public IsImportantChecker(int peId, Map<Integer, ProcessingEngineInterface> allProcessingEngines,
                              Map<Integer, LinkedBlockingQueue<EventInterface>> messagesToCheck,
                              Map<Integer, LinkedBlockingQueue<EventInterface>> messagesToProcess,
                              ProcessingEngineInterface pei) {
        this.peId = peId;
        this.allProcessingEngines = allProcessingEngines;
        this.messagesToCheck = messagesToCheck.get(peId);
        this.messagesToProcess = messagesToProcess.get(peId);
        this.processingEngineInterface = pei;
    }

    @Override
    public void run() {

        while (processingEngineIsStillRegistered()) {//is still registered
            try {
                addToMessagesToProcessIfItsImportant();
            } catch (InterruptedException e) {
            }
        }
    }

    private void addToMessagesToProcessIfItsImportant() throws InterruptedException {
        EventInterface eventInterface = messagesToCheck.take();
        if (processingEngineInterface.isItImportant(eventInterface)) {
            messagesToProcess.put(eventInterface);
        }
    }

    private boolean processingEngineIsStillRegistered() {
        return allProcessingEngines.containsValue(processingEngineInterface);
    }
}


class ProcessMessage implements Runnable {
    private final ProcessingEngineInterface processingEngineInterface;
    private int peId;
    private final Map<Integer, ProcessingEngineInterface> allProcessingEngines;
    LinkedBlockingQueue<EventInterface> messagesToProcess;

    public ProcessMessage(int peId, Map<Integer, ProcessingEngineInterface> allProcessingEngines,
                          Map<Integer, LinkedBlockingQueue<EventInterface>> messagesToProcess,
                          ProcessingEngineInterface pei) {
        this.peId = peId;
        this.allProcessingEngines = allProcessingEngines;
        this.messagesToProcess = messagesToProcess.get(peId);
        this.processingEngineInterface = pei;
    }

    @Override
    public void run() {
        try {
            while (processingEngineIsStillRegistered()) {
                takeEventAndProcess();
            }
        } catch (InterruptedException e) {
        }
    }

    private void takeEventAndProcess() throws InterruptedException {
        EventInterface ei = messagesToProcess.take();
        processingEngineInterface.processEvent(ei);
    }

    private boolean processingEngineIsStillRegistered() {
        return allProcessingEngines.containsValue(processingEngineInterface);
    }
}