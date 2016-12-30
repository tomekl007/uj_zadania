package prir.prir3;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by potaczekbartlomiej
 * 21/05/15.
 */
public class Game implements GameInterface, Serializable {
    private long playerID = 1;
    private long teamID = 1;

    private LinkedBlockingQueue<Player> players = new LinkedBlockingQueue<Player>();
    private LinkedBlockingQueue<Team> teams = new LinkedBlockingQueue<Team>();

    public Game() {}

    public boolean isEqualToPlayersInTeam(int v1, int playersInTeam) {
        if( v1 == PLAYERS_IN_TEAM) {
            return true;
        }
        return false;
    }

    @Override
    public long register() throws RemoteException {
        synchronized (players) {
            Player newPlayer = new Player();
            players.add(newPlayer);

            if(isEqualToPlayersInTeam(players.size(),PLAYERS_IN_TEAM)) {
                Team newTeam = new Team();
                for(Player player : players) {
                    newTeam.addPlayer(player);
                }

                teams.add(newTeam);
                players.clear();
            }
            return newPlayer.id;
        }

    }

    @Override
    public boolean isGameReady(long uid) throws RemoteException {
        synchronized (teams) {
            for(Team team : teams) {
                for (Player player : team.players) {
                    if(player.id == uid && player.hasJoinedToTeam()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    @Override
    public long[] getTeam(long uid) throws RemoteException, PlayerNotAssignedToGame {

        synchronized (teams) {
            for(Team team : teams) {
                synchronized (team.players) {
                    for (Player player : team.players) {
                        if(player.id == uid) {
                            return team.getAllPlayersIDs();
                        }
                    }
                }
            }
            throw new PlayerNotAssignedToGame();
        }
    }

    @Override
    public void move(Move mv) throws RemoteException, MoveAlreadyDone, PlayerNotAssignedToGame {
        synchronized (teams) {
            for (Team team : teams) {
                synchronized (team.players) {
                    for (Player player : team.players) {
                        if (player.id == mv.uid) {
                            if (team.alreadyMoved(mv)) {
                                throw new MoveAlreadyDone();
                            }
                            if(team.gamePhase == mv.phase) {
                                team.nextPhase();
                                team.addMove(mv, player);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Move getMove(long uid) throws RemoteException, PlayerNotAssignedToGame {
        synchronized (teams) {
            for (Team team : teams) {
                synchronized (team.players) {
                    for (Player player : team.players) {
                        if (player.id == uid) {
                            return player.moves.peek();
                        }
                    }
                }

            }
        }
        throw new PlayerNotAssignedToGame();
    }

    @Override
    public int getPhase(long uid) throws RemoteException, PlayerNotAssignedToGame {
        synchronized ( teams ) {
            for (Team team : teams) {
                synchronized (team.players) {
                    for (Player player : team.players) {
                        return team.getPhase();
                    }
                }
            }
            throw new PlayerNotAssignedToGame();
        }
    }
    void increasePlayerID() {
        playerID ++;
    }
    void increaseTeamID() {
        teamID ++;
    }

    class Player {
        private long id;
        private boolean hasTeam;
        private LinkedList<Move> moves;

        public Player() {
            this.id = playerID;
            this.moves = new LinkedList<Move>();
            increasePlayerID();
        }

        private void joinToTeam() {
            this.hasTeam = true;
        }

        private boolean hasJoinedToTeam() {
            return hasTeam;
        }
    }
    class Team {
        private long id;
        private LinkedList<Player> players;
        private LinkedList<Move> moves = new LinkedList<Move>();
        private int gamePhase;
        private int playersMovesCounter;

        public Team() {
            this.id = teamID;
            increaseTeamID();
            this.players = new LinkedList<Player>();
            this.gamePhase = 1;
            this.playersMovesCounter = 0;

        }

        void addPlayer(Player p) {
            players.add(p);
            p.joinToTeam();
        }

        long[] getAllPlayersIDs() {
            long[] playersIDs = new long[this.players.size()];
            for (int i = 0; i < this.players.size(); i++) {
                playersIDs[i] = this.players.get(i).id;
            }
            return playersIDs;
        }

        private void addMove(Move mv, Player playerNotToMove) {
            moves.add(mv);
            for (Player player : this.players) {
                if(player != playerNotToMove) {
                    player.moves.add(mv);
                }
            }
        }

        private boolean alreadyMoved(Move mv) {
            for (Move move : moves) {
                if (move.phase == mv.phase && move.uid == mv.uid) {
                    return true;
                }
            }
            return false;
        }

        private void nextPhase() {
            this.playersMovesCounter++;
            if(isEqualToPlayersInTeam(this.playersMovesCounter,PLAYERS_IN_TEAM)) {
                this.gamePhase++;
                this.playersMovesCounter = 0;
            }
        }
        int getPhase() {
            return this.gamePhase;
        }
    }
}