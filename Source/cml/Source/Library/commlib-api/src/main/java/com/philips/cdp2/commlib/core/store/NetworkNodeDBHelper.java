package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;

public interface NetworkNodeDBHelper {
    Cursor query(String selection, String[] selectionArgs) throws Exception;

    long insertRow(ContentValues values) throws Exception;

    int deleteNetworkNodeWithCppId(String cppId) throws Exception;
}
