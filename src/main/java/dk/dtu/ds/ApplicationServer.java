package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicationServer {

    /**
     * Creates and binds the registry to the specified port and binds services to it.
     */
    public static void main(String[] args) throws RemoteException {
        // Creates a registry on the specified port
        Registry registry = LocateRegistry.createRegistry(Config.SERVER_PORT);

        // Iterates over the server bindings defined in the configuration
        for (String serverBind : Config.SERVER_BINDS) {
            // Prints the binding information to the console
            System.out.println("Binding " + serverBind + " added to the registry!");

            // Binds the HelloServant instance to the registry with the specified server binding name
            registry.rebind(serverBind, new HelloServant());
        }
    }
}