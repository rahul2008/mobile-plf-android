/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import android.annotation.SuppressLint;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class ByteUtilTest extends RobolectricTest {

    private final String data = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

    @Test
    public void testRandom() {
        assertNotSame(ByteUtil.generateRandomNum(), ByteUtil.generateRandomNum());
    }

    @SuppressLint("DefaultLocale")
    @Test
    public void testByteToHex() {
        String testStr = "01144add4445aaa839812cccad".toUpperCase();
        String result = ByteUtil.bytesToCapitalizedHex(ByteUtil.hexToBytes(testStr));

        String key = "173B7E0A9A54CB3E96A70237F6974940";
        String result2 = ByteUtil.bytesToCapitalizedHex(ByteUtil.hexToBytes(key));

        assertEquals(testStr, result);
        assertEquals(key, result2);
    }

    @Test
    public void testBase64() {
        byte[] testData =
                "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4="
                        .getBytes();
        byte[] result = null;
        try {
            String encoded = ByteUtil.encodeToBase64(testData);
            result = ByteUtil.decodeFromBase64(encoded);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        byte[] testData2 =
                "aj2DQZ4KYo6z4zrnjt/a7Vg6MH2wtDUbsAS3WixxNBZVvUaihF/mLGGlHRqU/eSyYyBNv6YbIm/QxPxIvhQOtCT3Nr7WU5J6lXzQ7N1gRsTfeIG78IUNQx+5Bqy86dmDfGFFoqESG/7nWZEkvk5UjcKI5WQHMrUOI0241KnzZG6hX66GkILMrONIM2uR+IsZyi5NoVwf9d9uDZaAlLupdSrEaqkxEkwF495pM1BzvTZUqb0qrrE/9K8TU4IYJFlRJvwGBN6PLdgKsTDb9jgyJ6ypk6qA4sIYi+VsRsrtv9M="
                        .getBytes();
        byte[] result2 = null;
        try {
            String encoded2 = ByteUtil.encodeToBase64(testData2);
            result2 = ByteUtil.decodeFromBase64(encoded2);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        assertTrue(Arrays.equals(testData, result));
        assertTrue(Arrays.equals(testData2, result2));
    }

    @Test
    public void testAddRandomBytesCaseOne() {
        String testStr = "Hello Security";
        byte[] testBytes = testStr.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);

        assertEquals(testRandomBytes.length, testBytes.length + ByteUtil.RANDOM_BYTE_ARR_SIZE);
    }

    @Test
    public void whenAddingRandomBytes_thenNotEqual() {
        String testStr = "Hello Security";

        byte[] testBytes = testStr.getBytes();
        byte[] testRandomBytes1 = ByteUtil.addRandomBytes(testBytes);
        byte[] testRandomBytes2 = ByteUtil.addRandomBytes(testBytes);

        assertFalse(Arrays.equals(testRandomBytes1, testRandomBytes2));
    }

    @Test
    public void testAddRandomBytesCaseTwo() {
        byte[] testBytes = null;
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);

        assertNull(testRandomBytes);
    }

    @Test
    public void testAddRandomBytesCaseThree() {
        byte[] testBytes = data.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);

        assertEquals(testRandomBytes.length, testBytes.length + ByteUtil.RANDOM_BYTE_ARR_SIZE);
    }

    @Test
    public void testRemoveRandomBytesCaseOne() {
        String testStr = "Hello Security";
        byte[] testBytes = testStr.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testRandomBytes);

        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());

        assertEquals(testStr, testStr1);
    }

    @Test
    public void testRemoveRandomBytesCaseTwo() {
        String testStr = "H";
        byte[] testBytes = testStr.getBytes();
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testBytes);

        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());

        assertEquals(testStr, testStr1);
    }

    @Test
    public void testRemoveRandomBytesCaseEmptyStr() {
        String testStr = "";
        byte[] testBytes = testStr.getBytes();
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testBytes);

        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());

        assertEquals(testStr, testStr1);
    }

    @Test
    public void testRemoveRandomBytesCaseThree() {
        byte[] testBytes = null;
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testBytes);

        assertNull(afterRemoveBytes);
    }

    @Test
    public void testRemoveRandomBytesCaseFour() {
        byte[] testBytes = data.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testRandomBytes);

        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());

        assertEquals(data, testStr1);
    }

    @Test
    public void whenEncodingNull_thenReturnsNull() {
        assertNull(ByteUtil.encodeToBase64(null));
    }

    @Test
    public void whenDecodingNull_thenReturnNull() {
        assertNull(ByteUtil.decodeFromBase64(null));
    }
}
