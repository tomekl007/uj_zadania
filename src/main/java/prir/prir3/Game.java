package prir.prir3;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Game implements GameInterface, Serializable, Remote {
  private int id_pool = 0;
  private LinkedList<Team> teams;

  public Game() {
    teams = new LinkedList<Team>();
  }

  @Override
  public long register() throws RemoteException {
    synchronized (teams) {
      if (teams.size() < 1 || teams.getLast().size() >= PLAYERS_IN_TEAM)
        teams.add(new Team());
      int newID = newID();
      teams.getLast().addPlayer(newID);
      return newID;
    }
  }

  @Override
  public boolean isGameReady(long uid) throws RemoteException {
    synchronized (teams) {
      for (Team t : teams)
        if (t.contains(uid) && t.size() == PLAYERS_IN_TEAM)
          return true;
    }
    return false;
  }

  @Override
  public long[] getTeam(long uid) throws RemoteException, PlayerNotAssignedToGame {
    return findTeam(uid).getTeam();
  }

  @Override
  public void move(Move mv) throws RemoteException, MoveAlreadyDone, PlayerNotAssignedToGame {
    findTeam(mv.uid).move(mv);
  }

  @Override
  public Move getMove(long uid) throws RemoteException, PlayerNotAssignedToGame {
    return findTeam(uid).getMove(uid);
  }

  @Override
  public int getPhase(long uid) throws RemoteException, PlayerNotAssignedToGame {
    return findTeam(uid).getPhase();
  }

  private Team findTeam(long uid) throws PlayerNotAssignedToGame {
    synchronized (teams) {
      for (Team t : teams) {
        if (t.contains(uid))
          return t;
      }
    }
    throw new PlayerNotAssignedToGame();
  }

  private int newID() {
    return ++id_pool;
  }

  private class Team {
    private AtomicInteger phase;
    private LinkedList<Move> moves;
    private ConcurrentHashMap<Long, Player> playerMap;

    Team() {
      phase = new AtomicInteger(1);
      moves = new LinkedList<Move>();
      playerMap = new ConcurrentHashMap<Long, Player>();
    }

    public void addPlayer(long uid) {
      playerMap.put(uid, new Player(moves));
    }

    public Player find(long uid) {
      return playerMap.get(uid);
    }

    public boolean contains(long uid) {
      return find(uid) != null;
    }

    public int getPhase() {
      return phase.get();
    }

    public long[] getTeam() {
      Long[] keySet = playerMap.keySet().toArray(new Long[size()]);
      long[] ids = new long[size()];
      for (int i = 0; i < size(); ++i)
        ids[i] = keySet[i];
      return ids;
    }

    public int size() {
      return playerMap.size();
    }

    public void move(Move mv) throws MoveAlreadyDone {
      synchronized (moves) {
        if (hasMoved(mv.uid))
          throw new MoveAlreadyDone();
        moves.add(mv);
        if (moves.size() != 0 && moves.size() % PLAYERS_IN_TEAM == 0)
          phase.incrementAndGet();
      }
    }

    public Move getMove(long uid) throws PlayerNotAssignedToGame {
      Player player = playerMap.get(uid);
      if (player == null)
        throw new PlayerNotAssignedToGame();
      Move mv;
      while (true) {
        mv = player.getMove();
        if (mv == null)
          return null;
        if (mv.uid != uid)
          return mv;
      }
    }

    private boolean hasMoved(long uid) {
      synchronized (moves) {
        Iterator<Move> it = moves.descendingIterator();
        while (it.hasNext()) {
          Move mv = it.next();
          if (mv.phase != getPhase())
            return false;
          if (mv.uid == uid)
            return true;
        }
      }
      return false;
    }
  }

  private class Player {
    private AtomicInteger moveRequest;
    private LinkedList<Move> moves;

    Player(LinkedList<Move> moves) {
      moveRequest = new AtomicInteger(0);
      this.moves = moves;
    }

    public Move getMove() {
      if (moveRequest.get() >= moves.size())
        return null;
      return moves.get(moveRequest.getAndIncrement());
    }
  }
}


class RmiStarter {
    
    public RmiStarter() throws RemoteException {
        super();
    }

    public static final String REGISTRY_NAME =
            "GAME";

    public static void main(String[] args) throws Exception {
        int registryPortNumber = 1099;

        GameInterface game = new Game();

        LocateRegistry
                .createRegistry(registryPortNumber)
                .rebind(REGISTRY_NAME, game);
        System.out.println("Server is running...");
        Thread.sleep(1000000000);
    }
}
