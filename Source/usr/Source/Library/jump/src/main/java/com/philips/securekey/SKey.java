/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
package com.philips.securekey;

import android.provider.Settings;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SKey {
    private static char[] key;
    private static byte[] secretKey;

    private static void storeSecretKey() {
        key = "jlapp7jokj4ngiafcrbna8nutu".toCharArray(); // Since we don't have
        // a way to store key in file unlike iOS who have keychain, the
        // key will be saved as constant in this file and proguard obfuscated.
        try {
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec ks = new PBEKeySpec(key, Settings.Secure.ANDROID_ID.getBytes(), 1024, 128);
            SecretKey s = f.generateSecret(ks);
            secretKey = s.getEncoded();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static byte[] generateSecretKey() {
        if (secretKey == null) {
            storeSecretKey();
        }
        return secretKey;

    }

}
