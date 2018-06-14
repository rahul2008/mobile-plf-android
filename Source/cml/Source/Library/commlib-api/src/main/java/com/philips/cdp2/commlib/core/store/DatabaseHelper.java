package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

public interface DatabaseHelper {
    long insertRow(ContentValues values) throws SQLException;

    Cursor query(String selection, String[] selectionArgs) throws SQLException;

    int delete(String selection) throws SQLException;

    void close();
}
