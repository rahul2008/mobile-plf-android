package com.philips.cl.di.dicomm.security;

import java.nio.charset.Charset;
import java.util.Arrays;

import com.philips.cdp.dicommclient.security.ByteUtil;

import junit.framework.TestCase;
import android.annotation.SuppressLint;

public class ByteUtilTest extends TestCase {

    private String key = "173B7E0A9A54CB3E96A70237F6974940";
    public final static String DEVICE_ID = "deviceId";
    public final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
    String data = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

    public void testRandom() {
        assertNotSame(ByteUtil.generateRandomNum(), ByteUtil.generateRandomNum());
    }

    @SuppressLint("DefaultLocale")
    public void testByteToHex() {
        String testStr = new String("01144add4445aaa839812cccad").toUpperCase();
        String result = ByteUtil.bytesToCapitalizedHex(ByteUtil.hexToBytes(testStr));

        String result2 = ByteUtil.bytesToCapitalizedHex(ByteUtil.hexToBytes(key));

        assertEquals(testStr, result);
        assertEquals(key, result2);
    }

    public void testBase64() {
        byte[] testData = new String(
                "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=")
                .getBytes();
        byte[] result = null;
        try {
            String encoded = ByteUtil.encodeToBase64(testData);
            result = ByteUtil.decodeFromBase64(encoded);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        byte[] testData2 = new String(
                "aj2DQZ4KYo6z4zrnjt/a7Vg6MH2wtDUbsAS3WixxNBZVvUaihF/mLGGlHRqU/eSyYyBNv6YbIm/QxPxIvhQOtCT3Nr7WU5J6lXzQ7N1gRsTfeIG78IUNQx+5Bqy86dmDfGFFoqESG/7nWZEkvk5UjcKI5WQHMrUOI0241KnzZG6hX66GkILMrONIM2uR+IsZyi5NoVwf9d9uDZaAlLupdSrEaqkxEkwF495pM1BzvTZUqb0qrrE/9K8TU4IYJFlRJvwGBN6PLdgKsTDb9jgyJ6ypk6qA4sIYi+VsRsrtv9M=")
                .getBytes();
        byte[] result2 = null;
        try {
            String encoded2 = ByteUtil.encodeToBase64(testData2);
            result2 = ByteUtil.decodeFromBase64(encoded2);
        } catch (Exception e) {
            assertNotNull(result2);
        }

        assertTrue(Arrays.equals(testData, result));
        assertTrue(Arrays.equals(testData2, result2));
    }

    public void testGetRandomBytes() {
        byte[] byteArr1 = ByteUtil.getRandomByteArray(2);
        byte[] byteArr2 = ByteUtil.getRandomByteArray(2);

        assertFalse(byteArr1[0] == byteArr2[0]);
        assertFalse(byteArr1[1] == byteArr2[1]);
    }

    public void testAddRandomBytesCaseOne() {
        String testStr = "Hello Security";
        byte[] testBytes = testStr.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        assertEquals(testRandomBytes.length, testBytes.length + ByteUtil.RANDOM_BYTE_ARR_SIZE);
    }

    public void testAddRandomBytesCaseTwo() {
        byte[] testBytes = null;
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        assertNull(testRandomBytes);
    }

    public void testAddRandomBytesCaseThree() {
        byte[] testBytes = data.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        assertEquals(testRandomBytes.length, testBytes.length + ByteUtil.RANDOM_BYTE_ARR_SIZE);
    }

    public void testRemoveRandomBytesCaseOne() {
        String testStr = "Hello Security";
        byte[] testBytes = testStr.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testRandomBytes);

        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());
        assertEquals(testStr, testStr1);
    }

    public void testRemoveRandomBytesCaseTwo() {
        String testStr = "H";
        byte[] testBytes = testStr.getBytes();
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testBytes);
        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());
        assertEquals(testStr, testStr1);
    }

    public void testRemoveRandomBytesCaseEmptyStr() {
        String testStr = "";
        byte[] testBytes = testStr.getBytes();
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testBytes);
        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());
        assertEquals(testStr, testStr1);
    }

    public void testRemoveRandomBytesCaseThree() {
        byte[] testBytes = null;
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testBytes);
        assertNull(afterRemoveBytes);
    }

    public void testRemoveRandomBytesCaseFour() {

        byte[] testBytes = data.getBytes();
        byte[] testRandomBytes = ByteUtil.addRandomBytes(testBytes);
        byte[] afterRemoveBytes = ByteUtil.removeRandomBytes(testRandomBytes);

        String testStr1 = new String(afterRemoveBytes, Charset.defaultCharset());
        assertEquals(data, testStr1);
    }
}
