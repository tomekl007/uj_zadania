package prir.zad1;

import org.junit.Test;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class ManagementTest {
    @Test
    public void shouldProcessEventForOneListener() throws InterruptedException {
        //given
        Management management = new Management();
        ProcessingEngineInterface processingEngineInterface = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei)
            {
                System.out.println("is important" + new Date().getTime());
                return true;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                System.out.println("processing " + ei + " " + new Date().getTime());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("still processing " + ei + " " + new Date().getTime());
                assertTrue(true);
            }
        };

        ProcessingEngineInterface processingEngineInterfaceNotImportant = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei) {
                System.out.println("is not important " + new Date().getTime());
                return false;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                System.out.println("not processing " + ei + " " + new Date().getTime());
                assertTrue(false);
            }
        };

        management.registerProcessingEngine(processingEngineInterface);
        management.registerProcessingEngine(processingEngineInterfaceNotImportant);
        //when
        management.newEvent(new EventInterface() {
        });
        management.newEvent(new EventInterface() {
        });
        management.newEvent(new EventInterface() {
        });
        Thread.sleep(20000);
    }

    @Test
    public void shouldProcessEventForTwoListener() {
        //given
        Management management = new Management();
        ProcessingEngineInterface processingEngineInterface = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei) {
                return true;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                System.out.println("processing " + ei);
                assertTrue(true);
            }
        };
        management.registerProcessingEngine(processingEngineInterface);
        management.registerProcessingEngine(processingEngineInterface);
        //when
        management.newEvent(new EventInterface() {
        });
        management.newEvent(new EventInterface() {
        });
    }

    @Test
    public void shouldProcessEventForTwoListenerFromTwoThreads() throws InterruptedException {
        //given
        Executor executor = Executors.newFixedThreadPool(10);
        final Management management = new Management();
        ProcessingEngineInterface processingEngineInterface = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei) {
                return true;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                System.out.println("processing " + ei);
                assertTrue(true);
            }
        };
        management.registerProcessingEngine(processingEngineInterface);
        management.registerProcessingEngine(processingEngineInterface);
        //when
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    management.newEvent(new EventInterface() {
                    });
                }
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    management.newEvent(new EventInterface() {
                    });
                }
            }
        };
        executor.execute(runnable);
        executor.execute(runnable1);
        Thread.sleep(1000);

    }


    @Test
    public void shouldProcessEventForTwoListenerFromTwoThreadsWhenFirstListenerIsBlockingOnProcessEvent() throws InterruptedException {
        //given
        Executor executor = Executors.newFixedThreadPool(10);
        final Management management = new Management();


        ProcessingEngineInterface blockingProcessingEngineInterface = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei) {
                System.out.println("is important2 ? in : " + ei);
                return true;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                try {
                    System.out.println("processing2 " + ei);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.err.println("e : " + e);
                    e.printStackTrace();
                }

                assertTrue(true);
            }
        };
        //management.registerProcessingEngine(processingEngineInterface);
        management.registerProcessingEngine(blockingProcessingEngineInterface);
        //when

        for (int i = 0; i < 10; i++) {
            management.newEvent(new EventInterface() {});
        }
        Thread.sleep(10000);
    }


    @Test
    public void shouldProcessEventForTwoListenerFromTwoThreadsAndForOneThreadAfterDeregistering() throws InterruptedException {
        //given
        Executor executor = Executors.newFixedThreadPool(10);
        final Management management = new Management();
        ProcessingEngineInterface firstProcessingEngine = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei) {
                return true;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                System.out.println("processing first" + ei);
                assertTrue(true);
            }
        };

        ProcessingEngineInterface secondProcessingEngine = new ProcessingEngineInterface() {
            @Override
            public boolean isItImportant(EventInterface ei) {
                return true;
            }

            @Override
            public void processEvent(EventInterface ei) {
                //then
                System.out.println("processing second" + ei);
                assertTrue(true);
            }
        };
        
        management.registerProcessingEngine(firstProcessingEngine);
        management.registerProcessingEngine(secondProcessingEngine);
        //when
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    management.newEvent(new EventInterface() {
                    });
                }
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    management.newEvent(new EventInterface() {
                    });
                }
            }
        };
        executor.execute(runnable);
        executor.execute(runnable1);

        Thread.sleep(200);
        System.out.println("unregister second");
        management.deregisterProcessingEngine(secondProcessingEngine);
        Thread.sleep(1000);

    }

}