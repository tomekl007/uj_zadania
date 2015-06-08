package prir.prir3;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Game implements GameInterface, Serializable {
    private int platerId = 0;
    private AtomicInteger playerCounter = new AtomicInteger(0);
    private final LinkedList<Team> teams = new LinkedList<>();
    private final Lock teamsRegsisterLock = new ReentrantLock();


    @Override
    public long register() throws RemoteException {
        teamsRegsisterLock.lock();
        try {
            long id = ++platerId;
            boolean joined = false;

            for (Team team : teams) {
                if (couldJoinToGame(team)) {
                    joined = true;
                    team.addPlayer(id);
                }
            }
            if (!joined) {
                Team newTeam = new Team();
                newTeam.addPlayer(id);
                teams.add(newTeam);
            }
            return id;
        } finally {
            teamsRegsisterLock.unlock();
        }
    }

    private boolean couldJoinToGame(Team team) {
        return team.players.size() < GameInterface.PLAYERS_IN_TEAM;
    }


    @Override
    public boolean isGameReady(long uid) throws RemoteException {
        teamsRegsisterLock.lock();
        try {
            for (Team team : teams) {
                for (Player player : team.players) {
                    if (player.playerId == uid && team.players.size() == PLAYERS_IN_TEAM) {
                        return true;
                    }
                }
            }
            return false;

        } finally {
            teamsRegsisterLock.unlock();
        }

    }


    @Override
    public long[] getTeam(long uid) throws RemoteException, PlayerNotAssignedToGame {
        for (Team team : teams) {
            for (Player player : team.players) {
                if (player.playerId == uid) {
                    long[] uids = new long[team.players.size()];
                    for (int i = 0; i < team.players.size(); i++) {
                        uids[i] = team.players.get(i).playerId;
                    }
                    return uids;
                }
            }
        }
        throw new PlayerNotAssignedToGame();
    }
    
    
    @Override
    public void move(Move mv) throws RemoteException, MoveAlreadyDone, PlayerNotAssignedToGame {
        findTeamForPlayer(mv.uid).move(mv);
    }

    @Override
    public Move getMove(long uid) throws RemoteException, PlayerNotAssignedToGame {
        return findTeamForPlayer(uid).getMove(uid);
    }

    @Override
    public int getPhase(long uid) throws RemoteException, PlayerNotAssignedToGame {
       return findTeamForPlayer(uid).getPhase();
    }

    private Team findTeamForPlayer(long uid) throws PlayerNotAssignedToGame {
        teamsRegsisterLock.lock();
        try {

            for (Team t : teams) {
                if (t.contains(uid)) {
                    return t;
                }
            }
            throw new PlayerNotAssignedToGame();
        } finally {
            teamsRegsisterLock.unlock();
        }
    }


    private class Team {
        private AtomicInteger phase;
        private final LinkedList<Move> moves;
        public final List<Player> players;

        Team() {
            phase = new AtomicInteger(1);
            moves = new LinkedList<Move>();
            players = new LinkedList<>();
        }

        public void addPlayer(long uid) {
            players.add(new Player(moves, uid));
        }

        public Player find(long uid) {
            for (Player player : players) {
                if (player.playerId == uid) {
                  return player;  
                }
            }
            return null;
        }

        public boolean contains(long uid) {
            return find(uid) != null;
        }

        public int getPhase() {
            return phase.get();
        }

        public int size() {
            return players.size();

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
            Player player = null;
            for (Player p : players) {
                if (p.playerId == uid) {
                    player = p;
                }
            }

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
        public final long playerId;

        Player(LinkedList<Move> moves, long playerId) {
            this.playerId = playerId;
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
