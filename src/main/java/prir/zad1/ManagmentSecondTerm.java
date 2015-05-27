package prir.zad1;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

class ManagmentSecondTerm implements ManagementInterface {
    public static final int N_THREADS = 16;
    private Executor executor = Executors.newFixedThreadPool(N_THREADS);
    private AtomicInteger processingEngineIdGenerator = new AtomicInteger();
    private HashMap<Integer, ProcessingEngineInterface> processorsMap = new HashMap<>();
    private HashMap<Integer, LinkedBlockingQueue<EventInterface>> messagesToCheck = new HashMap<>();
    private HashMap<Integer, LinkedBlockingQueue<EventInterface>> messagesReadyToProcess = new HashMap<>();


    @Override
    public void registerProcessingEngine(ProcessingEngineInterface pei) {
        int peiId = processingEngineIdGenerator.incrementAndGet();
        LinkedBlockingQueue<EventInterface> messagesToCheck = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<EventInterface> messagesReadyToProcess = new LinkedBlockingQueue<>();
        processorsMap.put(peiId, pei);
        this.messagesToCheck.put(peiId, messagesToCheck);
        this.messagesReadyToProcess.put(peiId, messagesReadyToProcess);
        
        executor.execute(new IsRegisterChecker(peiId, processorsMap, this.messagesToCheck, this.messagesReadyToProcess, pei));
        executor.execute(new ProcessMessage(peiId, processorsMap, this.messagesReadyToProcess, pei));
    }

    @Override
    public void deregisterProcessingEngine(ProcessingEngineInterface pei) {
        for (Map.Entry<Integer, ProcessingEngineInterface> entry : processorsMap.entrySet()) {
            if (pei.equals(entry.getValue())){
                int procId = entry.getKey();
                processorsMap.remove(procId);
                messagesToCheck.remove(procId);
                messagesReadyToProcess.remove(procId);
            }
        }
    }

    @Override
    public void newEvent(EventInterface ei) {
        for (Map.Entry<Integer, ProcessingEngineInterface> entry : processorsMap.entrySet()) {
            Integer key = entry.getKey();
            BlockingQueue<EventInterface> preQueue = messagesToCheck.get(key);
            try {
                preQueue.put(ei);
            } catch (InterruptedException e) {
            }
        }
    }
}


class IsRegisterChecker implements Runnable {
    private int peId;
    private ProcessingEngineInterface processingEngineInterface;
    private final Map<Integer, ProcessingEngineInterface> allProcessingEngines;
    LinkedBlockingQueue<EventInterface> messagesToCheck;
    LinkedBlockingQueue<EventInterface> messagesToProcess;

    public IsRegisterChecker(int peId, Map<Integer, ProcessingEngineInterface> allProcessingEngines,
                             Map<Integer, LinkedBlockingQueue<EventInterface>> messagesToCheck,
                             Map<Integer, LinkedBlockingQueue<EventInterface>> messagesToProcess, ProcessingEngineInterface pei) {
        this.peId = peId;
        this.allProcessingEngines = allProcessingEngines;
        this.messagesToCheck = messagesToCheck.get(peId);
        this.messagesToProcess = messagesToProcess.get(peId);
        this.processingEngineInterface = pei;
    }

    @Override
    public void run() {
        try {
            while (allProcessingEngines.containsValue(processingEngineInterface)) {
                EventInterface eventInterface = messagesToCheck.take();
                if (processingEngineInterface.isItImportant(eventInterface)) {
                    messagesToProcess.put(eventInterface);
                }
            }
        } catch (InterruptedException e) {
        }
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
            while (allProcessingEngines.containsValue(processingEngineInterface)) {
                EventInterface ei = messagesToProcess.take();
                processingEngineInterface.processEvent(ei);
            }
        } catch (InterruptedException e) {
        }
    }
}