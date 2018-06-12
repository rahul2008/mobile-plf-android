/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.VisibleForTesting;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ContextProvider;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;

class NonSecureNetworkNodeDatabaseHelper extends SQLiteOpenHelper implements DatabaseHelper {

    static final String DB_NAME = "network_node.db";

    NonSecureNetworkNodeDatabaseHelper() {
        this(ContextProvider.get(), NetworkNodeDatabaseSchema.DB_VERSION);
    }

    @VisibleForTesting
    NonSecureNetworkNodeDatabaseHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(NetworkNodeDatabaseSchema.CREATE_NETWORK_NODE_TABLE);
        } catch (SQLException e) {
            DICommLog.e(DICommLog.DATABASE, "Table creation error" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String[] upgradeQuery = NetworkNodeDatabaseSchema.getUpgradeQuery(oldVersion, newVersion);

        for (String query : upgradeQuery) {
            db.execSQL(query);
        }
    }

    @Override
    public Cursor query(String selection, String[] selectionArgs) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE, null, selection, selectionArgs, null, null, null);
    }

    @Override
    public long insertRow(ContentValues values) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insertWithOnConflict(NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int delete(String selection) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE, KEY_CPP_ID + "= ?", new String[]{selection});
    }
}
