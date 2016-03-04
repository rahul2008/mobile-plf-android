package com.philips.securekey;

import android.provider.Settings;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by 310202337 on 3/4/2016.
 */
public class SKey {
private static char[] key;
    private static char[] oldKey;
    private static byte[] SECRET_KEY;

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

    public static byte[] generateSecretKey(){
        if(SECRET_KEY == null){
             storeSecretKey();
        }
        return SECRET_KEY;

    }


}
