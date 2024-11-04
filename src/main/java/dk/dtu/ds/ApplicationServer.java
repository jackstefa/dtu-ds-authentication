package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ApplicationServer {

    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(Config.SERVER_PORT);
        for (String serverBind: Config.SERVER_BINDS) {
            System.out.println("Binding " + serverBind);
            registry.rebind(serverBind, new HelloServant());
        }
    }

}
