package com.philips.cl.di.dicomm.security;

import org.mockito.Mockito;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.MockitoTestCase;

public class DISecurityTest extends MockitoTestCase {

	public final static String DEVICE_ID = "deviceId";
	public final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
	String data = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

	private NetworkNode mNetworkNode;
	
	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    
	    mNetworkNode = Mockito.mock(NetworkNode.class);
	}
	
	public void testEncryptionDecryption() {
		DISecurity security = new DISecurity();
		
        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

		String encryptedData = security.encryptData(data, mNetworkNode);
		String decrytedData = security.decryptData(encryptedData, mNetworkNode);
		assertEquals(data, decrytedData);
	}

	public void testDecryptNullData() {
		DISecurity security = new DISecurity();

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);
        
		String nullData = null;
		String decrytedData = security.decryptData(nullData, mNetworkNode);

		assertNull(decrytedData);
	}

	public void testDataEncryptionWithRandomBytes() {
		DISecurity security = new DISecurity();

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);
        
		String encryptedData1 = security.encryptData(data, mNetworkNode);
		String encryptedData2 = security.encryptData(data, mNetworkNode);
		
		assertFalse(encryptedData1.equals(encryptedData2));
	}

	public void testEncryptDataNullNetworkNodeObject() {
		DISecurity security = new DISecurity();
		String encryptedData1 = security.encryptData(data, null);
		assertNull(encryptedData1);
	}

	public void testEncryptDataNullkey() {
		DISecurity security = new DISecurity();
		String encryptedData1 = security.encryptData(data, mNetworkNode);
		assertNull(encryptedData1);
	}

	public void testEncryptDataEmptykey() {
		DISecurity security = new DISecurity();

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn("");
        
		String encryptedData1 = security.encryptData(data, mNetworkNode);
		assertNull(encryptedData1);
	}

	public void testDecryptEmptyData() {
		DISecurity security = new DISecurity();

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

		String decryptData = security.decryptData("", mNetworkNode);

		assertNull(decryptData);
	}

	public void testDecryptWithNullKey() {
		DISecurity security = new DISecurity();

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

		String encryptedData = security.encryptData(data, mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(null);

		String decryptData = security.decryptData(encryptedData, mNetworkNode);

		assertNull(decryptData);
	}

	public void testDecryptWithEmptyKey() {
        DISecurity security = new DISecurity();

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(data, mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn("");

        String decryptData = security.decryptData(encryptedData, mNetworkNode);

		assertNull(decryptData);
	}

	public void testDecryptWithNullNetworkNodeObject() {

		DISecurity security = new DISecurity();
		String decryptData = security.decryptData("hello", null);

		assertNull(decryptData);
	}
}
