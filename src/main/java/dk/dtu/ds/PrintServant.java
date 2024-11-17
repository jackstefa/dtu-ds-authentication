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
    public String print(String token, String filename, String printer) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token: " + token);
        }

        String result = "Printing " + filename + " on " + printer;

        System.out.println(result);

        return result;
    }

    @Override
    public List<String> queue(String token, String printer) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }

        return List.of("Job 1", "Job 2", "Job 3");
    }

    @Override
    public String topQueue(String token, String printer, int job) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        String result = "Moving job " + job + " to the top of the queue";

        System.out.println(result);

        return result;
    }

    @Override
    public String start(String token) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        String result = "Starting print service";

        System.out.println(result);

        return result;
    }

    @Override
    public String stop(String token) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        String result = "Stopping print service";

        System.out.println(result);

        return result;
    }

    @Override
    public String restart(String token) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        String result = "Restarting print service";

        System.out.println(result);

        return result;
    }

    @Override
    public String status(String token, String printer) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        System.out.println("Checking status of " + printer);

        return "Printer " + printer + " is ready";
    }

    @Override
    public String readConfig(String token, String parameter) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        String result = "Reading " + parameter;

        System.out.println(result);

        return result;
    }

    @Override
    public String setConfig(String token, String parameter, String value) throws RemoteException {
        if (!authService.isTokenValid(token)) {
            throw new RemoteException("Invalid token");
        }
        String result = "Setting " + parameter + " to " + value;

        System.out.println(result);

        return result;
    }
}