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

public class AuthenticationServant extends Remote implements AuthenticationService {

    private record User(String username, String passwordSalt, String passwordHash) { }

    private final RSAPrivateKey rsaPrivateKey;

    private final RSAPublicKey rsaPublicKey;

    private final HashMap<String,String> tokens = new HashMap<>();

    protected AuthenticationServant(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) throws RemoteException {
        super();
        this.rsaPrivateKey = rsaPrivateKey;
        this.rsaPublicKey = rsaPublicKey;
    }

    private User getUserFromDb(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("db.csv"))) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String username, String password) throws RemoteException {
        User user = getUserFromDb(username);
        if (Password.check(password, user.passwordHash).addSalt(user.passwordSalt).withArgon2()) {
            String token = createSession(username);
            boolean result = addToken(token);
            if (result) {
                return token;
            } else {
                throw new RemoteException("Token already present");
            }
        } else {
            System.out.println("Invalid password or username");
            throw new RemoteException("Invalid username or password");
        }
    }

    private boolean createSession(String username) throws  JWTCreationException {
        /*TokenGenerator tokenGenerator = new TokenGenerator();
        String token = TokenGenerator.generateToken(256);*/
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .sign(algorithm);
            tokens.put(username, token); // it is ok to store with the username as kay because without private_key we cannot decode it
            return true;
        } catch (JWTCreationException exception) {
            // Invalid Signing configuration / Couldn't convert Claims.
            System.out.println("Error creating token");
            return false;
        }
    }

    private boolean addToken(String username, String token) {
        if(isTokenPresent(token)) {
            return false;
        }
        tokens.put(username, token);
        return true;
    }

    private void removeToken(String tokenToRemove) {
        for (String key: tokens.keySet()){
            if (tokens.get(key).equals(tokenToRemove)) {
                tokens.remove(key);
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

    private boolean checkSingleTokenValidity(String token) throws JWTVerificationException {
        if (isTokenPresent(token)) {
            try {
                Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);
                JWTVerifier verifier = JWT.require(algorithm)
                        // specify any specific claim validations
                        .withIssuer("auth0")
                        // reusable verifier instance
                        .build();
                DecodedJWT decodedJWT = verifier.verify(token);
            } catch (JWTVerificationException exception){
                // Invalid signature/claims
            }
        }
    }

    private boolean checkTokensValidity() throws JWTVerificationException {
        for (String token: this.tokens.values()) {
            if (isTokenPresent(token)) {
                DecodedJWT decodedJWT;
                try {
                    Algorithm algorithm = Algorithm.RSA256(this.rsaPublicKey, rsaPrivateKey);
                    JWTVerifier verifier = JWT.require(algorithm)
                            // specify any specific claim validations
                            .withIssuer("auth0")
                            // reusable verifier instance
                            .build();
                    decodedJWT = verifier.verify(token);
                } catch (JWTVerificationException exception){
                    // Invalid signature/claims
                }
            }
        }
    }

}
