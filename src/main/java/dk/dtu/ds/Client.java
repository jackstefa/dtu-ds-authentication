package dk.dtu.ds;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Client {
    private static AuthenticationService authService;

    private static final Scanner scanner = new Scanner(System.in);

    private static String token;

    public static void main(String[] args) {
        try {
            authService = (AuthenticationService) Naming.lookup(Config.AUTH_ENDPOINT);
            token = getToken();
            if (token != null){
                System.out.println("Token: " + token);
                executeUserRequests();
            } else {
                System.out.println("Login failed");
            }
        } catch (InterruptedException e) {
            System.out.println("Execution failed1: " + e.getMessage());
        } catch (RemoteException e) {
            System.out.println("Lookup failed2: " + e.getMessage());
        } catch (NotBoundException e) {
            System.out.println("Lookup failed3: " + e.getMessage());
        } catch (MalformedURLException e) {
            System.out.println("Lookup failed4: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static String getToken() {
        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            try {
                return authService.login(username, password);
            } catch (RemoteException e) {
                System.out.println("Login failed");
            }
        }
    }

    public static void executeUserRequests() throws MalformedURLException, NotBoundException, InterruptedException, RemoteException {
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
            try {
                sleep(1000);    // sleep for 1 second, busy waiting
            } catch (InterruptedException e) {
                System.out.println("Sleep failed" + e.getMessage());
            }
        }
    }
}
