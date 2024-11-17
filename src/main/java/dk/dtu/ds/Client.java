package dk.dtu.ds;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private static AuthenticationService authService;

    private static final Scanner scanner = new Scanner(System.in);

    private static String token;

    public static void main(String[] args) {
        try {
            authService = (AuthenticationService) Naming.lookup(Config.AUTH_ENDPOINT);
            //authService.register("admin", "password");
            //authService.register("amir", "iran");
            //authService.register("fede", "italy_florence");
            //authService.register("nathan", "france");
            //authService.register("jack", "italy_milan");
            token = getToken();
            //System.out.println("token: " + token);
            if (token != null){
                executeUserRequests();
            } else {
                System.out.println("Token is null, login failed");
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
                System.out.println("Login failed: " + e.getMessage());
            }
        }
    }

    public static void executeUserRequests() throws MalformedURLException, NotBoundException, InterruptedException, RemoteException {
        PrintService printService = (PrintService) Naming.lookup(Config.PRINT_ENDPOINT);
        while (true) {
            System.out.println("Enter command: ");
            String command = scanner.nextLine();
            if (command.equals("exit")) {
                return;
            }
            if (command.equals("print")) {
                try {
                    System.out.println(printService.print(token, "file", "printer"));
                } catch (RemoteException e) {
                    System.out.println("Print failed " + e.getMessage());
                }
            }
            if (command.equals("queue")) {
                try {
                    printService.queue(token, "printer").forEach(System.out::println);
                } catch (RemoteException e) {
                    System.out.println("Queue failed " + e.getMessage());
                }
            }
            if (command.equals("topQueue")) {
                try {
                    System.out.println(printService.topQueue(token, "printer", 1));
                } catch (RemoteException e) {
                    System.out.println("TopQueue failed " + e.getMessage());
                }
            }

            if (command.equals("start")) {
                try {
                    System.out.println(printService.start(token));
                } catch (RemoteException e) {
                    System.out.println("Start failed " + e.getMessage());
                }
            }
            if (command.equals("stop")) {
                try {
                    System.out.println(printService.stop(token));
                } catch (RemoteException e) {
                    System.out.println("Stop failed " + e.getMessage());
                }
            }
            if (command.equals("restart")) {
                try {
                    System.out.println(printService.restart(token));
                } catch (RemoteException e) {
                    System.out.println("Restart failed " + e.getMessage());
                }
            }
            if (command.equals("status")) {
                try {
                    System.out.println(printService.status(token, "printer"));
                } catch (RemoteException e) {
                    System.out.println("Status failed " + e.getMessage());
                }
            }
            if (command.equals("readConfig")) {
                try {
                    System.out.println(printService.readConfig(token, "parameter"));
                } catch (RemoteException e) {
                    System.out.println("ReadConfig failed " + e.getMessage());
                }
            }
            if (command.equals("setConfig")) {
                try {
                    System.out.println(printService.setConfig(token, "parameter", "value"));
                } catch (RemoteException e) {
                    System.out.println("SetConfig failed " + e.getMessage());
                }
            }
        }
    }
}
