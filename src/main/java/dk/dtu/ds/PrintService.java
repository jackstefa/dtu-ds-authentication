package dk.dtu.ds;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PrintService extends Remote {

    /**
     * Returns a string that is prefixed with "From server: ".
     *
     * @param input The input string to be echoed.
     * @return The echoed string with the server prefix.
     * @throws RemoteException If a remote communication error occurs.
     */
    public String echo(String input) throws RemoteException;

    public void print(String filename, String printer) throws RemoteException;

    public List<String> queue(String printer) throws RemoteException;

    public void topQueue(String printer, int job) throws RemoteException;

    public void start() throws RemoteException;

    public void stop() throws RemoteException;

    public void restart() throws RemoteException;

    public String status(String printer) throws RemoteException;

    public String readConfig(String parameter) throws RemoteException;

    public void setConfig(String parameter, String value) throws RemoteException;



}
