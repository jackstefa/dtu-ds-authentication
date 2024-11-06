package dk.dtu.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public interface AuthenticationService extends UnicastRemoteObject extends Remote {

    private static HASH_SECRET;

    public String login(String username, String password) throws RemoteException;

    private boolean isInTheDatabase(String username, String password) throws RemoteException;
}
