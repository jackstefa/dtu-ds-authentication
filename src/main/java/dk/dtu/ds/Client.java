package dk.dtu.ds;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static AuthenticationService authService;

    private static final Scanner scanner = new Scanner(System.in);

    private static String token;

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        authService = (AuthenticationService) Naming.lookup(Config.AUTHENTICATION_ENDPOINT);
        token = getToken();
        executeUserRequests();
    }

    public static String getToken() {
        while (true) {
            System.out.print("Enter username :");
            String username = scanner.nextLine();
            System.out.print("Enter password :");
            String password = scanner.nextLine();
            try {
                return authService.login(username, password);
            } catch (RemoteException e) {
                System.out.println("Login failed");
            }
        }
    }

    public static void executeUserRequests() throws MalformedURLException, NotBoundException, RemoteException {
        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                return;
            }
            if (command.equals("print")) {
                try {
                    PrintService printService = (PrintService) Naming.lookup(Config.PRINT_ENDPOINT);
                    printService.print(token, "file", "printer");
                } catch (RemoteException e) {
                    System.out.println("Print failed" + e.getMessage());
                }
            }
        }
    }
}
