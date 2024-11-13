package dk.dtu.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public interface AuthenticationService extends Remote {
    String login(String username, String password) throws RemoteException;
    boolean isTokenValid(String token) throws RemoteException;
}
