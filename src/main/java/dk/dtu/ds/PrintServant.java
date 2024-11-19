package dk.dtu.ds;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PrintServant extends UnicastRemoteObject implements PrintService {
    private final AuthenticationService authentication;

    private final AuthorizationService authorization;

    public PrintServant(AuthenticationService authentication, AuthorizationService authorization) throws RemoteException {
        super();
        this.authentication = authentication;
        this.authorization = authorization;
    }

    private void authenticateAndAuthorize(String token, String action) throws RemoteException {
        if (!authentication.isTokenValid(token)) {
            throw new RemoteException("Invalid token: " + token);
        }

        if (!authorization.hasPermission(token, action)) {
            System.out.println("Not enough permission");
            throw new RemoteException("Not enough permission");
        }
    }

    public String echo(String input) throws RemoteException {
        return "From server: " + input;
    }

    @Override
    public String print(String token, String filename, String printer) throws RemoteException {
        authenticateAndAuthorize(token, "print");

        String result = "Printing " + filename + " on " + printer;

        System.out.println(result  + " at " + new java.util.Date());

        return result;
    }

    @Override
    public List<String> queue(String token, String printer) throws RemoteException {
        authenticateAndAuthorize(token, "queue");

        System.out.println("Returning queue at " + new java.util.Date());

        return List.of("Job 1", "Job 2", "Job 3");
    }

    @Override
    public String topQueue(String token, String printer, int job) throws RemoteException {
        authenticateAndAuthorize(token, "topQueue");

        String result = "Moving job " + job + " to the top of the queue";

        System.out.println(result  + " at " + new java.util.Date());

        return result;
    }

    @Override
    public String start(String token) throws RemoteException {
        authenticateAndAuthorize(token, "start");

        String result = "Starting print service";

        System.out.println(result  + " at " + new java.util.Date());

        return result;
    }

    @Override
    public String stop(String token) throws RemoteException {
        authenticateAndAuthorize(token, "stop");

        String result = "Stopping print service";
        
        System.out.println(result  + " at " + new java.util.Date());
        
        return result;
    }

    @Override
    public String restart(String token) throws RemoteException {
        authenticateAndAuthorize(token, "restart");

        String result = "Restarting print service"; 
        
        System.out.println(result  + " at " + new java.util.Date());
        
        return result;
    }

    @Override
    public String status(String token, String printer) throws RemoteException {
        authenticateAndAuthorize(token, "status");

        System.out.println("Checking status of " + printer  + " at " + new java.util.Date());

        return "Printer " + printer + " is ready";
    }

    @Override
    public String readConfig(String token, String parameter) throws RemoteException {
        authenticateAndAuthorize(token, "readConfig");

        String result = "Reading " + parameter;

        System.out.println(result  + " at " + new java.util.Date());

        return result;
    }

    @Override
    public String setConfig(String token, String parameter, String value) throws RemoteException {
        authenticateAndAuthorize(token, "setConfig");

        String result = "Setting " + parameter + " to " + value;

        System.out.println(result  + " at " + new java.util.Date());

        return result;
    }
}