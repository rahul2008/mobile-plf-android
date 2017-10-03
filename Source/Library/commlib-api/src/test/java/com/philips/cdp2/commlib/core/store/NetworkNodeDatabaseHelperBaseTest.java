/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.store;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.After;
import org.robolectric.RuntimeEnvironment;

import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.DB_SCHEMA;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.DB_VERSION;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.TABLE_NETWORK_NODE;
import static org.junit.Assert.assertEquals;

abstract public class NetworkNodeDatabaseHelperBaseTest extends RobolectricTest {

    NetworkNodeDatabaseHelper networkNodeDatabaseHelper;

    @Override
    @After
    public void tearDown() throws Exception {
        if (networkNodeDatabaseHelper != null) {
            networkNodeDatabaseHelper.close();
        }
    }

    @NonNull
    Cursor getReadableDatabaseCursor() {
        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    void closeCursor(Cursor cursor) {
        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    void verifyDatabaseUpgrade(int oldVersion, String networkNodeTableStructure) {
        final SQLiteDatabase database = prepareSqliteDatabase(oldVersion, networkNodeTableStructure);

        networkNodeDatabaseHelper.onUpgrade(database, oldVersion, DB_VERSION);

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
    }

    @NonNull
    SQLiteDatabase prepareSqliteDatabase(final int oldVersion, String networkNodeTableStructure) {
        networkNodeDatabaseHelper = new NetworkNodeDatabaseHelper(RuntimeEnvironment.application, oldVersion) {
            @Override
            void onBeforeUpgrade(SQLiteDatabase db, int currentVersion) {
                logTableColumns(db, currentVersion);
            }
        };

        final SQLiteDatabase database = networkNodeDatabaseHelper.getWritableDatabase();

        try {
            database.execSQL("DROP TABLE " + TABLE_NETWORK_NODE + ";");
            database.execSQL(networkNodeTableStructure);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return database;
    }

    @NonNull
    private Set<String> getColumns(SQLiteDatabase database) {
        Cursor c = database.rawQuery("PRAGMA table_info(" + TABLE_NETWORK_NODE + ")", null);
        c.getColumnNames();
        Set<String> columnNames = new HashSet<>();
        if (c.moveToFirst()) {
            do {
                columnNames.add(c.getString(c.getColumnIndex("name")));
            } while (c.moveToNext());
        }
        return columnNames;
    }

    private void logTableColumns(SQLiteDatabase db, int currentVersion) {
        Cursor c = db.rawQuery("PRAGMA table_info(" + TABLE_NETWORK_NODE + ")", null);
        if (c.moveToFirst()) {
            System.out.println("** " + TABLE_NETWORK_NODE + "(" + currentVersion + ") **");
            do {
                System.out.println("name: " + c.getString(1) + " type: " + c.getString(2));
            } while (c.moveToNext());
        }
        c.close();
    }
}
