package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PrintServant extends UnicastRemoteObject implements PrintService {
    private final AuthenticationService authService;

    public PrintServant(AuthenticationService authService) throws RemoteException {
        super();
        this.authService = authService;
    }

    public String echo(String input) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public void print(String token, String filename, String printer) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        System.out.println("Printing " + filename + " on " + printer);
    }

    @Override
    public List<String> queue(String token, String printer) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        return List.of("Job 1", "Job 2", "Job 3");
    }

    @Override
    public void topQueue(String token, String printer, int job) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        System.out.println("Moving job " + job + " to the top of the queue");
    }

    @Override
    public void start(String token) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        System.out.println("Starting print service");
    }

    @Override
    public void stop(String token) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        System.out.println("Stopping print service");
    }

    @Override
    public void restart(String token) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        System.out.println("Restarting print service");
    }

    @Override
    public String status(String token, String printer) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        return "Printer " + printer + " is ready";
    }

    @Override
    public String readConfig(String token, String parameter) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        return "Value of " + parameter;
    }

    @Override
    public void setConfig(String token, String parameter, String value) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        System.out.println("Setting " + parameter + " to " + value);
    }
}