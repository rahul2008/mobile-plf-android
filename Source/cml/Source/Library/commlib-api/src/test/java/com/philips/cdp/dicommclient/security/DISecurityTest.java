/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.security;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import java.nio.charset.Charset;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*" })
@PrepareForTest({ByteUtil.class, EncryptionUtil.class})
public class DISecurityTest {

    private final static String KEY = "173B7E0A9A54CB3E96A70237F6974940";
    private final static String DATA = "{\"aqi\":\"0\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"78\",\"fs2\":\"926\",\"fs3\":\"2846\",\"fs4\":\"2846\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"13002\",\"psens\":\"1\"}";

    @Mock
    private NetworkNode networkNodeMock;

    @Before
    public void setUp() throws Exception {
        DICommLog.disableLogging();

        initMocks(this);
        mockStatic(ByteUtil.class, EncryptionUtil.class);

        when(ByteUtil.encodeToBase64((byte[])any())).thenAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) throws Throwable {
                return new String((byte[])invocation.getArguments()[0]);
            }
        });
        when(ByteUtil.decodeFromBase64(anyString())).thenAnswer(new Answer<byte[]>() {
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                return (new String(((String) invocation.getArguments()[0]).getBytes(), Charset.defaultCharset())).getBytes();
            }
        });
        when(ByteUtil.getRandomByteArray(anyInt())).thenReturn(new byte[] {123, 123});
        when(ByteUtil.addRandomBytes((byte[])any())).thenCallRealMethod();
        when(ByteUtil.removeRandomBytes((byte[])any())).thenCallRealMethod();

        when(EncryptionUtil.aesEncryptData(anyString(), anyString())).thenAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                return (ByteUtil.addRandomBytes(((String) invocation.getArguments()[0]).getBytes(Charset.defaultCharset())));
            }
        });
        when(EncryptionUtil.aesDecryptData((byte[])any(), anyString())).thenAnswer(new Answer<byte[]>() {
            @Override
            public byte[] answer(InvocationOnMock invocation) throws Throwable {
                return (byte[]) invocation.getArguments()[0];
            }
        });
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

        when(ByteUtil.getRandomByteArray(anyInt())).thenReturn(new byte[] {124, 123});
        String encryptedData2 = security.encryptData(DATA);

        assertNotNull(encryptedData1);
        assertNotNull(encryptedData2);
        assertNotEquals(encryptedData1, encryptedData2);
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