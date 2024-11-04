package dk.dtu.ds;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {

        for (String serverBind: Config.SERVER_BINDS) {
            HelloService service = (HelloService) Naming.lookup("rmi://localhost:" + Config.SERVER_PORT +"/" + serverBind);
            System.out.println("--- " + service.echo("Hey Server!"));
        }

    }

}
