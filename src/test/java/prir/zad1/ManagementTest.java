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
        ManagmentSecondTerm ManagmentSecondTerm = new ManagmentSecondTerm();
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

        ManagmentSecondTerm.registerProcessingEngine(processingEngineInterface);
        ManagmentSecondTerm.registerProcessingEngine(processingEngineInterfaceNotImportant);
        //when
        ManagmentSecondTerm.newEvent(new EventInterface() {
        });
        ManagmentSecondTerm.newEvent(new EventInterface() {
        });
        ManagmentSecondTerm.newEvent(new EventInterface() {
        });
        Thread.sleep(20000);
    }

    @Test
    public void shouldProcessEventForTwoListener() {
        //given
        ManagmentSecondTerm ManagmentSecondTerm = new ManagmentSecondTerm();
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
        ManagmentSecondTerm.registerProcessingEngine(processingEngineInterface);
        ManagmentSecondTerm.registerProcessingEngine(processingEngineInterface);
        //when
        ManagmentSecondTerm.newEvent(new EventInterface() {
        });
        ManagmentSecondTerm.newEvent(new EventInterface() {
        });
    }

    @Test
    public void shouldProcessEventForTwoListenerFromTwoThreads() throws InterruptedException {
        //given
        Executor executor = Executors.newFixedThreadPool(10);
        final ManagmentSecondTerm ManagmentSecondTerm = new ManagmentSecondTerm();
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
        ManagmentSecondTerm.registerProcessingEngine(processingEngineInterface);
        ManagmentSecondTerm.registerProcessingEngine(processingEngineInterface);
        //when
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ManagmentSecondTerm.newEvent(new EventInterface() {
                    });
                }
            }
        };
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    ManagmentSecondTerm.newEvent(new EventInterface() {
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
        final ManagmentSecondTerm ManagmentSecondTerm = new ManagmentSecondTerm();


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
        //ManagementMichal.registerProcessingEngine(processingEngineInterface);
        ManagmentSecondTerm.registerProcessingEngine(blockingProcessingEngineInterface);
        //when

        for (int i = 0; i < 10; i++) {
            ManagmentSecondTerm.newEvent(new EventInterface() {});
        }
        Thread.sleep(10000);
    }


    @Test
    public void shouldProcessEventForTwoListenerFromTwoThreadsAndForOneThreadAfterDeregistering() throws InterruptedException {
        //given
        Executor executor = Executors.newFixedThreadPool(10);
        final ManagmentSecondTerm ManagmentSecondTerm = new ManagmentSecondTerm();
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

        ManagmentSecondTerm.registerProcessingEngine(firstProcessingEngine);
        ManagmentSecondTerm.registerProcessingEngine(secondProcessingEngine);
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
                    ManagmentSecondTerm.newEvent(new EventInterface() {
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
                    ManagmentSecondTerm.newEvent(new EventInterface() {
                    });
                }
            }
        };
        executor.execute(runnable);
        executor.execute(runnable1);

        Thread.sleep(200);
        System.out.println("unregister second");
        ManagmentSecondTerm.deregisterProcessingEngine(secondProcessingEngine);
        Thread.sleep(1000);

    }

}