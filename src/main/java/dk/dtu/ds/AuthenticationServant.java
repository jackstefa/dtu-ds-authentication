package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthenticationServant extends UnicastRemoteObject implements AuthenticationService {

    private static final HASH_SECRET = "jsdbfiluebrfvoubeavobaekjfbvouebgvoueivbjb1234567824rbsdjkcb";

    protected AuthenticationServant() throws RemoteException {
            super();
    }

    @Override
    public String login(String username, String password) throws RemoteException {
        String hashPassword = ;
        if (isInTheDatabase(username, hashPassword)) {
            token = createSession(username, hashPassword)
            return token;
        } else {
            System.out.println("Invalid password or username");
            throw new RemoteException("Invalid username or password");
        }
    }

    private boolean isInTheDatabase(String username, String password) throws RemoteException {
        return true;
    }
}
