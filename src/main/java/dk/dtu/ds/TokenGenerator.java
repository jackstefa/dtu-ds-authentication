package dk.dtu.ds;

import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {
    public static String generateToken(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[byteLength];
        random.nextBytes(tokenBytes);

        // Convert the random bytes to a Base64 string
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}