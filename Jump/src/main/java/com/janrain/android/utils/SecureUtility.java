package com.janrain.android.utils;

import android.provider.Settings;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SecureUtility {

    private static byte[] SECRET_KEY;

    static{
        generateSecretKey();
    }

    public static byte[] encrypt(String text) {
        try {
            Key key = (Key) new SecretKeySpec(SECRET_KEY, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encText = cipher.doFinal(text.getBytes());
            return encText;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String objectToString(Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
        }
        return null;
    }

    public static Object stringToObject(String str) {
        try {
            return new ObjectInputStream(new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP)).readObject();
        } catch (Exception e) {
        }
        return null;
    }


    public static byte[] decrypt(byte[] encByte) {
        try {
            Key key = (Key) new SecretKeySpec(SECRET_KEY, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decText = cipher.doFinal(encByte);
            return decText;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static void generateSecretKey() {
        if (SECRET_KEY == null) {
            storeSecretKey();
        }
    }

    private static void storeSecretKey() {
        final byte[] salt = Settings.Secure.ANDROID_ID.getBytes();
        final char[] key = "jlapp7jokj4ngiafcrbna8nutu".toCharArray();
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(key, salt, 1024, 128);
            SecretKey s = f.generateSecret(ks);
            SECRET_KEY = s.getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
