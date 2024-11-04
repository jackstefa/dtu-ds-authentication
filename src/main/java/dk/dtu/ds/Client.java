package dk.dtu.ds;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class Client {

    /**
     * The main method that starts the client application.
     * It looks up remote services and invokes them.
     */
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        // Saving the services in a map
        Map<String, HelloService> serviceMap = new HashMap<>();
        for (String serverBind: Config.SERVER_BINDS) {
            // Look up the remote service and add it to the map
            HelloService service = (HelloService) Naming.lookup("rmi://localhost:" + Config.SERVER_PORT +"/" + serverBind);
            serviceMap.put(serverBind, service);
        }

        // Invoking the services saved in the map
        for (String serverBind: Config.SERVER_BINDS) {
            // Retrieve the service from the map and invoke its echo method
            HelloService service = serviceMap.get(serverBind);
            System.out.println("--- " + service.echo("Hey Server!"));
        }

    }

}
