package com.philips.cdp.security;

import android.content.Context;
import android.provider.Settings;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.philips.securekey.SKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class SecureStorage {

    private static Context mContext;

    public static void init(Context pContext){
        mContext = pContext;
    }

    private static byte[] SECRET_KEY;

    /*public static byte[] encrypt(String text) {
      //  storeSecretKey();
        SECRET_KEY = SKey.generateSecretKey();
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
    }*/

    //migrates data encrypted with an old key to new key
    public static void migrateDataFromOldToNewKey(final String pFileName){
        //get Data with old key from the file
      /*  FileInputStream fis = null;
        Serializable serializableObject = null;
        Object object;
        boolean isSerialized = false;

        final byte[] salt = Settings.Secure.ANDROID_ID.getBytes();
        oldKey = "jlapp7jokj4ngiafcrbna8nutu".toCharArray();
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(oldKey, salt, 1024, 128);
            SecretKey s = f.generateSecret(ks);
            SECRET_KEY = s.getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        try {
            fis = mContext.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] encryptTextBytes = (byte[]) ois.readObject();
            byte[] decryptBytes = decrypt(encryptTextBytes);
            String plainText = new String(decryptBytes);
            if(stringToObject(new String(decryptBytes)) instanceof Serializable){
                serializableObject = (Serializable) stringToObject(new String(decryptBytes));
                isSerialized = true;
            }
            fis.close();
            ois.close();
            mContext.deleteFile(pFileName);

            //Generate New Key and Encrypt data
            refreshSecretKey("feakl9joke4ngicfcrbag8hute"); //This new key is just a dummy key. This will be replaced with actual key and then data migration from old key to new will happen
            key = "feakl9joke4ngicfcrbag8hute".toCharArray();// Since we don't have a way to store key in file, the new key will be saved as constant in this file and proguard obfuscated.
            FileOutputStream fos = mContext.openFileOutput(pFileName,0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            if(isSerialized) {
                oos.writeObject(encrypt(objectToString(serializableObject)));

            }else {

                byte[] encryptedData = encrypt(plainText);
                oos.writeObject(encryptedData);
            }
            fos.close();
            oos.close();
           oldKey =  "feakl9joke4ngicfcrbag8hute".toCharArray();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
    }




    //meant to migrate unencrypted data to encrypted one
    public static void migrateUserData(final String pFileName){

       /* try {
            //Read from file
            FileInputStream fis = mContext.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            String plainTextString = null;
            byte[] plainBytes = null;
           *//* if(object instanceof String) {

                plainTextString = (String) object;
            }*//*

            if(object instanceof byte[]){
                plainBytes = (byte[])object;
            }


            mContext.deleteFile(pFileName);
            fis.close();
            ois.close();

            //Encrypt the contents of file
            FileOutputStream fos = mContext.openFileOutput(pFileName, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            byte[] ectext = null;
            if(plainTextString != null) {
                ectext = SecureStorage.encrypt(plainTextString);
            }
            if(plainBytes != null){
                ectext = SecureStorage.encrypt(new String(plainBytes));
                byte[] decr = SecureStorage.decrypt(ectext);
            }

            oos.writeObject(ectext);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/



    }



    private static void refreshSecretKey(final String pKey){
        key = pKey.toCharArray();
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(key, Settings.Secure.ANDROID_ID.getBytes(), 1024, 128);
            SecretKey s = f.generateSecret(ks);
            SECRET_KEY = s.getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
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
        } catch (Exception e) {
        }
        return null;
    }


   /* public static byte[] decrypt(byte[] encByte) {
        //storeSecretKey();
        SECRET_KEY = SKey.generateSecretKey();
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
    }*/

    public static void generateSecretKey() {
        if (SECRET_KEY == null) {
            //storeSecretKey();
            SECRET_KEY = SKey.generateSecretKey();
        }
    }

    private static void refreshKey(){
        byte[] randomByte = new byte[16]; //Means 128 bit
        Random random = new SecureRandom();
        random.nextBytes(randomByte);
        String key = new String(randomByte);
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            byte[] sek = key.getBytes();
            SecretKey sk = new SecretKeySpec(sek, 0, sek.length, "AES");
            ks.setKeyEntry("keyAlias", sk, "jlapp7jokj4ngiafcrbna8nutu".toCharArray(), new Certificate[0]);
            OutputStream writeStream = mContext.openFileOutput("keys", 0);
            ks.store(writeStream, "jlapp7jokj4ngiafcrbna8nutu".toCharArray());
            writeStream.close();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //To be discussed about below approach to stire key in a keystore file
    private static char[] getStoredKey(){
        InputStream readStream = null;
        Key key = null;
        String keyString = null;
        char[] keyCharArray = null;
        try {
            readStream = mContext.openFileInput("keys");
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(readStream, "jlapp7jokj4ngiafcrbna8nutu".toCharArray());
            key = ks.getKey("keyAlias", "jlapp7jokj4ngiafcrbna8nutu".toCharArray());
            if(key instanceof SecretKey){
                keyString = new String(((SecretKey) key).getEncoded());
            }
            readStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        if(keyString != null) {
            keyCharArray = keyString.toCharArray();
        }
        return keyCharArray;


    }



    private static char[] key;
    private static char[] oldKey;



    private static void storeSecretKey() {
        final byte[] salt = Settings.Secure.ANDROID_ID.getBytes();
        key = "jlapp7jokj4ngiafcrbna8nutu".toCharArray(); // Since we don't have a way to store key in file unlike iOS who have keychain, the  key will be saved as constant in this file and proguard obfuscated.
        oldKey = "jlapp7jokj4ngiafcrbna8nutu".toCharArray();// Since we don't have a way to store key in file unlike iOS who have keychain, the  key will be saved as constant in this file and proguard obfuscated.
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
