package dk.dtu.ds;

import com.auth0.jwt.algorithms.Algorithm;
import com.password4j.Password;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Objects;

public class AuthenticationServant implements AuthenticationService, Remote {

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
                    return new User(username, columns[1], columns[2]);
                }
            }
            throw new UserException(username + " not found");
        } catch (IOException e) {
            System.out.println(Config.DB_PATH + " not found");
        }
        return null;
    }

    public String login(String username, String password) throws RemoteException {
        try {
            User user = getUserFromDb(username);
            if (Password.check(password, user.passwordHash).addSalt(user.passwordSalt).withArgon2()) {
                return createSession(username);
            } else {
                System.out.println("Invalid password or username");
                throw new RemoteException("Invalid password");
            }
        } catch (UserException e) {
            System.out.println("User " + username + " not found");
        }
        return null;
    }

    private String createSession(String username) throws  JWTCreationException {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + Config.TOKEN_EXPIRATION_TIME))
                    .sign(algorithm);
            addToken(username, token); // it is ok to store with the username as kay because without private_key we cannot decode it
            return token;
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
            System.out.println("Error creating token");
            return null;
        }
    }

    private boolean addToken(String username, String token) {
        if (isTokenPresent(token)) {
            return false;
        }
        tokens.put(username, token);
        return true;
    }

    private void removeToken(String tokenToRemove) {
        for (String user: tokens.keySet()){
            if (tokens.get(user).equals(tokenToRemove)) {
                tokens.remove(user);
            }
        }
    }

    private void removeTokenForUser(String user) {
        tokens.remove(user);
    }

    private boolean isTokenPresent(String tokenToCheck) {
        for (String key: tokens.keySet()){
            if (tokens.get(key).equals(tokenToCheck)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTokenValid(String token) throws JWTVerificationException {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    // specify any specific claim validations
                    .withIssuer("auth0")
                    // reusable verifier instance
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getExpiresAt().getTime() > System.currentTimeMillis();
        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            return false;
        }
    }

}
