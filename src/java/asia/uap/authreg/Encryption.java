/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asia.uap.authreg;

/**
 *
 * @author blonyagoncillo
 */
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    // Encrypt password using SHA-256
    public String encrypt(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Convert password string to bytes and hash it
            byte[] hashBytes = digest.digest(password.getBytes());
            
            // Convert hash bytes to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-256 algorithm not available", e);
        }
    }
}


