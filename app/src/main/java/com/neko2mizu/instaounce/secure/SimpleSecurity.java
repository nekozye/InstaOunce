package com.neko2mizu.instaounce.secure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SimpleSecurity {

    //Note, this is just proof of concept in username based saltified MD5.
    //Not actualy secure

    private static String getMD5Hash(String hashing_string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        md.update(hashing_string.getBytes());

        byte[] bytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        String generatedHash = sb.toString();
        return generatedHash;
    }

    public static String getSaltFromUsername(String username)
    {
        String hashed = getMD5Hash(username);
        return hashed.substring(0,16);
    }

    public static String getPasswordHash(String username, String password)
    {
        String hashedSalt = getSaltFromUsername(username);
        String salt_added_pw = hashedSalt+password;
        String hashed_salted_pw = getMD5Hash(salt_added_pw);
        return hashed_salted_pw;
    }




}
