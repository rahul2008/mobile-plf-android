/*
 * © Koninklijke Philips N.V., 2015, 2016.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import com.philips.cdp.dicommclient.MockitoTestCase;
import com.philips.cdp.dicommclient.networknode.NetworkNode;

import org.mockito.Mockito;

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
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(data);
        String decrytedData = security.decryptData(encryptedData);
        assertEquals(data, decrytedData);
    }

    public void testDecryptNullData() {
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String nullData = null;
        String decrytedData = security.decryptData(nullData);

        assertNull(decrytedData);
    }

    public void testDataEncryptionWithRandomBytes() {
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String encryptedData1 = security.encryptData(data);
        String encryptedData2 = security.encryptData(data);

        assertFalse(encryptedData1.equals(encryptedData2));
    }

    public void testEncryptDataNullNetworkNodeObject() {
        DISecurity security = new DISecurity(null);
        String encryptedData1 = security.encryptData(data);
        assertNull(encryptedData1);
    }

    public void testEncryptDataNullkey() {
        DISecurity security = new DISecurity(mNetworkNode);
        String encryptedData1 = security.encryptData(data);
        assertNull(encryptedData1);
    }

    public void testEncryptDataEmptykey() {
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn("");

        String encryptedData1 = security.encryptData(data);
        assertNull(encryptedData1);
    }

    public void testDecryptEmptyData() {
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String decryptData = security.decryptData("");

        assertNull(decryptData);
    }

    public void testDecryptWithNullKey() {
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(data);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(null);

        String decryptData = security.decryptData(encryptedData);

        assertNull(decryptData);
    }

    public void testDecryptWithEmptyKey() {
        DISecurity security = new DISecurity(mNetworkNode);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(data);

        Mockito.when(mNetworkNode.getEncryptionKey()).thenReturn("");

        String decryptData = security.decryptData(encryptedData);

        assertNull(decryptData);
    }

    public void testDecryptWithNullNetworkNodeObject() {

        DISecurity security = new DISecurity(null);
        String decryptData = security.decryptData("hello");

        assertNull(decryptData);
    }
}
