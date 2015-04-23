package com.philips.cl.di.dicomm.security;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.ews.EWSConstant;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dicomm.security.DISecurity;


public class DISecurityTest extends TestCase {

	private AirPurifier purAirDevice;
	private String key = "173B7E0A9A54CB3E96A70237F6974940";
	public final static String DEVICE_ID = "deviceId";
	public final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
	String data = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

	public void testEncryptionDecryption() {
		DISecurity security = new DISecurity();

		AirPurifier helperDevice = new AirPurifier(null, DEVICE_ID, null, null, null, -1, ConnectionState.DISCONNECTED);
		helperDevice.getNetworkNode().setEncryptionKey(KEY);

		String encryptedData = security.encryptData(data, helperDevice.getNetworkNode());
		String decrytedData = security.decryptData(encryptedData, helperDevice.getNetworkNode());
		assertEquals(data, decrytedData);
	}

	public void testDecryptNullData() {
		DISecurity security = new DISecurity();

		AirPurifier helperDevice = new AirPurifier(null, DEVICE_ID, null, null, null, -1, ConnectionState.DISCONNECTED);
		helperDevice.getNetworkNode().setEncryptionKey(KEY);

		String nullData = null;
		String decrytedData = security.decryptData(nullData, helperDevice.getNetworkNode());

		assertNull(decrytedData);
	}

	public void testDataEncryptionWithRandomBytes() {
		DISecurity security = new DISecurity();
		purAirDevice = new AirPurifier(null,
				null, null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);

		String encryptedData1 = security.encryptData(data, purAirDevice.getNetworkNode());
		String encryptedData2 = security.encryptData(data, purAirDevice.getNetworkNode());
		assertFalse(encryptedData1.equals(encryptedData2));
	}

	public void testEncryptDataNullPurifierObject() {
		DISecurity security = new DISecurity();
		String encryptedData1 = security.encryptData(data, null);
		assertNull(encryptedData1);
	}

	public void testEncryptDataNullkey() {
		DISecurity security = new DISecurity();
		purAirDevice = new AirPurifier(null,
				null, null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(null);
		String encryptedData1 = security.encryptData(data, purAirDevice.getNetworkNode());
		assertNull(encryptedData1);
	}

	public void testEncryptDataEmptykey() {
		DISecurity security = new DISecurity();
		purAirDevice = new AirPurifier(null,
				null, null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey("");
		String encryptedData1 = security.encryptData(data, purAirDevice.getNetworkNode());
		assertNull(encryptedData1);
	}

	public void testDecryptEmptyData() {

		DISecurity security = new DISecurity();
		purAirDevice = new AirPurifier(null,
				"1c5a6bfffe6c74b2", null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);

		String decryptData = security.decryptData("", purAirDevice.getNetworkNode());

		assertNull(decryptData);
	}

	public void testDecryptWithNullKey() {

		DISecurity security = new DISecurity();
		purAirDevice = new AirPurifier(null,
				"1c5a6bfffe6c74b2", null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);

		String encryptedData = security.encryptData(data, purAirDevice.getNetworkNode());

		purAirDevice.getNetworkNode().setEncryptionKey(null);

		String decryptData = security.decryptData(encryptedData, purAirDevice.getNetworkNode());

		assertNull(decryptData);
	}

	public void testDecryptWithEmptyKey() {

		DISecurity security = new DISecurity();
		purAirDevice = new AirPurifier(null,
				"1c5a6bfffe6c74b2", null, EWSConstant.PURIFIER_ADHOCIP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		purAirDevice.getNetworkNode().setEncryptionKey(key);

		String encryptedData = security.encryptData(data, purAirDevice.getNetworkNode());

		purAirDevice.getNetworkNode().setEncryptionKey("");

		String decryptData = security.decryptData(encryptedData, purAirDevice.getNetworkNode());

		assertNull(decryptData);
	}

	public void testDecryptWithNullPurifierObject() {

		DISecurity security = new DISecurity();
		String decryptData = security.decryptData("hello", null);

		assertNull(decryptData);
	}
}
