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

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_TYPE;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_HTTPS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_KNOWN_NETWORK;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MAC_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MISMATCHED_PIN;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.DB_VERSION;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE;
import static org.junit.Assert.assertEquals;

abstract public class NonSecureNetworkNodeDatabaseHelperBaseTest extends RobolectricTest {

    static final Set<String> DB_SCHEMA = new HashSet<String>() {{
        add(KEY_BOOT_ID);
        add(KEY_CPP_ID);
        add(KEY_DEVICE_NAME);
        add(KEY_DEVICE_TYPE);
        add(KEY_ENCRYPTION_KEY);
        add(KEY_HTTPS);
        add(KEY_ID);
        add(KEY_IP_ADDRESS);
        add(KEY_IS_PAIRED);
        add(KEY_LAST_KNOWN_NETWORK);
        add(KEY_LAST_PAIRED);
        add(KEY_MODEL_ID);
        add(KEY_PIN);
        add(KEY_MISMATCHED_PIN);
        add(KEY_MAC_ADDRESS);
    }};

    NonSecureNetworkNodeDatabaseHelper networkNodeDatabaseHelper;

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
        networkNodeDatabaseHelper = new NonSecureNetworkNodeDatabaseHelper(RuntimeEnvironment.application, oldVersion) {
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
