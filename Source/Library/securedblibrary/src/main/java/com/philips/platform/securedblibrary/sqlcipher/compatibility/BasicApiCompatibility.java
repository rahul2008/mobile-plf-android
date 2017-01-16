package com.philips.platform.securedblibrary.sqlcipher.compatibility;

import android.database.Cursor;

import com.philips.platform.securedblibrary.sqlcipher.compatibility.ApiCompatibility;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * Basic class which provides no-op methods for all Android version.
 * 
 * @author graywatson
 */
public class BasicApiCompatibility implements ApiCompatibility {

	public Cursor rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs, CancellationHook cancellationHook) {
		// NOTE: cancellationHook will always be null
		return db.rawQuery(sql, selectionArgs);
	}

	public CancellationHook createCancellationHook() {
		return null;
	}
}
