package dk.dtu.ds;

import java.rmi.RemoteException;

public interface AuthorizationService {
    boolean hasPermission(String token, String action) throws RemoteException;
}
