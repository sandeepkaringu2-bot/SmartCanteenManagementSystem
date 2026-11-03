package com.smartcanteen.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Demo project password hashing. 
 * For a production system, use a salted adaptive hash such as BCrypt/Argon2.
 */
public final class PasswordUtil {
    
    private PasswordUtil() {
        // Prevent instantiation of utility class
    }
    
    public static String hash(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable", e);
        }
    }
    
    public static boolean matches(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        return hash(password).equalsIgnoreCase(hash);
    }
}
