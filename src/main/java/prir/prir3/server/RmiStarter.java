package prir.prir3.server;

import prir.prir3.BrokenGame;
import prir.prir3.Game;
import prir.prir3.GameInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by tomasz.lelek on 21/05/15.
 */
public class RmiStarter {//extends UnicastRemoteObject{

    public RmiStarter() throws RemoteException {
            super();
        }
    
    public static final String REGISTRY_NAME =
            "GAME";

    public static void main(String[] args) throws Exception {
        int registryPortNumber = 1099;

        // Start RMI registry
        Registry registry = LocateRegistry.createRegistry(registryPortNumber);

        GameInterface rmiService = (GameInterface) UnicastRemoteObject
                .exportObject(new Game(), 0);
        
        registry.rebind(REGISTRY_NAME, rmiService);
        System.out.println("Server running...");
        Thread.sleep(1000000000);
    }
}
