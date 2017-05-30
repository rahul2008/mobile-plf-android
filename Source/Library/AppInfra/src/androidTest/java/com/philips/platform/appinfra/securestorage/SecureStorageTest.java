/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by 310238114 on 4/7/2016.
 */

public class SecureStorageTest extends MockitoTestCase {
    SecureStorageInterface mSecureStorage=null;
   // Context context = Mockito.mock(Context.class);

    private Context context;
    private AppInfra mAppInfra;
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra =  new AppInfra.Builder().build(context);
        mSecureStorage = mAppInfra.getSecureStorage();
        assertNotNull(mSecureStorage);

    }




    public void testStoreValueForKey() throws Exception {

        SecureStorage secureStorageMock = mock(SecureStorage.class);

        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();

        assertFalse(mSecureStorage.storeValueForKey("", "value",sse));
        assertFalse(mSecureStorage.storeValueForKey("", "",sse));
        assertFalse(mSecureStorage.storeValueForKey("key", null,sse));
        assertFalse(mSecureStorage.storeValueForKey(null, "value",sse));
        assertFalse(mSecureStorage.storeValueForKey(null, null,sse));
        assertTrue(mSecureStorage.storeValueForKey("key", "",sse)); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey(" ", "val",sse)); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey("   ", "val",sse)); // value can be empty

        assertTrue(mSecureStorage.storeValueForKey("key", "value",sse)); // true condition

        // value passed by user should not be same as that of its encrypted equivalent

        }

    public void testFetchValueForKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey(null,sse));
        assertNull(mSecureStorage.fetchValueForKey("",sse));
        assertNull(mSecureStorage.fetchValueForKey("NotSavedKey",sse));

    }


    public void testCreateKey() throws Exception {

        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertFalse(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "", sse));
        assertFalse(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, null, sse));
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "KeyName", sse));
        assertTrue(mSecureStorage.createKey(null, "KeyName", sse));
        assertFalse(mSecureStorage.createKey(null, " ", sse));
        assertFalse(mSecureStorage.createKey(null, null, sse));

    }

    public void testGetKey() throws Exception {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "KeyName", sse));
        assertNotNull(mSecureStorage.getKey("KeyName", sse));
      /*  Key key =mSecureStorage.getKey("KeyName", sse);
        String keyString = Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
        Log.v("SqlCipher Data Key" , keyString );*/
    }

    public void testClearKey() {
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();

        assertFalse(mSecureStorage.clearKey(" ", sse));
        assertFalse(mSecureStorage.clearKey(null, sse));
        assertTrue(mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, "KeyName", sse));
        assertTrue(mSecureStorage.clearKey("KeyName", sse));

    }

    public void testSharedPreferences(){
        final SharedPreferences sharedPreferencesMock = mock(SharedPreferences.class);
        when(sharedPreferencesMock.getString("key",null)).thenReturn("value");
        when(sharedPreferencesMock.getString("",null)).thenReturn(null);
        when(sharedPreferencesMock.getString(null,null)).thenReturn(null);
        SecureStorage secureStorage = new SecureStorage(mAppInfra){

            private SharedPreferences getSharedPreferences() {
                return sharedPreferencesMock;
            }
        };
    }

    public void testRemoveValueForKey() throws Exception {

        assertFalse(mSecureStorage.removeValueForKey(""));
        assertFalse(mSecureStorage.removeValueForKey(null));

        //assertEquals(mSecureStorage.RemoveValueForKey("key"),mSecureStorage.deleteEncryptedData("key"));


    }
    public void testHappyPath()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
//        assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        assertNull(mSecureStorage.fetchValueForKey(keyStored,sse));
    }

    public void testMultipleCallIndependentMethods()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
        }
        for(iCount=0;iCount<10;iCount++) {
//            assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        }

        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        for(iCount=0;iCount<10;iCount++) {
            assertFalse(mSecureStorage.removeValueForKey(keyStored));
        }
    }

    public void testMultipleCallSequentialMethods()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
//            assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        }


    }

    public void testLargeValue() throws Exception {
        String valueStored = getString();
        String keyStored= "keyLarge";
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
        assertNotNull(mSecureStorage.fetchValueForKey(keyStored,sse));
        String actual = mSecureStorage.fetchValueForKey(keyStored, sse);
        assertEquals(valueStored, actual);
    }

    private String getString() {
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

    public void testgetDeviceCapability() {
        assertNotNull(mSecureStorage.getDeviceCapability());
    }

    public void testdevicehaspasscode() {
        assertNotNull(mSecureStorage.deviceHasPasscode());
    }

    public void testByteDataEncryptionAndDecryption() throws Exception {
        SecureStorageInterface.SecureStorageError sse;
        sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.encryptData(null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData, sse.getErrorCode());

        sse = new SecureStorageInterface.SecureStorageError();
        assertNull(mSecureStorage.decryptData(null, sse));
        assertEquals(SecureStorageInterface.SecureStorageError.secureStorageError.NullData, sse.getErrorCode());

       /* sse = new SecureStorageInterface.SecureStorageError();
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
        assertNull(sse.getErrorCode());*/
    }
}