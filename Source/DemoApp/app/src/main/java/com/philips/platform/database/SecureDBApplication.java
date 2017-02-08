/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.database;

import android.app.Application;
import android.content.SharedPreferences;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;


public class SecureDBApplication extends Application {
    public static String DATABASE_PASSWORD_KEY = "hi";
    static SecureStorageInterface mSecureStorage = null;
    static SecureDataBaseQueryHelper secureDataBaseQueryHelper;
    SecureDataBaseHelper secureDataBaseHelper;
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor;
    static AppInfraInterface appInfra;


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("com.appinfra", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstRun", true)) {
            appInfra = new AppInfra.Builder().build(getApplicationContext());
            mSecureStorage = appInfra.getSecureStorage();
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, DATABASE_PASSWORD_KEY, sse);

            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();

        }
    }

    public static AppInfraInterface getAppInfraInterface()
    {
       return appInfra;
    }

}
