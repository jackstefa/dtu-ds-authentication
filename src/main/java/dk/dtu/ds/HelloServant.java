package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloServant extends UnicastRemoteObject implements HelloService {

    /**
     * Constructs a new HelloServant instance and exports it on an anonymous port.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    protected HelloServant() throws RemoteException {
        super();
    }

    /**
     * Returns a string that is prefixed with "From server: ".
     *
     * @param input The input string to be echoed.
     * @return The echoed string with the server prefix.
     * @throws RemoteException If a remote communication error occurs.
     */
    public String echo(String input) throws RemoteException {
        return "From server: " + input;
    }

}