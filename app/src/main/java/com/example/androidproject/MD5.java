package com.example.androidproject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String md5(String pass) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte b: result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if(hex.length() == 1) {
                    sb.append("0").append(hex);
                } else {
                    sb.append(hex);
                }
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return "";
    }
}
