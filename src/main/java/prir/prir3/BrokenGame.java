package prir.prir3;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

 public class BrokenGame implements GameInterface, Serializable, Remote {

    private static final AtomicLong playerCounter = new AtomicLong();
    final List<SpecificGame> games = Collections.synchronizedList(new LinkedList<SpecificGame>());
    Lock registerLock = new ReentrantLock();
    Lock addToMovesLock = new ReentrantLock();
    Lock getAllMovesLock = new ReentrantLock();
    Lock getMovesLock = new ReentrantLock();
     //Executor executor = Executors.newSingleThreadExecutor(); //todo it cause index out of bounds
    private final Map<Long, LinkedBlockingQueue<Move>> movesToProcess = Collections.synchronizedMap(new HashMap<Long, LinkedBlockingQueue<Move>>());

     private final LinkedBlockingQueue<Move> allMoves = new LinkedBlockingQueue<Move>();
    private Map<Long, Player> players = Collections.synchronizedMap(new HashMap<Long, Player>());

     public BrokenGame() throws RemoteException {
         new Thread(new PlayerThreadSync(games, players, allMoves)).start(); //todo problem is that it starts many threads,
     }

     //todo maybe map of thread, and each player has own registered thread that handles his actions
    @Override
    public long register() throws RemoteException {
        registerLock.lock();
        long id = playerCounter.getAndIncrement();
        boolean joined = false;
        Player newPlayer = new Player(id);
        for (SpecificGame game : games) {
            if (game.couldJoinToGame()) {
                joined = true;
                game.players.add(newPlayer);
                players.put(id, newPlayer);
                movesToProcess.put(id, new LinkedBlockingQueue<Move>());
                //new Thread(new PlayerThread(game, newPlayer, players, movesToProcess)); //todo problem is that it starts many threads, 
                
            }
        }
        if (!joined) {
            SpecificGame game1 = new SpecificGame();
            game1.players.add(newPlayer);
            players.put(id, newPlayer);
            games.add(game1);
            movesToProcess.put(id, new LinkedBlockingQueue<Move>());
            //new Thread(new PlayerThread(game1, newPlayer, players, movesToProcess));
        }
        registerLock.unlock();
        return id;
    }

     boolean couldJoinToGame() {
         return players.size() < BrokenGame.PLAYERS_IN_TEAM;

     }


     @Override
    public boolean isGameReady(long uid) throws RemoteException {
        for (SpecificGame game : games) {
            for (Player player : game.players) {
                if (player.playerId == uid && game.players.size() == PLAYERS_IN_TEAM) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public long[] getTeam(long uid) throws RemoteException, PlayerNotAssignedToGame {
        for (SpecificGame game : games) {
            for (Player player : game.players) {
                if (player.playerId == uid) {
                    long[] uids = new long[game.players.size()];
                    for (int i = 0; i < game.players.size(); i++) {
                        uids[i] = game.players.get(i).playerId;
                    }
                    return uids;
                }
            }
        }
        throw new PlayerNotAssignedToGame();
    }

    @Override
    public void move(Move mv) throws RemoteException, MoveAlreadyDone, PlayerNotAssignedToGame {
       /* synchronized (movesToProcess) {
            try {

                LinkedBlockingQueue<Move> moves = movesToProcess.get(mv.uid);
                if (moves == null) {
                    throw new PlayerNotAssignedToGame();
                } else {
                    moves.put(mv);
                    movesToProcess.put(mv.uid, moves);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
*/
        try {
            System.out.println("--> first arrived : " + mv + " "  + new Date().getTime()) ;
            allMoves.put(mv);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    
        
    }

    @Override
    synchronized public Move getMove(final long uid) throws RemoteException, PlayerNotAssignedToGame {
        getAllMovesLock.lock();
        try {
            synchronized (games) {
                for (SpecificGame game : games) {
                    synchronized (game.players) {
                        for (Player player : game.players) {
                            if (player.playerId == uid) {
                                System.out.println("plyr.moves" + player.movesInCurrentGame + " for player : " + player);
                                System.out.println("game.moves" + game.moves + " for game.player : " + player);
                                //return getLastWithoutUserId(game.moves, game.moves, uid);
                                return getMoveUsingHistoryId(player);
                                //return getLastWithoutUserId(player.movesInCurrentGame, player.movesInCurrentGame, uid);
                            }
                        }
                    }
                }
            }

            throw new PlayerNotAssignedToGame();
        } finally {
            getAllMovesLock.unlock();
        }
    }


    private Move getMoveUsingHistoryId(Player player) {
        synchronized (player.movesInCurrentGame) {
            int historyIdCurrent = player.historyId.getAndIncrement();
            if (historyIdCurrent > player.movesInCurrentGame.size() || player.movesInCurrentGame.size() == 0 ) return null;
            return player.movesInCurrentGame.get(historyIdCurrent);
        }
    }

    public Move getLastWithoutUserId(List<Move> moves, List<Move> original, long uid) {

        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).uid != uid) {
                Move mv = moves.get(i);
                Move removed = moves.remove(i);
                original.remove(removed);
                return mv;
            } else if (moves.get(i).uid == uid) {
                ArrayList<Move> clone = new ArrayList<Move>();
                clone.addAll(moves);
                clone.remove(i);
                return getLastWithoutUserId(clone, original, uid);
            }
        }
        return null;
    }

    @Override
    public int getPhase(long uid) throws RemoteException, PlayerNotAssignedToGame {
        synchronized (games) {
            for (SpecificGame game : games) {
                synchronized (game.players) {
                    for (Player player : game.players) {
                        if (player.playerId == uid) {
                            return game.phase.get();
                        }
                    }
                }
            }
        }
        throw new PlayerNotAssignedToGame();
    }

    class SpecificGame {
        final List<Player> players = Collections.synchronizedList(new LinkedList<Player>());
        List<Move> moves = Collections.synchronizedList(new ArrayList<Move>());
        AtomicInteger phase = new AtomicInteger(1);

        boolean couldJoinToGame() {
            return players.size() < BrokenGame.PLAYERS_IN_TEAM;

        }

    }

    class Player {
        long playerId;
        boolean moved = false;
        long code;
        AtomicInteger historyId = new AtomicInteger(0);
        public final List<Move> movesInCurrentGame = Collections.synchronizedList(new ArrayList<Move>());


        public Player(long playerId) {
            this.playerId = playerId;

        }

        @Override
        public String toString() {
            return "Player{" +
                    "playerId=" + playerId +
                    ", moved=" + moved +
                    ", code=" + code +
                    ",historyId" + historyId +
                    '}';
        }
    }

}

class PlayerThread implements Runnable {


    private final BrokenGame.SpecificGame game;
    private final BrokenGame.Player newPlayer;
    private Map<Long, BrokenGame.Player> players;
    private Map<Long, LinkedBlockingQueue<GameInterface.Move>> movesToProcess;

    public PlayerThread(BrokenGame.SpecificGame game, BrokenGame.Player newPlayer, Map<Long, BrokenGame.Player> players, Map<Long, LinkedBlockingQueue<GameInterface.Move>> movesToProcess) {
        this.game = game;
        this.newPlayer = newPlayer;
        this.players = players;
        this.movesToProcess = movesToProcess;
    }

    @Override
    public void run() {
        while (players.containsValue(newPlayer)) {
            try {
                BrokenGame.Move move = movesToProcess.get(newPlayer.playerId).take(); //todo and we do not know in which order threads will take that and process, should be linear
                //todo one thread should process that
                System.out.println("player " + this.newPlayer.playerId + " consume " + move);

                if (allPlayersMovedInGivenPhase(game, move)) {
                    game.phase.incrementAndGet();
                    synchronized (game.players) {
                        for (BrokenGame.Player player : game.players) {
                            player.moved = false;
                        }
                    }
                }


                if (newPlayer.playerId == move.uid) {
                    if (couldMakeMove(game, move) && !newPlayer.moved) {
                        System.out.println("move maked");
                        newPlayer.moved = true;
                        newPlayer.code = move.code;
                        addMoveToGame(game, move);
                        synchronized (game.players) {
                            addMoveToAllPlayers(game.players, move);
                        }
                        return;
                    } else {
                        System.out.println("move already done");
                        //throw new GameInterface.MoveAlreadyDone();
                    }
                } else {
                    System.out.println("ignoring move");
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("player not assigment");
    }

    private void addMoveToGame(BrokenGame.SpecificGame game, GameInterface.Move move) {
        game.moves.add(move);
    }


    private boolean allPlayersMovedInGivenPhase(BrokenGame.SpecificGame game, GameInterface.Move mv) {
        synchronized (game.players) {
            for (BrokenGame.Player player : game.players) {
                if (!player.moved) return false;
            }
            return true;
        }
    }


    private boolean couldMakeMove(BrokenGame.SpecificGame game, GameInterface.Move mv) {
        return game.phase.get() == mv.phase;
    }

    private void addMoveToAllPlayers(final List<BrokenGame.Player> players, GameInterface.Move mv) {

        for (BrokenGame.Player player : players) {
            if (player.playerId != mv.uid) {
                player.movesInCurrentGame.add(mv);
            } else {
                System.out.println("not adding move for myself" + player);
            }
        }
    }


}

class MoveAlreadyDoneRuntimeException extends RuntimeException {


}


class PlayerThreadSync implements Runnable {


    private final List<BrokenGame.SpecificGame> game;
    
    private Map<Long, BrokenGame.Player> players;
    private LinkedBlockingQueue<GameInterface.Move> movesToProcess;

    public PlayerThreadSync(List<BrokenGame.SpecificGame> game, Map<Long, BrokenGame.Player> players, LinkedBlockingQueue<GameInterface.Move> movesToProcess) {
        this.game = game;
    
        this.players = players;
        this.movesToProcess = movesToProcess;
    }

    @Override
    public void run() {
        while (true) {
            try {
                BrokenGame.Move move = movesToProcess.take();
                System.out.println("player " + move.uid+ " consume " + move);
                BrokenGame.SpecificGame currentGame = null;
                BrokenGame.Player newPlayer = null;
                synchronized (game) {
                    for (BrokenGame.SpecificGame gameS : game) {
                        synchronized (gameS.players) {
                            for (BrokenGame.Player player : gameS.players) {
                                if (player.playerId == move.uid) {
                                    currentGame = gameS;
                                    newPlayer = player;
                                }
                            }
                        }
                    }
                }
                
                if(currentGame == null || newPlayer == null){
                    continue;//todo check it out
                }
                
                

                if (allPlayersMovedInGivenPhase(currentGame, move)) {
                    currentGame.phase.incrementAndGet();
                    synchronized (currentGame.players) {
                        for (BrokenGame.Player player : currentGame.players) {
                            player.moved = false;
                        }
                    }
                }



                    if (couldMakeMove(currentGame, move) && !newPlayer.moved) {
                        System.out.println("move maked");
                        newPlayer.moved = true;
                        newPlayer.code = move.code;
                        addMoveToGame(currentGame, move);
                        synchronized (currentGame.players) {
                            addMoveToAllPlayers(currentGame.players, move);
                        }
                       // return;
                    } else {
                        System.out.println("move already done");//todo move that verification into move()
                        //throw new GameInterface.MoveAlreadyDone();
                    }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       // System.out.println("player not assigment");
    }

    private void addMoveToGame(BrokenGame.SpecificGame game, GameInterface.Move move) {
        game.moves.add(move);
    }


    private boolean allPlayersMovedInGivenPhase(BrokenGame.SpecificGame game, GameInterface.Move mv) {
        synchronized (game.players) {
            for (BrokenGame.Player player : game.players) {
                if (!player.moved) return false;
            }
            return true;
        }
    }


    private boolean couldMakeMove(BrokenGame.SpecificGame game, GameInterface.Move mv) {
        return game.phase.get() == mv.phase;
    }

    private void addMoveToAllPlayers(final List<BrokenGame.Player> players, GameInterface.Move mv) {

        for (BrokenGame.Player player : players) {
            if (player.playerId != mv.uid) {
                player.movesInCurrentGame.add(mv);
            } else {
                System.out.println("not adding move for myself" + player);
            }
        }
    }


}