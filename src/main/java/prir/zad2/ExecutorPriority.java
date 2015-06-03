package prir.zad2;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/*public class ExecutorPriority {

public static void main(String[] args) {

    PriorityBlockingQueue<Runnable> pq = new PriorityBlockingQueue<Runnable>(20, new ComparePriority<RunWithPriority>());

    Executor exe = new ThreadPoolExecutor(1, 2, 10, TimeUnit.SECONDS, pq);
    exe.execute(new RunWithPriority(2) {

        @Override
        public void run() {

            System.out.println(this.getPriority() + " started");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExecutorPriority.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(this.getPriority() + " finished");
        }
    });
    exe.execute(new RunWithPriority(10) {

        @Override
        public void run() {
            System.out.println(this.getPriority() + " started");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ExecutorPriority.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(this.getPriority() + " finished");
        }
    });

}

private static class ComparePriority<T extends RunWithPriority> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        return o1.getPriority().compareTo(o2.getPriority());
    }
}
}

class RunWithPriority implements Runnable, Comparable<RunWithPriority> {

    int priority;

    public RunWithPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public void run() {
        System.out.println("run");
    }

    @Override
    public int compareTo(RunWithPriority o) {
        return o.getPriority().compareTo(o2.getPriority());
    }
}*/