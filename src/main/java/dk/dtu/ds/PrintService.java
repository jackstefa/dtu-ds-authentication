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

    public void print(String token, String filename, String printer) throws RemoteException;

    public List<String> queue(String token, String printer) throws RemoteException;

    public void topQueue(String token, String printer, int job) throws RemoteException;

    public void start(String token) throws RemoteException;

    public void stop(String token) throws RemoteException;

    public void restart(String token) throws RemoteException;

    public String status(String token, String printer) throws RemoteException;

    public String readConfig(String token, String parameter) throws RemoteException;

    public void setConfig(String token, String parameter, String value) throws RemoteException;



}
