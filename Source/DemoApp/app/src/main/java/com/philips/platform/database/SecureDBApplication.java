/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.database;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;


public class SecureDBApplication extends Application {
    SecureDataBaseHelper secureDataBaseHelper;
    static SecureDataBaseQueryHelper secureDataBaseQueryHelper;
    private static final String DATABASE_NAME = "address.db";
    public static  String DATABASE_PASSWORD_KEY = "hi";
    private static  int DATABASE_VERSION = 3;
   static SecureStorageInterface mSecureStorage=null;
    @Override
    public void onCreate() {
        super.onCreate();
        AppInfraInterface appInfra= new AppInfra.Builder().build(getApplicationContext());
        mSecureStorage = appInfra.getSecureStorage();
        SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
        mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, DATABASE_PASSWORD_KEY, sse);

        secureDataBaseHelper=new SecureDataBaseHelper<>(this, AddressBook.class,DATABASE_NAME,DATABASE_VERSION,DATABASE_PASSWORD_KEY);
        secureDataBaseQueryHelper=new SecureDataBaseQueryHelper(this,secureDataBaseHelper, "hi");

    }

public static SecureDataBaseQueryHelper getSecureDataBaseQueryHelper()
{
    return secureDataBaseQueryHelper;
}
    public static SecureStorageInterface getSecureStorageInterface()
    {
        return mSecureStorage;
    }
}
