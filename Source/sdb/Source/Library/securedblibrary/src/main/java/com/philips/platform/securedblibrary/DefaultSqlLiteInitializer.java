package com.philips.platform.securedblibrary;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

public class DefaultSqlLiteInitializer implements SqlLiteInitializer {
    @Override
    public void loadLibs(final Context context) {
        SQLiteDatabase.loadLibs(context);
    }
}
