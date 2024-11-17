package dk.dtu.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthenticationService extends Remote {
    String login(String username, String password) throws RemoteException;
    boolean isTokenValid(String token) throws RemoteException;
    void register(String username, String password) throws RemoteException;
    String getUsernameFromToken(String token) throws RemoteException;
}
