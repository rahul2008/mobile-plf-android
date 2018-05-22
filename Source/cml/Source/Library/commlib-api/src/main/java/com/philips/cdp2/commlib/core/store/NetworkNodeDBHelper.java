package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;

public interface NetworkNodeDBHelper {
    long insertRow(ContentValues values);

    Cursor query(String selection, String[] selectionArgs);

    int deleteNetworkNodeWithCppId(String cppId);

    void close();
}
