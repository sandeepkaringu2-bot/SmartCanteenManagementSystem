package com.smartcanteen.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

// Demo project password hashing. For a production system, use a salted adaptive hash such as BCrypt/Argon2.
public final class PasswordUtil {
    private PasswordUtil() { }
    public static String hash(String password) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException("SHA-256 is unavailable", e); }
    }
    public static boolean matches(String password, String hash) { return hash(password).equalsIgnoreCase(hash); }
}
