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

        AuthenticationService authService = (AuthenticationService) Naming.lookup(Config.AUTHENTICATION_ENDPOINT);

        String token;

        try {
            token = authService.login("username", "password");
        } catch (RemoteException e) {
            System.out.println("Login failed");
            return;
        }

        PrintService printService = (PrintService) Naming.lookup(Config.PRINT_ENDPOINT);

        printService.print(token, "file", "printer");

    }

}
