package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class PrintServant extends UnicastRemoteObject implements PrintService {

    /**
     * Constructs a new HelloServant instance and exports it on an anonymous port.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    protected PrintServant() throws RemoteException {
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

    @Override
    public void print(String filename, String printer) throws RemoteException {

    }

    @Override
    public List<String> queue(String printer) throws RemoteException {
        return List.of();
    }

    @Override
    public void topQueue(String printer, int job) throws RemoteException {

    }

    @Override
    public void start() throws RemoteException {

    }

    @Override
    public void stop() throws RemoteException {

    }

    @Override
    public void restart() throws RemoteException {

    }

    @Override
    public String status(String printer) throws RemoteException {
        return "";
    }

    @Override
    public String readConfig(String parameter) throws RemoteException {
        return "";
    }

    @Override
    public void setConfig(String parameter, String value) throws RemoteException {

    }

}