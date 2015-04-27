package prir.zad1;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

class Management implements ManagementInterface{
    public static final int N_THREADS = 16;
    private BlockingDeque<EventInterface> blockingDeque = new LinkedBlockingDeque<>();
    private Executor executor = Executors.newFixedThreadPool(N_THREADS);
    private List<ProcessingEngineInterface> processingEngines = new LinkedList<>();
    

    @Override
    public void registerProcessingEngine(final ProcessingEngineInterface pei) {
        processingEngines.add(pei);
    }

    @Override
    public void deregisterProcessingEngine(ProcessingEngineInterface pei) {
        processingEngines.remove(pei);
    }

    @Override
    public void newEvent(final EventInterface ei) {
        blockingDeque.offerFirst(ei);
        final EventInterface event = blockingDeque.pollLast();
        for( final ProcessingEngineInterface pei : processingEngines) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    boolean result = pei.isItImportant(event);
                    if(result)
                        pei.processEvent(event);
                }
            });
        }
    }
}
