package prir.prir3.server;

import prir.prir3.Game;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by tomasz.lelek on 21/05/15.
 */
public class RmiStarter extends UnicastRemoteObject{

    public RmiStarter() throws RemoteException {
            super();
        }
    
    public static final String REGISTRY_NAME =
            "GAME";

    public static void main(String[] args) throws Exception {
        int registryPortNumber = 1099;

        // Start RMI registry
        LocateRegistry.createRegistry(registryPortNumber);

        Naming.rebind(REGISTRY_NAME, new Game());
        System.out.println("Server running...");
        Thread.sleep(1000000000);
    }
}
