/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.securedblibrary.securestoragedb;

import android.content.Context;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.securedblibrary.MockitoTestCase;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Created by 310238114 on 4/7/2016.
 */

public class SecureStorageDBTest extends MockitoTestCase {
    SecureStorageInterface mSecureStorage=null;
   // Context context = Mockito.mock(Context.class);

    private Context context;
    private AppInfra mAppInfra;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        //mAppInfra =  new AppInfra.Builder().build(context);
       // mSecureStorage = mAppInfra.getSecureStorage();
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












}