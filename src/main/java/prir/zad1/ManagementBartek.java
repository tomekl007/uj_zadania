package prir.zad1;

import prir.zad1.EventInterface;
import prir.zad1.ManagementInterface;
import prir.zad1.ProcessingEngineInterface;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by potaczekbartlomiej
 * 10/05/15
 */
class ManagementBartek implements ManagementInterface {
    private LinkedList<ProcessingEngineInterface> processingEngineList = new LinkedList<ProcessingEngineInterface>();
    private LinkedBlockingQueue<EventInterface> eventQueue = new LinkedBlockingQueue<EventInterface>();

    public ManagementBartek() {

    }

    @Override
    public void registerProcessingEngine(ProcessingEngineInterface pei) {
        processingEngineList.add(pei);
    }

    @Override
    public void deregisterProcessingEngine(ProcessingEngineInterface pei) {
        processingEngineList.remove(pei);
    }

    @Override
    public void newEvent(EventInterface ei) {
        eventQueue.add(ei);
        final EventInterface event = eventQueue.element();
        this.eventBroadcast(event);
    }
    void eventBroadcast(final EventInterface ei) {
        for( final ProcessingEngineInterface pei : processingEngineList ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if( pei.isItImportant(ei) ) {
                        executeProcess(pei, ei);
                    }
                }
            }).start();
        }
    }
    void executeProcess (final ProcessingEngineInterface pei, final EventInterface ei) {
        synchronized (ei) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    pei.processEvent(ei);
                }
            }).start();
        }
    }
}