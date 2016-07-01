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
//        context = getInstrumentation().getContext();
//

//        MockitoAnnotations.initMocks(this);
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra =  new AppInfra.Builder().build(context);
        mSecureStorage = mAppInfra.getSecureStorage();
        assertNotNull(mSecureStorage);



    }




    public void testStoreValueForKey() throws Exception {

        SecureStorage secureStorageMock = mock(SecureStorage.class);

        SecureStorageError sse = new SecureStorageError();

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

    public void testFetchValuetForKey() throws Exception {
        SecureStorageError sse = new SecureStorageError();
        assertNull(mSecureStorage.fetchValueForKey(null,sse));
        assertNull(mSecureStorage.fetchValueForKey("",sse));
        assertNull(mSecureStorage.fetchValueForKey("NotSavedKey",sse));

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
        SecureStorageError sse = new SecureStorageError();
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
//        assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        assertNull(mSecureStorage.fetchValueForKey(keyStored,sse));
    }

    public void testMultipleCallIndependentMethods()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        SecureStorageError sse = new SecureStorageError();
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
        SecureStorageError sse = new SecureStorageError();
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored,sse));
//            assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        }


    }


}