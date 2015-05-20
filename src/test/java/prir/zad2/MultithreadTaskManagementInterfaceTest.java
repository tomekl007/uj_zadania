package prir.zad2;

import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

public class MultithreadTaskManagementInterfaceTest {
    
    @Test
    public void shouldExecuteTask() throws InterruptedException {
        //gievn
        MultithreadTaskManagement multithreadTaskManagement = new MultithreadTaskManagement();
        //when
        multithreadTaskManagement.setNumberOfAvailableThreads(2);
        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(2, "0"));
        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(3, "0"));

        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(0, "1"));
        
        
        //
        
        Thread.sleep(1000);
    }


    @Test
    public void shouldExecuteTaskWithProperThreadCountPerNiceLevel() throws InterruptedException {
        //gievn
        Executor executor = Executors.newFixedThreadPool(10);
        final MultithreadTaskManagement multithreadTaskManagement = new MultithreadTaskManagement();
        //when
        multithreadTaskManagement.setNumberOfAvailableThreads(1);
        multithreadTaskManagement.setMaximumThreadsPerNiceLevel(new int[]{ //niceLevel, nrOfThreads
                0, 3,
                1, 2

        });
        executor.execute(new Runnable() {
            @Override
            public void run() {
                multithreadTaskManagement.newTask(taskWithNiceLevelAndId(3, "0"));
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                multithreadTaskManagement.newTask(taskWithNiceLevelAndId(0, "1"));
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                multithreadTaskManagement.newTask(taskWithNiceLevelAndId(3, "1"));
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                multithreadTaskManagement.newTask(taskWithNiceLevelAndId(0, "1"));
            }
        });


        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(1, "0"));
        
        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(1, "1"));
        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(1, "2"));
        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(0, "2"));
        
        multithreadTaskManagement.newTask(taskWithNiceLevelAndId(0, "3"));


        //

        Thread.sleep(5000);
    }

    private TaskInterface taskWithNiceLevelAndId(final int niceLevel, final String id) {
        return new TaskInterface() {
            @Override
            public int getNiceLevel() {
                return niceLevel;
            }

            @Override
            public void execute() {
                System.out.println("execute thread with nice level  " + niceLevel + " and id " + id);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assertTrue(true);
            }

            @Override
            public String toString() {
                return "task " + getNiceLevel();
            }
        };
    }

}