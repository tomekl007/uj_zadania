package prir.zad2;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultithreadTaskManagementFinal implements MultithreadTaskManagementInterface {

  private Manager manager;

  MultithreadTaskManagementFinal() {
    manager = new Manager();
  }

  @Override
  public void setNumberOfAvailableThreads(int threads) {
    manager.setMaxThreads(threads);
  }

  @Override
  public void setMaximumThreadsPerNiceLevel(int[] threadsLimit) {
    for (int i = 0; i < threadsLimit.length; ++i)
      manager.addLimit(i, threadsLimit[i]);
  }

  @Override
  public void newTask(TaskInterface ti) {
    manager.addTask(ti);
  }

  private class Listener {
    private AtomicInteger usedThreads;

    Listener(AtomicInteger usedThreads) {
      this.usedThreads = usedThreads;
    }

    public void done() {
      usedThreads.decrementAndGet();
    }

    public void add() {
      usedThreads.incrementAndGet();
    }
  }

  private class Manager {
    private SortedMap<Integer, Level> taskMap;
    private AtomicInteger maxThreads, usedThreads;
    private Listener listener;
    private Thread scheduler;

    Manager() {
      taskMap = new TreeMap<Integer, Level>();
      maxThreads = new AtomicInteger(1);
      usedThreads = new AtomicInteger(0);
      listener = new Listener(usedThreads);
    }

    public void addLimit(int nlevel, int limit) {
      synchronized (taskMap) {
        taskMap.put(nlevel, new Level(limit, listener));
      }
    }

    public void setMaxThreads(int limit) {
      maxThreads.set(limit);
    }

    public void addTask(TaskInterface ti) {
      synchronized (taskMap) {
        if (!taskMap.containsKey(ti.getNiceLevel()))
          taskMap.put(ti.getNiceLevel(), new Level(1, listener));
        taskMap.get(ti.getNiceLevel()).getQueue().add(ti);
      }
      monitor();
    }

    private boolean empty() {
      synchronized (taskMap) {
        for (Level l : taskMap.values())
          if (l.getQueue().size() > 0)
            return false;
        return true;
      }
    }

    public void monitor() {
      if (scheduler != null && scheduler.isAlive())
        return;
      scheduler = new Thread(new Runnable() {

        private boolean levelIteration() throws InterruptedException {
          synchronized (taskMap) {
            for (Level l : taskMap.values()) {
              if (usedThreads.get() >= maxThreads.get()) {
                return false;
              }
              if (!l.isFull() && !l.getQueue().isEmpty())
                l.runJob(l.getQueue().poll());
            }
            return true;
          }
        }

        @Override
        public void run() {
          while (!empty()) {
            try {
              if (!levelIteration())
                synchronized (listener) {
                  listener.wait();
                }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      });
      scheduler.start();
    }
  }

  private class Level {
    private Integer limit;
    private AtomicInteger used;
    private ConcurrentLinkedQueue<TaskInterface> queue;
    private Listener listener;

    Level(int limit, Listener listener) {
      this.limit = limit;
      this.listener = listener;
      this.used = new AtomicInteger(0);
      queue = new ConcurrentLinkedQueue<TaskInterface>();
    }

    public ConcurrentLinkedQueue<TaskInterface> getQueue() {
      return queue;
    }

    public boolean isFull() {
      return used.get() >= limit;
    }

    public void runJob(final TaskInterface ti) {
      listener.add();
      used.incrementAndGet();
      new Thread(new Runnable() {
        @Override
        public void run() {
          ti.execute();
          listener.done();
          synchronized (listener) {
            listener.notifyAll();
          }
          used.decrementAndGet();
        }
      }).start();
    }
  }
}
