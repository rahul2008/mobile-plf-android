package com.philips.cl.di.dev.pa.test;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;

import junit.framework.TestCase;
import android.annotation.SuppressLint;

import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.security.DISecurity;
import com.philips.cl.di.dev.pa.security.Util;


public class DISecurityTest extends TestCase {
	
	private AirPurifier purAirDevice;
	private String key = "173B7E0A9A54CB3E96A70237F6974940";
	public final static String DEVICE_ID = "deviceId";
	public final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
	String data = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

	public void testRandom() {
		assertNotSame(Util.generateRandomNum(), Util.generateRandomNum());
	}
	
	@SuppressLint("DefaultLocale")
	public void testByteToHex() {
		String testStr = new String("01144add4445aaa839812cccad").toUpperCase();
		String result = Util.bytesToHex(Util.hexToBytes(testStr));
		
		String result2 = Util.bytesToHex( Util.hexToBytes(key));	
				
		assertEquals(testStr,result);
		assertEquals(key,result2);
	}
	
	public void testBase64() {
		byte[] testData = new String("TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdlLCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=").getBytes();
		byte[] result = null;
		try {
			String encoded = Util.encodeToBase64(testData);
			result = Util.decodeFromBase64(encoded);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
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
		
		AirPurifier helperDevice = new AirPurifier(DEVICE_ID, null, null, null, -1, ConnectionState.DISCONNECTED);
		helperDevice.getNetworkNode().setEncryptionKey(KEY);
		
		String encryptedData = security.encryptData(data, helperDevice.getNetworkNode());
		String decrytedData = security.decryptData(encryptedData, helperDevice.getNetworkNode());
		assertEquals(data, decrytedData);
		
	}
	
	public void testDecryptNullData() {
		DISecurity security = new DISecurity(null);

		AirPurifier helperDevice = new AirPurifier(DEVICE_ID, null, null, null, -1, ConnectionState.DISCONNECTED);
		helperDevice.getNetworkNode().setEncryptionKey(KEY);
		
		String nullData = null;
		String decrytedData = security.decryptData(nullData, helperDevice.getNetworkNode());
		
		assertNull(decrytedData);
	}
	
	public void testDiffieGeneration() {
		DISecurity security = new DISecurity(null);
		
		String diffie1 = null;
		String diffie2 = null;
		
		try {
			@SuppressWarnings("rawtypes")
			Class[] cArg = new Class[1];
		    cArg[0] = String.class;
			Method diffieMethod = DISecurity.class.getDeclaredMethod("generateDiffieKey", cArg);
			diffieMethod.setAccessible(true);
			diffie1 = (String) diffieMethod.invoke(security, "111");
			diffie2 = (String) diffieMethod.invoke(security, "222");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		assertNotNull(diffie1);
		assertNotNull(diffie2);
		assertNotSame(diffie1, diffie2);
	}
	
	public void testGetEvenNumberSecretKey255bitKey() {
		String key255bit = "9cd15f5d121ec8c9adbd0682fb9e8d079cba90e7683230985a895f6d90b7d87884e4a3a4cc80ac58889de8f174d0df7dd4fd1c3e7d1f766fdeed89154ea6714ee8f70e551299e41ff8a6f51d60f2f763d8b58af70119fc0734ee80ddbccf0f84d22b5add6103be35dfff1a521075d973fc3262a98a5378364851bbd6a7b1cab";
		
		String newKey = Util.getEvenNumberSecretKey(key255bit);
		assertEquals(256, newKey.length());
	}
	
	public void testGetEvenNumberSecretKey256bitKey() {
		String key256bit = "2253FD28E69FAC2F38620ED0B6F84565C7634E586CFF83C6300AC296F6DFE1C668D04627F6F929569BC2F783DAB16EAC33D6231959EEC9EBB1BAD522B7B919EA4C51C660A148DCA3B3B2AD558B07DF959337E64423BF4EC8EBD2333032AF11FC430746965E30862680EB9CF075AFD87B60F597699F2EE4354796C7ADAB581747";
		
		String newKey = Util.getEvenNumberSecretKey(key256bit);
		assertEquals(256, newKey.length());
	}
	
	public void testGetRandomBytes() {
		byte[] byteArr1 = Util.getRandomByteArray(2);
		byte[] byteArr2 = Util.getRandomByteArray(2);
		
		assertFalse(byteArr1[0] == byteArr2[0]);
		assertFalse(byteArr1[1] == byteArr2[1]);
	}
	
	public void testAddRandomBytesCaseOne() {
		String testStr = "Hello Security";
		byte[] testBytes = testStr.getBytes();
		byte[] testRandomBytes = Util.addRandomBytes(testBytes);
		assertEquals(testRandomBytes.length, testBytes.length + Util.RANDOM_BYTE_ARR_SIZE);
	}
	
	public void testAddRandomBytesCaseTwo() {
		byte[] testBytes = null;
		byte[] testRandomBytes = Util.addRandomBytes(testBytes);
		assertNull(testRandomBytes);
	}
	
	public void testAddRandomBytesCaseThree() {
		byte[] testBytes = data.getBytes();
		byte[] testRandomBytes = Util.addRandomBytes(testBytes);
		assertEquals(testRandomBytes.length, testBytes.length + Util.RANDOM_BYTE_ARR_SIZE);
	}
	
	public void testRemoveRandomBytesCaseOne() {
		String testStr = "Hello Security";
		byte[] testBytes = testStr.getBytes();
		byte[] testRandomBytes = Util.addRandomBytes(testBytes);
		byte[] afterRemoveBytes = Util.removeRandomBytes(testRandomBytes);
		
		String testStr1 = new String(afterRemoveBytes,Charset.defaultCharset());
		assertEquals(testStr, testStr1);
	}
	
	public void testRemoveRandomBytesCaseTwo() {
		String testStr = "H";
		byte[] testBytes = testStr.getBytes();
		byte[] afterRemoveBytes = Util.removeRandomBytes(testBytes);
		String testStr1 = new String(afterRemoveBytes,Charset.defaultCharset());
		assertEquals(testStr, testStr1);
	}
	
	public void testRemoveRandomBytesCaseEmptyStr() {
		String testStr = "";
		byte[] testBytes = testStr.getBytes();
		byte[] afterRemoveBytes = Util.removeRandomBytes(testBytes);
		String testStr1 = new String(afterRemoveBytes,Charset.defaultCharset());
		assertEquals(testStr, testStr1);
	}
	
	public void testRemoveRandomBytesCaseThree() {
		byte[] testBytes = null;
		byte[] afterRemoveBytes = Util.removeRandomBytes(testBytes);
		assertNull(afterRemoveBytes);
	}
	
	public void testRemoveRandomBytesCaseFour() {
		
		byte[] testBytes = data.getBytes();
		byte[] testRandomBytes = Util.addRandomBytes(testBytes);
		byte[] afterRemoveBytes = Util.removeRandomBytes(testRandomBytes);
		
		String testStr1 = new String(afterRemoveBytes,Charset.defaultCharset());
		assertEquals(data, testStr1);
	}
	
	public void testDataEncryptionWithRandomBytes() {
		DISecurity security = new DISecurity(null);
		purAirDevice = new AirPurifier(
				null, null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);
			
		String encryptedData1 = security.encryptData(data, purAirDevice.getNetworkNode());
		String encryptedData2 = security.encryptData(data, purAirDevice.getNetworkNode());
		assertFalse(encryptedData1.equals(encryptedData2));
	}
	
	public void testEncryptDataNullPurifierObject() {
		DISecurity security = new DISecurity(null);
		String encryptedData1 = security.encryptData(data, null);
		assertNull(encryptedData1);
	}
	
	public void testEncryptDataNullkey() {
		DISecurity security = new DISecurity(null);
		purAirDevice = new AirPurifier(
				null, null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(null);
		String encryptedData1 = security.encryptData(data, purAirDevice.getNetworkNode());
		assertNull(encryptedData1);
	}
	
	public void testEncryptDataEmptykey() {
		DISecurity security = new DISecurity(null);
		purAirDevice = new AirPurifier(
				null, null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey("");
		String encryptedData1 = security.encryptData(data, purAirDevice.getNetworkNode());
		assertNull(encryptedData1);
	}
		
	public void testDecryptEmptyData() {
		
		DISecurity security = new DISecurity(null);
		purAirDevice = new AirPurifier(
				"1c5a6bfffe6c74b2", null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);
		
		String decryptData = security.decryptData("", purAirDevice.getNetworkNode());
		
		assertNull(decryptData);
	}
	
	public void testDecryptWithNullKey() {
		
		DISecurity security = new DISecurity(null);
		purAirDevice = new AirPurifier(
				"1c5a6bfffe6c74b2", null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);
		
		String encryptedData = security.encryptData(data, purAirDevice.getNetworkNode());
		
		purAirDevice.getNetworkNode().setEncryptionKey(null);
		
		String decryptData = security.decryptData(encryptedData, purAirDevice.getNetworkNode());
		
		assertNull(decryptData);
	}
	
	public void testDecryptWithEmptyKey() {
		
		DISecurity security = new DISecurity(null);
		purAirDevice = new AirPurifier(
				"1c5a6bfffe6c74b2", null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);
		
		String encryptedData = security.encryptData(data, purAirDevice.getNetworkNode());
		
		purAirDevice.getNetworkNode().setEncryptionKey("");
		
		String decryptData = security.decryptData(encryptedData, purAirDevice.getNetworkNode());
		
		assertNull(decryptData);
	}
	
	public void testDecryptWithNullPurifierObject() {
		
		DISecurity security = new DISecurity(null);
		String decryptData = security.decryptData("hello", null);
		
		assertNull(decryptData);
	}
}
