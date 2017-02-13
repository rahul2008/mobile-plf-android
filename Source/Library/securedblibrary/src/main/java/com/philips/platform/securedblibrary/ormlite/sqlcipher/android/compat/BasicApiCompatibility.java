package com.philips.platform.securedblibrary.ormlite.sqlcipher.android.compat;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;;

/**
 * Basic class which provides no-op methods for all Android version.
 *
 */
public class BasicApiCompatibility implements ApiCompatibility {


    @Override
    public Cursor rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs, CancellationHook cancellationHook) {
        // NOTE: cancellationHook will always be null
        return db.rawQuery(sql, selectionArgs);
    }

    @Override
    public CancellationHook createCancellationHook() {
        return null;
    }
}