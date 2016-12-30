package prir.prir3;

import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GameTest {
    
    @Test
    public void shouldRegisterUser() throws RemoteException {
        //given
        BrokenGame game = new BrokenGame();
        //when
        long uidR1 = game.register();
        long uidR2 = game.register();
        //then
        boolean result = game.isGameReady(uidR1);
        assertTrue(result);

        boolean result2 = game.isGameReady(100000);
        assertFalse(result2);

        long[] team = game.getTeam(uidR1);
        System.out.println(team.length);
        assertEquals(team.length, 2);

    }



    @Test(expected = GameInterface.MoveAlreadyDone.class)
    public void shouldMoveInGameTwiceAndThrowForMoveForSamePhase() throws RemoteException {
        //given
        BrokenGame game = new BrokenGame();
        //when
        long uidR1 = game.register();
        long uidR2 = game.register();
        
        //then
        GameInterface.Move move = new GameInterface.Move();
        move.code = 100;
        move.phase = 1;
        move.uid = uidR1;
        game.move(move);

        game.move(move);
    }

    @Test(expected = GameInterface.PlayerNotAssignedToGame.class)
    public void shouldThrowIfPlayerNotAssigned() throws RemoteException {
        //given
        BrokenGame game = new BrokenGame();
        //when
        
        //then
        GameInterface.Move move = new GameInterface.Move();
        move.code = 100;
        move.phase = 1;
        move.uid = 1;
        game.move(move);

        game.move(move);
    }

    @Test
    public void shouldPlayGameWithTwoPlayers() throws RemoteException {
        //given
        BrokenGame game = new BrokenGame();
        //when
        long uidR1 = game.register();
        long uidR2 = game.register();

        //then
        GameInterface.Move moveFirst = new GameInterface.Move();
        moveFirst.code = 100;
        moveFirst.phase = 1;
        moveFirst.uid = uidR1;
        game.move(moveFirst);
        assertEquals(game.getMove(uidR1).code, 100);

        GameInterface.Move moveSecond = new GameInterface.Move();
        moveSecond.code = 101;
        moveSecond.phase = 1;
        moveSecond.uid = uidR2;
        game.move(moveSecond);
        assertEquals(game.getMove(uidR2).code, 101);
        
        //second turn 

        //then
        GameInterface.Move moveFirstSecondTurn = new GameInterface.Move();
        moveFirstSecondTurn.code = 101;
        moveFirstSecondTurn.phase = 2;
        moveFirstSecondTurn.uid = uidR1;
        game.move(moveFirstSecondTurn);
        assertEquals(game.getMove(uidR1).code, 101);

        GameInterface.Move moveSecondSecondTurn = new GameInterface.Move();
        moveSecondSecondTurn.code = 102;
        moveSecondSecondTurn.phase = 2;
        moveSecondSecondTurn.uid = uidR2;
        game.move(moveSecondSecondTurn);
        assertEquals(game.getMove(uidR2).code, 102);
    }


    @Test(expected = GameInterface.MoveAlreadyDone.class)
    public void shouldPlayGameWithTwoPlayersAndThrowWhenMoveAlreadyDone() throws RemoteException {
        //given
        BrokenGame game = new BrokenGame();
        //when
        long uidR1 = game.register();
        long uidR2 = game.register();

        //then
        GameInterface.Move moveFirst = new GameInterface.Move();
        moveFirst.code = 100;
        moveFirst.phase = 1;
        moveFirst.uid = uidR1;
        game.move(moveFirst);
        assertEquals(game.getMove(uidR1).code, 100);

        GameInterface.Move moveSecond = new GameInterface.Move();
        moveSecond.code = 101;
        moveSecond.phase = 1;
        moveSecond.uid = uidR2;
        game.move(moveSecond);
        assertEquals(game.getMove(uidR2).code, 101);

        //second turn 

        //then
        GameInterface.Move moveFirstSecondTurn = new GameInterface.Move();
        moveFirstSecondTurn.code = 101;
        moveFirstSecondTurn.phase = 2;
        moveFirstSecondTurn.uid = uidR1;
        game.move(moveFirstSecondTurn);
        assertEquals(game.getMove(uidR1).code, 101);

        GameInterface.Move moveSecondSecondTurn = new GameInterface.Move();
        moveSecondSecondTurn.code = 102;
        moveSecondSecondTurn.phase = 2;
        moveSecondSecondTurn.uid = uidR2;
        game.move(moveSecondSecondTurn);
        assertEquals(game.getMove(uidR2).code, 102);

        GameInterface.Move moveThatWillThrow = new GameInterface.Move();
        moveThatWillThrow.code = 101;
        moveThatWillThrow.phase = 2;
        moveThatWillThrow.uid = uidR1;
        game.move(moveThatWillThrow);
        assertEquals(game.getMove(uidR1).code, 101);
    }



    @Test
    public void shouldGetPhaseForCurrentGame() throws RemoteException {
        //given
        BrokenGame game = new BrokenGame();
        //when
        long uidR1 = game.register();
        long uidR2 = game.register();
        assertEquals(game.getPhase(uidR2), 1);
        
        //then
        GameInterface.Move moveFirst = new GameInterface.Move();
        moveFirst.code = 100;
        moveFirst.phase = 1;
        moveFirst.uid = uidR1;
        game.move(moveFirst);
        assertEquals(game.getMove(uidR1).code, 100);

        GameInterface.Move moveSecond = new GameInterface.Move();
        moveSecond.code = 101;
        moveSecond.phase = 1;
        moveSecond.uid = uidR2;
        game.move(moveSecond);
        assertEquals(game.getMove(uidR2).code, 101);
        
        //second turn 

        //then
        GameInterface.Move moveFirstSecondTurn = new GameInterface.Move();
        moveFirstSecondTurn.code = 101;
        moveFirstSecondTurn.phase = 2;
        moveFirstSecondTurn.uid = uidR1;
        game.move(moveFirstSecondTurn);
        assertEquals(game.getMove(uidR1).code, 101);
        
        
        GameInterface.Move moveSecondSecondTurn = new GameInterface.Move();
        moveSecondSecondTurn.code = 102;
        moveSecondSecondTurn.phase = 2;
        moveSecondSecondTurn.uid = uidR2;
        game.move(moveSecondSecondTurn);
        assertEquals(game.getMove(uidR2).code, 102);
        assertEquals(game.getPhase(uidR2), 2);
        
        
        GameInterface.Move nextPhaseTurn = new GameInterface.Move();
        nextPhaseTurn.code = 202;
        nextPhaseTurn.phase = 3;
        nextPhaseTurn.uid = uidR2;
        game.move(nextPhaseTurn);
        assertEquals(game.getMove(uidR2).code, 202);
        assertEquals(game.getPhase(uidR2), 3);
        
    }

    @Test
    public void getButLast() throws RemoteException {
        int uidR2 = 1;
        
        GameInterface.Move mv1 = new GameInterface.Move();
        mv1.code = 202;
        mv1.phase = 3;
        mv1.uid = uidR2;

        GameInterface.Move mv2 = new GameInterface.Move();
        mv2.code = 202;
        mv2.phase = 3;
        mv2.uid = 2;
        
        ArrayList<GameInterface.Move> moves = new ArrayList<>();
        moves.add(mv1);
        moves.add(mv2);
        GameInterface.Move lastWithoutUserId = new BrokenGame().getLastWithoutUserId(moves, moves, uidR2);
        assertEquals(lastWithoutUserId.uid, 2);
        assertEquals(moves.size(), 1);

    }

    @Test
    public void getButLastSecond() throws RemoteException {
        int uidR2 = 1;

        GameInterface.Move mv1 = new GameInterface.Move();
        mv1.code = 202;
        mv1.phase = 3;
        mv1.uid = uidR2;


        ArrayList<GameInterface.Move> moves = new ArrayList<>();
        moves.add(mv1);
        GameInterface.Move lastWithoutUserId = new BrokenGame().getLastWithoutUserId(moves, moves, uidR2);
        assertEquals(lastWithoutUserId, null);
        assertEquals(moves.size(), 1);

    }

    
}