package prir.prir3;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Game implements GameInterface, Serializable {

    private static final AtomicLong playerCounter = new AtomicLong();
    List<SpecificGame> games = new LinkedList<>();


    @Override
    public long register() throws RemoteException {
        long id = playerCounter.getAndIncrement();
        boolean joined = false;
        for (SpecificGame game : games) {
            if (game.couldJoinToGame()) {
                joined = true;
                game.players.add(new Player(id));
            }
        }
        if (!joined) {
            SpecificGame game1 = new SpecificGame();
            game1.players.add(new Player(id));
            games.add(game1);
        }
        return id;
    }

    @Override
    public boolean isGameReady(long uid) throws RemoteException {
        for (SpecificGame game : games) {
            for (Player player : game.players) {
                if (player.playerId == uid) {
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
        for (SpecificGame game : games) {
            if (allPlayersMovedInGivenPhase(game, mv)) {
                game.phase++;
                for (Player player : game.players) {
                    player.moved = false;
                }
            }


            for (Player player : game.players) {
                if (player.playerId == mv.uid) {
                    if (couldMakeMove(game, mv) && !player.moved) {
                        player.moved = true;
                        player.code = mv.code;
                        return;
                    } else {
                        throw new MoveAlreadyDone();
                    }
                }
            }
        }
        throw new PlayerNotAssignedToGame();
    }

    private boolean allPlayersMovedInGivenPhase(SpecificGame game, Move mv) {
        for (Player player : game.players) {
            if (!player.moved) return false;
        }
        return true;
    }

    private boolean couldMakeMove(SpecificGame game, Move mv) {
        return game.phase == mv.phase;
    }

    @Override
    public Move getMove(long uid) throws RemoteException, PlayerNotAssignedToGame {
        for (SpecificGame game : games) {
            for (Player player : game.players) {
                if (player.playerId == uid) {
                    Move move = new Move();
                    move.code = player.code;
                    move.phase = game.phase;
                    move.uid = player.playerId;
                    return move;
                }
            }
        }
        throw new PlayerNotAssignedToGame();
    }

    @Override
    public int getPhase(long uid) throws RemoteException, PlayerNotAssignedToGame {
        for (SpecificGame game : games) {
            for (Player player : game.players) {
                if (player.playerId == uid) {
                    return game.phase;
                }
            }
        }
        throw new PlayerNotAssignedToGame();
    }

    class SpecificGame {
        List<Player> players = new LinkedList<>();
        int phase = 1;

        boolean couldJoinToGame() {
            return players.size() < Game.PLAYERS_IN_TEAM;

        }

    }

    class Player {
        long playerId;
        boolean moved = false;
        long code;

        public Player(long playerId) {
            this.playerId = playerId;

        }
    }
}
