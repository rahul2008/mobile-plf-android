/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtil {

    public static byte[] aesEncryptData(String data, String keyStr) throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] longKey = new BigInteger(keyStr, 16).toByteArray();
        byte[] key;
        if (longKey[0] == 0) {
            key = Arrays.copyOfRange(longKey, 1, 17);
        } else {
            key = Arrays.copyOf(longKey, 16);
        }

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        c.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        byte[] dataBytes = ByteUtil.addRandomBytes(data.getBytes(Charset.defaultCharset()));

        return c.doFinal(dataBytes);
    }

    public static byte[] aesDecryptData(byte[] data, String keyStr) throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
        byte[] longKey = new BigInteger(keyStr, 16).toByteArray();
        byte[] key;
        if (longKey[0] == 0) {
            key = Arrays.copyOfRange(longKey, 1, 17);
        } else {
            key = Arrays.copyOf(longKey, 16);
        }

        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        byte[] ivBytes = new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        c.init(Cipher.DECRYPT_MODE, keySpec, iv);

        return c.doFinal(data);
    }

    public static String generateDiffieKey(String randomValue) {
        BigInteger p = new BigInteger(ByteUtil.PVALUE, 16);
        BigInteger g = new BigInteger(ByteUtil.GVALUE, 16);
        BigInteger r = new BigInteger(randomValue);
        return ByteUtil.bytesToCapitalizedHex(g.modPow(r, p).toByteArray());
    }

    public static String generateSecretKey(String hellmanKey, String randomValue) {
        BigInteger p = new BigInteger(ByteUtil.PVALUE, 16);
        BigInteger g = new BigInteger(hellmanKey, 16);
        BigInteger r = new BigInteger(randomValue);
        return ByteUtil.bytesToCapitalizedHex(g.modPow(r, p).toByteArray());
    }

    public static String extractEncryptionKey(String shellman, String skeyEnc, String randomValue) throws Exception {
        String secKey = generateSecretKey(shellman, randomValue);
        secKey = EncryptionUtil.getEvenNumberSecretKey(secKey);
        byte[] bytesEncKey = ByteUtil.hexToBytes(skeyEnc);
        byte[] bytesDecKey = aesDecryptData(bytesEncKey, secKey);

        String key = ByteUtil.bytesToCapitalizedHex(bytesDecKey);
        return key;
    }

    public static String getEvenNumberSecretKey(String secKey) {
        String tempKey = secKey;
        if (secKey != null) {
            int keyLength = secKey.length();
            if (keyLength % 2 == 0) {
                tempKey = secKey;
            } else {
                tempKey = "0" + secKey;
            }
        }
        return tempKey;
    }
}
