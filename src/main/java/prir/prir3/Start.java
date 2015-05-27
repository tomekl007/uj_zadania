package prir.prir3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Start {
	public static void main(String[] argv) throws Exception {
		GameInterface rmiService = (GameInterface) UnicastRemoteObject
				.exportObject(new Game(), 0);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("GAME", rmiService);
        Thread.sleep(100000);
	}
}
