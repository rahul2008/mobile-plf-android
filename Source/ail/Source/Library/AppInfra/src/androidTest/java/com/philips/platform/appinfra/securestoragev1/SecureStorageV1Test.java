/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.appinfra.securestoragev1;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * SecureStorage Test class.
 */

public class SecureStorageV1Test {

    private SecureStorageInterface mSecureStorage = null;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getContext();
        assertNotNull(context);

        final AppInfra mAppInfra = new AppInfra.Builder().build(context);
        mSecureStorage = new SecureStorageV1(mAppInfra);

        assertNotNull(mSecureStorage);
    }

    @Test
    public void testStoreValueForKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.storeValueForKey("", "", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
        assertFalse(mSecureStorage.storeValueForKey(null, "value", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testStoringFetchingRandomValue() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        String s = generateRandomNumber(3000);
        int randomInt = Integer.parseInt(s);
        String random_key = mSecureStorage.fetchValueForKey("random_key", sse);
        if (random_key != null) {
            Integer data = Integer.parseInt(random_key);
            assertNotNull(data);
        } else {
            boolean isStored = mSecureStorage.storeValueForKey("random_key", String.valueOf(randomInt), sse);
            assertTrue(isStored);
            random_key = mSecureStorage.fetchValueForKey("random_key", sse);
            Integer data = Integer.parseInt(random_key);
            assertNotNull(data);
            assertEquals(randomInt, data.intValue());
        }
    }

    @Test
    public void testForEmptyKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.storeValueForKey("", "value", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testStoringTrueCondition() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey("key", "value", sse)); // true condition
        sse = new SecureStorageInterface.SecureStorageError();
        assertEquals(mSecureStorage.fetchValueForKey("key", sse), "value");
        assertNull(sse.getErrorCode());
    }

    @Test
    public void testForSpaceKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.storeValueForKey(" ", "val", sse)); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey("   ", "val", sse)); // value can be empty
    }

    @Test
    public void testStoringEmptyValue() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey("key", "", sse)); // value can be empty
    }

    @Test
    public void testFetchValueForNullKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey(null, sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
        assertNull(mSecureStorage.fetchValueForKey("NotSavedKey", sse));
    }

    @Test
    public void testFetchValueTrueCondition() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        mSecureStorage.storeValueForKey("key", "valueForKey", sse);
        assertEquals(mSecureStorage.fetchValueForKey("key", sse), "valueForKey");
    }

    @Test
    public void testFetchValueForEmptyKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey("", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testCreateKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "KeyName", sse));
        assertNull(sse.getErrorCode());
        assertTrue(mSecureStorage.createKey(null, "KeyName", sse));
        assertFalse(mSecureStorage.createKey(null, " ", sse));
        assertFalse(mSecureStorage.createKey(null, null, sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testCreatingNullKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, null, sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testCreatingEmptyKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testGetKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "KeyName", sse));
        assertNull(sse.getErrorCode());
        assertNull(mSecureStorage.getKey("", sse));
        assertNull(mSecureStorage.getKey(null, sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.UnknownKey);
    }

    @Test
    public void testClearKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.clearKey("", sse));
        assertFalse(mSecureStorage.clearKey(null, sse));
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "KeyName", sse));
        assertTrue(mSecureStorage.clearKey("KeyName", sse));
        assertFalse(mSecureStorage.clearKey("KeyNameData", sse));
        assertEquals(sse.getErrorCode(), SecureStorageInterface.SecureStorageError.secureStorageError.DeleteError);
    }

    @Test
    public void testRemoveValueForKey() throws Exception {
        assertFalse(mSecureStorage.removeValueForKey(""));
        assertFalse(mSecureStorage.removeValueForKey(null));
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        mSecureStorage.storeValueForKey("someKey", "someValue", sse);
        assertTrue(mSecureStorage.removeValueForKey("someKey"));
    }

    @Test
    public void testHappyPath() throws Exception {
        String valueStored = "value";
        String keyStored = "key";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored, sse));
        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        assertNull(mSecureStorage.fetchValueForKey(keyStored, sse));
    }

    @Test
    public void testMultipleCallIndependentMethods() throws Exception {
        String valueStored = "value";
        String keyStored = "key";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        int iCount;
        for (iCount = 0; iCount < 10; iCount++) {
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored, sse));
        }
        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        for (iCount = 0; iCount < 10; iCount++) {
            assertFalse(mSecureStorage.removeValueForKey(keyStored));
        }
    }

    @Test
    public void testMultipleCallSequentialMethods() throws Exception {
        String valueStored = "value";
        String keyStored = "key";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        int iCount;
        for (iCount = 0; iCount < 10; iCount++) {
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored, sse));
        }
    }

    @Test
    public void testLargeValue() throws Exception {
        String valueStored = getLargeString();
        String keyStored = "keyLarge";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored, sse));
        assertNotNull(mSecureStorage.fetchValueForKey(keyStored, sse));
        String actual = mSecureStorage.fetchValueForKey(keyStored, sse);
        assertEquals(valueStored, actual);
    }

    @Test
    public void testByteDataEncryptionAndDecryption() throws Exception {
        SecureStorageInterface.SecureStorageError sse;
        sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.encryptData(null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData, sse.getErrorCode());

        sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.decryptData(null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData, sse.getErrorCode());

        sse = new SecureStorageInterface.SecureStorageError();
        byte[] plainByte = "abcd".getBytes();
        byte[] encBytes = mSecureStorage.encryptData(plainByte, sse);
        byte[] decBytes = mSecureStorage.decryptData(encBytes, sse);
        assertTrue(Arrays.equals(plainByte, decBytes));
        assertNull(sse.getErrorCode());

        sse = new SecureStorageInterface.SecureStorageError();
        byte[] plainByte1 = getLargeString().getBytes();
        byte[] encBtyes1 = mSecureStorage.encryptData(plainByte1, sse);
        byte[] decBtyes1 = mSecureStorage.decryptData(encBtyes1, sse);
        assertTrue(Arrays.equals(plainByte1, decBtyes1));
        assertNull(sse.getErrorCode());
    }

    private String generateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }

    private String getLargeString() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("big_string");
        File file = new File(resource.getPath());
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }
}