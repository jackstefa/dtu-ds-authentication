package dk.dtu.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote {

    /**
     * Returns a string that is prefixed with "From server: ".
     *
     * @param input The input string to be echoed.
     * @return The echoed string with the server prefix.
     * @throws RemoteException If a remote communication error occurs.
     */
    public String echo(String input) throws RemoteException;

}
