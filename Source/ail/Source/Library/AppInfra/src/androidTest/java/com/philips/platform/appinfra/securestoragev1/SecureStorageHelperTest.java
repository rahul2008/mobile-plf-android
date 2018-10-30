/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra.securestoragev1;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SecureStorageHelperTest {

    private SecureStorageHelper secureStorageHelper;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        secureStorageHelper = new SecureStorageHelper(mAppInfra);
        assertNotNull(secureStorageHelper.generateAESKey());
    }

    @Test
    public void testEncodeDecodeData() throws Exception {
        SecretKey secretKey = secureStorageHelper.generateAESKey();
        String encryptedData = secureStorageHelper.encodeDecodeData(Cipher.ENCRYPT_MODE, secretKey, "somevalue");
        assertNotNull(encryptedData);
        assertEquals("somevalue", secureStorageHelper.encodeDecodeData(Cipher.DECRYPT_MODE, secretKey, encryptedData));
    }

    @Test
    public void testEncodeDecodeBytes() throws Exception {
        SecretKey secretKey = secureStorageHelper.generateAESKey();
        byte[] bytes = "somevalue".getBytes();
        byte[] encryptedData = secureStorageHelper.encodeDecodeData(Cipher.ENCRYPT_MODE, secretKey, bytes);
        assertNotNull(encryptedData);
        assertTrue(Arrays.equals(bytes, secureStorageHelper.encodeDecodeData(Cipher.DECRYPT_MODE, secretKey, encryptedData)));
    }

    @Test
    public void testGetAppendedByte() throws Exception {
        byte[] encryptedBytes = "encryptedData".getBytes();
        byte[] ivBytes = "IVData".getBytes();
        assertNotNull(secureStorageHelper.getAppendedByte(encryptedBytes, ivBytes));
    }

    @Test
    public void testGetSplitData() throws Exception {
        String splitValue = "someSplitValuedelimiterdata";
        String[] splitData = secureStorageHelper.getSplitData(splitValue);
        assertEquals(splitData[0], "someSplitValue");
        assertEquals(splitData[1], "data");
        String[] someData = secureStorageHelper.getSplitData("somedatadelimiter");
        assertEquals(1, someData.length);
    }

    @Test
    public void testCheckProcess() {
        secureStorageHelper.checkProcess();
        secureStorageHelper.checkProcess();
    }
}