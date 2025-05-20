package com.example.ebook_reader.util;

import java.security.MessageDigest;
import java.util.UUID;

public class AuthUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi băm mật khẩu", e);
        }
    }

    public static String generateUid() {
        return UUID.randomUUID().toString();
    }
}