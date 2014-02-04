package com.philips.cl.disecurity.disecuritysample.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Hashtable;

import com.philips.cl.disecurity.DISecurity;
import com.philips.cl.disecurity.Util;

import junit.framework.TestCase;


public class DISecurityTest extends TestCase {
	
	
	public final static String DEVICE_ID = "deviceId";
	public final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";

	public void testSample() {
		assertEquals(1, 1);
	}
	
	public void testRandom() {
		assertNotSame(Util.generateRandomNum(), Util.generateRandomNum());
	}
	
	public void testByteToHex() {
		String testStr = new String("01144add4445aaa839812cccad").toUpperCase();
		String result = Util.bytesToHex(Util.hexToBytes(testStr));
		
		String testStr2 = "173B7E0A9A54CB3E96A70237F6974940";
		String result2 = Util.bytesToHex( Util.hexToBytes(testStr2));	
				
		assertEquals(testStr,result);
		assertEquals(testStr2,result2);
	}
	
	public void testBase64() {
		byte[] testData = new String("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=").getBytes();
		byte[] result = null;
		try {
			String encoded = Util.encodeToBase64(testData);
			result = Util.decodeFromBase64(encoded);
		} catch (Exception e) {
			assertNotNull(result);
		}
		
		byte[] testData2 = new String("aj2DQZ4KYo6z4zrnjt/a7Vg6MH2wtDUbsAS3WixxNBZVvUaihF/mLGGlHRqU/eSyYyBNv6YbIm/QxPxIvhQOtCT3Nr7WU5J6lXzQ7N1gRsTfeIG78IUNQx+5Bqy86dmDfGFFoqESG/7nWZEkvk5UjcKI5WQHMrUOI0241KnzZG6hX66GkILMrONIM2uR+IsZyi5NoVwf9d9uDZaAlLupdSrEaqkxEkwF495pM1BzvTZUqb0qrrE/9K8TU4IYJFlRJvwGBN6PLdgKsTDb9jgyJ6ypk6qA4sIYi+VsRsrtv9M=").getBytes();
		byte[] result2 = null;
		try {
			String encoded2 = Util.encodeToBase64(testData2);
			result2 = Util.decodeFromBase64(encoded2);
		} catch (Exception e) {
			assertNotNull(result2);
		}

		assertTrue(Arrays.equals(testData, result));
		assertTrue(Arrays.equals(testData2, result2));
	}
	
	
	public void testEncryptionDecryption() {
		
		DISecurity security = new DISecurity(null);
		
		try {
			Field keysField = DISecurity.class.getDeclaredField("securityHashtable");
			keysField.setAccessible(true);
			Hashtable<String, String> keysTable = (Hashtable<String, String>) keysField.get(security);
			keysTable.put(DEVICE_ID, KEY);
			
		} catch (Exception e) {
			e.printStackTrace();
			assertNotNull(null);
		}
		
		String data = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";
		String encryptedData = security.encryptData(data, DEVICE_ID);
		String decrytedData = security.decryptData(encryptedData, DEVICE_ID);
		
		assertEquals(data, decrytedData);
		
	}
	
	public void testDiffieGeneration() {
		DISecurity security = new DISecurity(null);
		
		String diffie1 = null;
		String diffie2 = null;
		
		try {
			Method diffieMethod = DISecurity.class.getDeclaredMethod("generateDiffieKey", (Class<?>[])null);
			diffieMethod.setAccessible(true);
			diffie1 = (String) diffieMethod.invoke(security, (Object[])null);
			diffie2 = (String) diffieMethod.invoke(security, (Object[])null);
		} catch (Exception e) {
			e.printStackTrace();
			assertNotNull(null);
		}
		
		assertNotNull(diffie1);
		assertNotNull(diffie2);
		assertNotSame(diffie1, diffie2);
	}
}
