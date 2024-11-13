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