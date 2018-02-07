/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.appinfra.securestorage;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class SecureStorageHelperTest extends AppInfraInstrumentation {

    private SecureStorageHelper secureStorageHelper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        secureStorageHelper = new SecureStorageHelper(mAppInfra);
        assertNotNull(secureStorageHelper.generateAESKey());
    }

    public void testEncodeDecodeData() throws Exception {
        SecretKey secretKey = secureStorageHelper.generateAESKey();
        String encryptedData = secureStorageHelper.encodeDecodeData(Cipher.ENCRYPT_MODE, secretKey, "somevalue");
        assertNotNull(encryptedData);
        assertEquals("somevalue", secureStorageHelper.encodeDecodeData(Cipher.DECRYPT_MODE, secretKey, encryptedData));
    }

    public void testEncodeDecodeBytes() throws Exception {
        SecretKey secretKey = secureStorageHelper.generateAESKey();
        byte[] bytes = "somevalue".getBytes();
        byte[] encryptedData = secureStorageHelper.encodeDecodeData(Cipher.ENCRYPT_MODE, secretKey, bytes);
        assertNotNull(encryptedData);
        assertTrue(Arrays.equals(bytes, secureStorageHelper.encodeDecodeData(Cipher.DECRYPT_MODE, secretKey, encryptedData)));
    }

    public void testGetAppendedByte() throws Exception {
        byte[] encryptedBytes = "encryptedData".getBytes();
        byte[] ivBytes = "IVData".getBytes();
        assertNotNull(secureStorageHelper.getAppendedByte(encryptedBytes, ivBytes));
    }

    public void testGetSplitData() throws Exception {
        String splitValue = "someSplitValuedelimiterdata";
        String[] splitData = secureStorageHelper.getSplitData(splitValue);
        assertEquals(splitData[0], "someSplitValue");
        assertEquals(splitData[1], "data");
        String[] someData = secureStorageHelper.getSplitData("somedatadelimiter");
        assertTrue(someData.length == 1);
    }

    public void testCheckProcess() {
        assertNotNull(secureStorageHelper.checkProcess());
        assertNotNull(secureStorageHelper.checkProcess());
    }

}