package prir.prir3.client;

import prir.prir3.Game;
import prir.prir3.GameInterface;
import prir.prir3.server.RmiStarter;

import javax.naming.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class GameClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException, NamingException {
        String host = "localhost";
        int portNumber = 1099;
        String lookupName = "//" + host + ":" + portNumber + "/" +
                RmiStarter.REGISTRY_NAME;
        
        Context namingContext = new InitialContext();
        NamingEnumeration<NameClassPair> list = namingContext.list("rmi://localhost/");
        while (list.hasMore()){
            NameClassPair next = list.next();
            System.out.println(next);
        }
        
        Game game = (Game)
                Naming.lookup(lookupName);
        long result = game.register();
        GameInterface.Move move = new GameInterface.Move();
        move.uid = result;
        move.phase = 1;
        game.move(move);
        System.out.println("player registered" + result);
    }
}

