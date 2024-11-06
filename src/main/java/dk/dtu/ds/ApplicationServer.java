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

        registry.rebind(Config.PRINT_BIND, new PrintServant());

        registry.rebind(Config.AUTH_BIND, new AuthenticationServant());

        System.out.println("Server ready!");
        for (String reg: registry.list()) {
            System.out.println("List of bindings " + reg);
        }
    }
}