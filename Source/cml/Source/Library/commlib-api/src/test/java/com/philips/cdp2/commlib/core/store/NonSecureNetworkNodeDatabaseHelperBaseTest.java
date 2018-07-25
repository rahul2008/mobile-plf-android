/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
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
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_KNOWN_NETWORK;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MAC_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MISMATCHED_PIN;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE;

abstract public class NonSecureNetworkNodeDatabaseHelperBaseTest extends RobolectricTest {

    static final int VERSION_1 = 1;
    static final int VERSION_2 = 2;
    static final int VERSION_3 = 3;
    static final int VERSION_4 = 4;
    static final int VERSION_5 = 5;
    static final int VERSION_6 = 6;
    static final int VERSION_7 = 7;

    static final String CPP_ID = "ccp";
    static final long BOOT_ID = 1337L;
    static final String ENCRYPTION_KEY = "encryption key";
    static final String DEVICE_NAME = "device name";
    static final String NETWORK_NAME = "network name";
    static final int PAIRED_STATE = 2;
    static final long LAST_PAIRED_TIMESTAMP = -1L;
    static final String IP_ADDRESS = "IP Address";
    static final String MODEL_NAME = "model name";
    static final int HTTPS = 1;

    static final String MODEL_ID = "model Id";
    static final String PIN = "pin";
    static final String TYPE = "type";
    static final String MISMATCHED_PIN = "mismatched pin";
    static final String MAC_ADDRESS = "00:11:22:33:44:55";
    static final String SSID = "ssid";

    NonSecureNetworkNodeDatabaseHelper networkNodeDatabaseHelper;

    @Override
    @After
    public void tearDown() {
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

    @NonNull
    SQLiteDatabase prepareSqliteDatabase(final int oldVersion, String networkNodeTableStructure) {
        networkNodeDatabaseHelper = new NonSecureNetworkNodeDatabaseHelper(RuntimeEnvironment.application, oldVersion);
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
    Set<String> getColumns(SQLiteDatabase database) {
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

    @NonNull
    ContentValues createContentValues(int version) {
        ContentValues data = new ContentValues();
        data.put(KEY_CPP_ID, CPP_ID);
        data.put(KEY_BOOT_ID, BOOT_ID);
        data.put(KEY_ENCRYPTION_KEY, ENCRYPTION_KEY);
        data.put(KEY_DEVICE_NAME, DEVICE_NAME);
        data.put(KEY_LAST_KNOWN_NETWORK, NETWORK_NAME);
        data.put(KEY_IS_PAIRED, PAIRED_STATE);
        data.put(KEY_LAST_PAIRED, LAST_PAIRED_TIMESTAMP);
        data.put(KEY_IP_ADDRESS, IP_ADDRESS);

        if (version >= VERSION_2) {
            data.put(KEY_HTTPS, HTTPS);
        }

        if (version >= VERSION_3) {
            data.put(KEY_MODEL_ID, MODEL_ID);
        } else {
            data.put("model_type", MODEL_ID);
        }

        if (version >= VERSION_4) {
            data.put(KEY_PIN, PIN);
        }

        if (version >= VERSION_5) {
            data.put(KEY_DEVICE_TYPE, TYPE);
        } else {
            data.put(KEY_MODEL_NAME, MODEL_NAME);
        }

        if (version >= VERSION_6) {
            data.put(KEY_MISMATCHED_PIN, MISMATCHED_PIN);
        }

        if (version >= VERSION_7) {
            data.put(KEY_MAC_ADDRESS, MAC_ADDRESS);
        }
        return data;
    }
}
