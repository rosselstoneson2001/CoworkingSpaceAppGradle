package com.example.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for handling password operations using BCrypt.
 * <p>
 * This class provides methods to hash plain text passwords and
 * to check if a given plain password matches a hashed password.
 * BCrypt is used for password hashing as it is a strong and slow hashing algorithm,
 * providing defense against brute-force and rainbow table attacks.
 * </p>
 */
public class PasswordUtils {

    /**
     * Hashes the given plain password using BCrypt.
     *
     * @param plainPassword the plain password to be hashed
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12)); // 12 is the work factor
    }

    /**
     * Hashes the given plain password using BCrypt.
     *
     * @param plainPassword the plain password to be hashed
     * @return the hashed password
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) { // Method to check if the plain password matches the hashed password
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
