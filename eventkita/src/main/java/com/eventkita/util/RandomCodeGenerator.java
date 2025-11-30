package com.eventkita.util;

import java.util.UUID;
import java.security.SecureRandom;

public class RandomCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    // Generate UUID string (untuk QR code / referenceId)
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    // Generate kode random custom panjang tertentu
    public static String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

}
