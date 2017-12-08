/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import static java.lang.System.arraycopy;
import static java.util.Arrays.copyOfRange;


public class ByteUtil {

    static final int MIN = 101;
    static final int MAX = Integer.MAX_VALUE;
    static final int RANDOM_BYTE_ARR_SIZE = 2;

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    final static String PVALUE = "B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C6"
            + "9A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C0"
            + "13ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD70"
            + "98488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0"
            + "A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708"
            + "DF1FB2BC2E4A4371";

    final static String GVALUE = "A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507F"
            + "D6406CFF14266D31266FEA1E5C41564B777E690F5504F213"
            + "160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1"
            + "909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28A"
            + "D662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24"
            + "855E6EEB22B3B2E5";

    static String encodeToBase64(byte[] data) {
        if (data == null || data.length <= 0) {
            return null;
        }

        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    static byte[] decodeFromBase64(String data) {
        if (data == null) {
            return null;
        }

        return Base64.decode(data.getBytes(Charset.defaultCharset()), Base64.DEFAULT);
    }

    static byte[] hexToBytes(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    static String bytesToCapitalizedHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String generateRandomNum() {
        return String.valueOf(new SecureRandom().nextInt(MAX - MIN) + 1 + MIN);
    }

    private static byte[] getRandomByteArray(int size) {
        byte[] result = new byte[size];

        new SecureRandom().nextBytes(result);

        return result;
    }

    static byte[] addRandomBytes(byte[] data) {
        if (data == null) {
            return null;
        }

        byte[] randomBytes = getRandomByteArray(RANDOM_BYTE_ARR_SIZE);

        int dataLength = data.length;

        byte[] dataBytes = new byte[dataLength + RANDOM_BYTE_ARR_SIZE];

        arraycopy(randomBytes, 0, dataBytes, 0, RANDOM_BYTE_ARR_SIZE);
        arraycopy(data, 0, dataBytes, RANDOM_BYTE_ARR_SIZE, dataLength);

        return dataBytes;
    }

    static byte[] removeRandomBytes(byte[] data) {
        if (data == null || data.length < 3) {
            return data;
        }

        return copyOfRange(data, RANDOM_BYTE_ARR_SIZE, data.length);
    }
}
