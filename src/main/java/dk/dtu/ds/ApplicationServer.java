package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class ApplicationServer {
    public static void main(String[] args) throws RemoteException, NoSuchAlgorithmException {
        // Creates a registry on the specified port
        Registry registry = LocateRegistry.createRegistry(Config.SERVER_PORT);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        var authenticationServant = new AuthenticationServant((RSAPrivateKey) pair.getPrivate(), (RSAPublicKey)pair.getPublic());
        registry.rebind(Config.AUTH_BIND, authenticationServant);

        var printServant = new PrintServant(authenticationServant);
        registry.rebind(Config.PRINT_BIND, printServant);

        System.out.println("Server ready!");
        for (String reg: registry.list()) {
            System.out.println("Binding: " + reg);
        }
    }
}