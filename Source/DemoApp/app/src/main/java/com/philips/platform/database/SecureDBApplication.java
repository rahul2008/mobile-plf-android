/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.database;

import android.app.Application;


public class SecureDBApplication extends Application {
    SecureDataBaseHelper secureDataBaseHelper;
    static SecureDataBaseQueryHelper secureDataBaseQueryHelper;
    private static final String DATABASE_NAME = "address.db";
    public static  String DATABASE_PASSWORD = "Philips@123#";
    private static  int DATABASE_VERSION = 3;
    @Override
    public void onCreate() {
        super.onCreate();
        secureDataBaseHelper=new SecureDataBaseHelper<>(this, AddressBook.class,DATABASE_NAME,DATABASE_VERSION,DATABASE_PASSWORD);
        secureDataBaseQueryHelper=new SecureDataBaseQueryHelper(this,secureDataBaseHelper, DATABASE_PASSWORD);

    }

public static SecureDataBaseQueryHelper getSecureDataBaseQueryHelper()
{
    return secureDataBaseQueryHelper;
}
}
