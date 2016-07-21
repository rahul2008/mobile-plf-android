package com.philips.cdp.security;

import android.content.Context;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.philips.securekey.SKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class SecureStorage {

    public static final String AES = "AES";
    private static Context mContext;

    public static void init(Context pContext) {
        mContext = pContext;
    }

    private static byte[] SECRET_KEY;

    public static byte[] encrypt(String text) {
        //  storeSecretKey();
        SECRET_KEY = SKey.generateSecretKey();
        try {
            Key key = (Key) new SecretKeySpec(SECRET_KEY, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text.getBytes());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }


    //meant to migrate unencrypted data to encrypted one
    public static void migrateUserData(final String pFileName) {

        try {
            //Read from file
            FileInputStream fis = mContext.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            byte[] plainBytes = null;
            if (object instanceof byte[]) {
                plainBytes = (byte[]) object;
            }


            mContext.deleteFile(pFileName);
            fis.close();
            ois.close();

            //Encrypt the contents of file
            FileOutputStream fos = mContext.openFileOutput(pFileName, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            byte[] ectext = null;
            if (plainBytes != null) {
                ectext = SecureStorage.encrypt(new String(plainBytes));
                byte[] decr = SecureStorage.decrypt(ectext);
            }

            oos.writeObject(ectext);
            oos.close();
            fos.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }


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
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] decrypt(byte[] encByte) {
        //storeSecretKey();
        SECRET_KEY = SKey.generateSecretKey();
        try {
            Key key = (Key) new SecretKeySpec(SECRET_KEY, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encByte);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void generateSecretKey() {
        if (SECRET_KEY == null) {
            //storeSecretKey();
            SECRET_KEY = SKey.generateSecretKey();
        }
    }

}
