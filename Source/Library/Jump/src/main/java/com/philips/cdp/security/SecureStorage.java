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

import com.janrain.android.Jump;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.securekey.SKey;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SecureStorage {

    public static final String AES = "AES";
    private static Context mContext;

    public static void init(Context pContext) {
        mContext = pContext;
    }

    private static byte[] secretKey;

     //meant to migrate unencrypted data to encrypted one
    public static void migrateUserData(final String pFileName) {

        try {
            //Read from file
            final FileInputStream fis = mContext.openFileInput(pFileName);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            final Object object = ois.readObject();
            byte[] plainBytes = null;
            if (object instanceof byte[]) {
                plainBytes = (byte[]) object;
            }
            mContext.deleteFile(pFileName);
            fis.close();
            ois.close();
            Jump.getSecureStorageInterface().storeValueForKey(pFileName,new String(plainBytes),
                    new SecureStorageInterface.SecureStorageError());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }


    }

    public static String objectToString(Serializable obj) {
        try {
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(
                    new Base64OutputStream(baos, Base64.NO_PADDING
                            | Base64.NO_WRAP));
            oos.writeObject(obj);
            oos.close();
            return baos.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
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
        secretKey = SKey.generateSecretKey();
        try {
            final Key key = (Key) new SecretKeySpec(secretKey, AES);
            final Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(encByte);

        } catch (NoSuchAlgorithmException | InvalidKeyException |
                NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateSecretKey() {
        if (secretKey == null) {
            //storeSecretKey();
            secretKey = SKey.generateSecretKey();
        }
    }

}
