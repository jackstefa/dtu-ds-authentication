package dk.dtu.ds;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.NoSuchAlgorithmException;

public class ApplicationServer {

    /**
     * Creates and binds the registry to the specified port and binds services to it.
     */
    public static void main(String[] args) throws RemoteException {
        // Creates a registry on the specified port
        Registry registry = LocateRegistry.createRegistry(Config.SERVER_PORT);

        registry.rebind(Config.PRINT_BIND, new PrintServant());

        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();
        registry.rebind(Config.AUTH_BIND, new AuthenticationServant(pair.getPrivate(), pair.getPublic()));

        System.out.println("Server ready!");
        for (String reg: registry.list()) {
            System.out.println("List of bindings " + reg);
        }
    }
}

/*
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.interfaces.RSAPublicKey;

private static RSAPublicKey loadPublicKey(String filePath) {
    try {
        String key = new String(Files.readAllBytes(Paths.get(filePath)))
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    } catch (Exception e) {
        throw new RuntimeException("Failed to load public key", e);
    }
}*/