package dk.dtu.ds;

import com.password4j.Password;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.*;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Objects;
import java.security.SecureRandom;
import java.util.Base64;

public class AuthenticationServant extends UnicastRemoteObject implements AuthenticationService, Serializable {

    private record User(String username, String passwordSalt, String passwordHash) { }

    private final RSAPrivateKey rsaPrivateKey;

    private final RSAPublicKey rsaPublicKey;

    private final HashMap<String,String> tokens = new HashMap<>();

    protected AuthenticationServant(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) throws RemoteException {
        super();
        this.rsaPrivateKey = rsaPrivateKey;
        this.rsaPublicKey = rsaPublicKey;
    }

    private User getUserFromDb(String username) throws UserException {
        try (BufferedReader br = new BufferedReader(new FileReader(Config.DB_PATH))) {
            // we expect CSV file with the following format:
            // username, passwordSalt, passwordHash
            for (String line; (line = br.readLine()) != null; ) {
                String[] columns = line.split(",");
                // columns[0] is username
                // columns[1] is passwordSalt
                // columns[2] is passwordHash
                if (Objects.equals(columns[0], username)) {
                    return new User(username, columns[1].replaceAll(Config.DB_REPLACE_COMMA, ","), columns[2].replaceAll(Config.DB_REPLACE_COMMA, ","));
                }
            }
            throw new UserException(username + " not found");
        } catch (IOException e) {
            System.out.println(Config.DB_PATH + " not found");
        }
        return null;
    }

    @Override
    public String login(String username, String password) throws RemoteException, NullPointerException {
        try {
            User user = getUserFromDb(username);
            if (user != null && Password.check(password, user.passwordHash).addSalt(user.passwordSalt).withArgon2()) {
                System.out.println("\nUser " + username + " logged in at " + new java.util.Date());
                return createSession(username);
            } else {
                System.out.println("Invalid password or username");
                throw new RemoteException("Invalid password");
            }
        } catch (UserException e) {
            System.out.println("User " + username + " not found");
        } catch (NullPointerException e) {
            System.out.println("Invalid password or username");
        }
        return null;
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public void register(String username, String password) throws RemoteException {
        String salt = generateSalt();
        String hash = Password.hash(password).addSalt(salt).withArgon2().getResult();
        System.out.println("Registering " + username + " with salt " + salt + " and hash " + hash);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Config.DB_PATH, true))) {
            writer.write(username + "," + salt.replaceAll(",", Config.DB_REPLACE_COMMA) + "," + hash.replaceAll(",", Config.DB_REPLACE_COMMA) + "\n");
        } catch (IOException e1) {
            System.out.println("Error writing to " + Config.DB_PATH);
        }

    }

    private String createSession(String username) throws JWTCreationException {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + Config.TOKEN_EXPIRATION_TIME))
                    .sign(algorithm);
            boolean isTokenBeenAdded = addToken(username, token); // it is ok to store with the username as kay because without private_key we cannot decode it
            return isTokenBeenAdded ? token : null;
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
            System.out.println("Error creating token");
            return null;
        }
    }

    private boolean addToken(String username, String token) {
        // One user can only have one token, so one session open at a time
        if (isTokenPresent(token)) {
            System.out.println("Last token already present");
            return false;
        }
        tokens.put(username, token);
        return true;
    }

    private void removeToken(String tokenToRemove) {
        for (String user: tokens.keySet()){
            if (tokens.get(user).equals(tokenToRemove)) {
                removeTokenForUser(user);
            }
        }
    }

    private void removeTokenForUser(String user) {
        tokens.remove(user);
    }

    private boolean isTokenPresent(String tokenToCheck) {
        if (tokens.containsValue(tokenToCheck)) {
            for (String user : tokens.keySet()) {
                if (tokens.get(user).equals(tokenToCheck)) {
                    System.out.println("\nChecking token for user: " + user);
                    System.out.println("Token is present!");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isTokenValid(String token) throws JWTVerificationException {
        try {
            if (isTokenPresent(token)) {
                Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
                JWTVerifier verifier = JWT.require(algorithm)
                        // specify any specific claim validations
                        .withIssuer("auth0")
                        // reusable verifier instance
                        .build();
                DecodedJWT decodedJWT = verifier.verify(token);
                boolean isTokenValid = decodedJWT.getExpiresAt().after(new java.util.Date());
                if (!isTokenValid) {
                    removeToken(token);
                }
                return isTokenValid;
            } else {
                System.out.println("Token is not present!");
                return false;
            }
        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            return false;
        }
    }

}
