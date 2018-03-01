/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
package com.philips.cdp.security;

import android.content.Context;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;

import com.janrain.android.Jump;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.securekey.SKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecureStorage {

    private static String TAG = SecureStorage.class.getSimpleName();

    private static byte[] secretKey;

    //meant to migrate unencrypted data to encrypted one
    public static void migrateUserData(Context context, final String pFileName) {

        try {
            //Read from file
            final FileInputStream fis = context.openFileInput(pFileName);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            final Object object = ois.readObject();
            byte[] plainBytes = null;
            if (object instanceof byte[]) {
                plainBytes = (byte[]) object;
            } else {
                Log.e(TAG, "plainBytes= " + Arrays.toString(plainBytes));
            }

            context.deleteFile(pFileName);
            fis.close();
            ois.close();
            if (plainBytes != null) {
                Jump.getSecureStorageInterface().storeValueForKey(pFileName, new String(plainBytes),
                        new SecureStorageInterface.SecureStorageError());
            } else {
                Log.e(TAG, "plainBytes is coming null");
            }
        } catch (ClassNotFoundException | IOException e) {
            Log.e(TAG, "migrateUserData Exception = " + e.getMessage());
        }


    }

    public static String objectToString(Serializable obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Base64OutputStream base64OutputStream = new Base64OutputStream(baos, Base64.NO_PADDING
                | Base64.NO_WRAP); ObjectOutputStream oos = new ObjectOutputStream(base64OutputStream)) {
            oos.writeObject(obj);
            return baos.toString("UTF-8");
        } catch (IOException e) {
            Log.e(TAG, "objectToString Exception = " + e.getMessage());
        }
        return null;
    }


    public static Object stringToObject(String str) {
        try {
            final Base64InputStream base64InputStream = new Base64InputStream(
                    new ByteArrayInputStream(str.getBytes()), Base64.NO_PADDING
                    | Base64.NO_WRAP);
            final ObjectInputStream objectInputStream = new ObjectInputStream(base64InputStream);
            objectInputStream.close();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Log.e(TAG, "stringToObject Exception = " + e.getMessage());
        }
        return null;
    }


    public static byte[] decrypt(byte[] encByte) {
        final String AES = "AES";
        secretKey = SKey.generateSecretKey();
        try {
            final Key key = new SecretKeySpec(secretKey, AES);
            final Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encByte);
        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            Log.e(TAG, "decrypt Exception = " + e.getMessage());
        }
        return new byte[0];
    }

    public static void generateSecretKey() {
        if (secretKey == null) {
            secretKey = SKey.generateSecretKey();
        }
    }

}
