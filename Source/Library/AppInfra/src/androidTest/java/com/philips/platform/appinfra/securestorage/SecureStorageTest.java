/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.securestorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.MockitoTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by 310238114 on 4/7/2016.
 */

public class SecureStorageTest extends MockitoTestCase {
    SecureStorage mSecureStorage=null;
   // Context context = Mockito.mock(Context.class);

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
//        context = getInstrumentation().getContext();
//

//        MockitoAnnotations.initMocks(this);
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mSecureStorage = new SecureStorage(context);
        assertNotNull(mSecureStorage);



    }




    public void testStoreValueForKey() throws Exception {

        SecureStorage secureStorageMock = mock(SecureStorage.class);



        assertFalse(mSecureStorage.storeValueForKey("", "value"));
        assertFalse(mSecureStorage.storeValueForKey("", ""));
        assertFalse(mSecureStorage.storeValueForKey("key", null));
        assertFalse(mSecureStorage.storeValueForKey(null, "value"));
        assertFalse(mSecureStorage.storeValueForKey(null, null));
        assertTrue(mSecureStorage.storeValueForKey("key", "")); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey(" ", "val")); // value can be empty
        assertFalse(mSecureStorage.storeValueForKey("   ", "val")); // value can be empty

        assertTrue(mSecureStorage.storeValueForKey("key", "value")); // true condition

        // value passed by user should not be same as that of its encrypted equivalent

        }

    public void testFetchValuetForKey() throws Exception {

        assertNull(mSecureStorage.fetchValueForKey(null));
        assertNull(mSecureStorage.fetchValueForKey(""));
        assertNull(mSecureStorage.fetchValueForKey("NotSavedKey"));

    }

    public void testSharedPreferences(){
        final SharedPreferences sharedPreferencesMock = mock(SharedPreferences.class);
        when(sharedPreferencesMock.getString("key",null)).thenReturn("value");
        when(sharedPreferencesMock.getString("",null)).thenReturn(null);
        when(sharedPreferencesMock.getString(null,null)).thenReturn(null);
        SecureStorage secureStorage = new SecureStorage(context){
            @Override
            protected SharedPreferences getSharedPreferences() {
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
        assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored));
//        assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        assertTrue(mSecureStorage.removeValueForKey(keyStored));
        assertNull(mSecureStorage.fetchValueForKey(keyStored));
    }

    public void testMultipleCallIndependentMethods()throws Exception {
        String valueStored= "value";
        String keyStored= "key";
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored));
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
        int iCount;
        for(iCount=0;iCount<10;iCount++){
            assertTrue(mSecureStorage.storeValueForKey(keyStored, valueStored));
//            assertEquals(valueStored, mSecureStorage.fetchValueForKey(keyStored));
        }


    }


}