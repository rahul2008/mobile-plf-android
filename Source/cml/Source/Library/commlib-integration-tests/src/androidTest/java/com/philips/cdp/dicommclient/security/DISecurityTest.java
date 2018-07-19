/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DISecurityTest {

    private final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
    private final static String DATA = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

    @Mock
    private NetworkNode networkNodeMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testEncryptionDecryption() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(DATA);
        String decryptedData = security.decryptData(encryptedData);

        assertEquals(DATA, decryptedData);
    }

    @Test
    public void testDecryptNullData() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn(KEY);

        String decryptedData = security.decryptData(null);

        assertNull(decryptedData);
    }

    @Test
    public void testDataEncryptionWithRandomBytes() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn(KEY);

        String encryptedData1 = security.encryptData(DATA);
        String encryptedData2 = security.encryptData(DATA);

        assertFalse(encryptedData1 != null && encryptedData1.equals(encryptedData2));
    }

    @Test
    public void testEncryptDataNullNetworkNodeObject() {
        DISecurity security = new DISecurity(null);
        String encryptedData = security.encryptData(DATA);

        assertNull(encryptedData);
    }

    @Test
    public void testEncryptDataNullkey() {
        DISecurity security = new DISecurity(networkNodeMock);
        String encryptedData = security.encryptData(DATA);

        assertNull(encryptedData);
    }

    @Test
    public void testEncryptDataEmptykey() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn("");

        String encryptedData = security.encryptData(DATA);
        assertNull(encryptedData);
    }

    @Test
    public void testDecryptEmptyData() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn(KEY);

        String decryptData = security.decryptData("");

        assertNull(decryptData);
    }

    @Test
    public void testDecryptWithNullKey() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(DATA);

        when(networkNodeMock.getEncryptionKey()).thenReturn(null);

        String decryptData = security.decryptData(encryptedData);

        assertNull(decryptData);
    }

    @Test
    public void testDecryptWithEmptyKey() {
        DISecurity security = new DISecurity(networkNodeMock);

        when(networkNodeMock.getEncryptionKey()).thenReturn(KEY);

        String encryptedData = security.encryptData(DATA);

        when(networkNodeMock.getEncryptionKey()).thenReturn("");

        String decryptData = security.decryptData(encryptedData);

        assertNull(decryptData);
    }

    @Test
    public void testDecryptWithNullNetworkNodeObject() {
        DISecurity security = new DISecurity(null);
        String decryptData = security.decryptData("hello");

        assertNull(decryptData);
    }
}
