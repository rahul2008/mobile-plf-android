/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import org.junit.Test;

import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.DB_VERSION;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_BOOT_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_CPP_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_DEVICE_NAME;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_DEVICE_TYPE;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_ENCRYPTION_KEY;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_HTTPS;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_IP_ADDRESS;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_IS_PAIRED;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_LAST_KNOWN_NETWORK;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_LAST_PAIRED;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_MISMATCHED_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_MODEL_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_MODEL_NAME;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.KEY_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseHelper.TABLE_NETWORK_NODE;
import static org.junit.Assert.assertEquals;

public class NetworkNodeDatabaseHelperVersion4Test extends NetworkNodeDatabaseHelperBaseTest {

    private static final int OLD_VERSION = 4;
    private String version4 = "CREATE TABLE IF NOT EXISTS network_node("
            + "_id INTEGER NOT NULL UNIQUE,"
            + "cppid TEXT UNIQUE,"
            + "bootid NUMERIC,"
            + "encryption_key TEXT,"
            + "dev_name TEXT,"
            + "lastknown_network TEXT,"
            + "is_paired SMALLINT NOT NULL DEFAULT 0,"
            + "last_paired NUMERIC,"
            + "ip_address TEXT,"
            + "model_name TEXT,"
            + "model_id TEXT,"
            + "https SMALLINT NOT NULL DEFAULT 0,"
            + "pin TEXT,"
            + "PRIMARY KEY(_id)"
            + ");";

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(4, version4);
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_cppId() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String cppId = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
        assertEquals("Some Cpp Id", cppId);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_bootId() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
        assertEquals(1337L, bootId);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_encryptionKey() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
        assertEquals("Some Encryption Key", encryptionKey);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_name() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
        assertEquals("Some Device Name", name);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_lastKnownNetwork() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LAST_KNOWN_NETWORK));
        assertEquals("Some Network Name", lastKnownNetwork);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_isPaired() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
            assertEquals(2, pairedStatus);
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_lastPairedTime() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
        assertEquals(-1L, lastPairedTime);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_ipAddress() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
        assertEquals("Some IP Address", ipAddress);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_deviceType() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String deviceType = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_TYPE));
        assertEquals("Some Model Name", deviceType);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_modelId() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String modelId = cursor.getString(cursor.getColumnIndex(KEY_MODEL_ID));
        assertEquals("Some Model Id", modelId);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_https() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        short https = cursor.getShort(cursor.getColumnIndex(KEY_HTTPS));
        assertEquals(1, https);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_pin() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));
        assertEquals(null, pin);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDataShouldBeCorrect_mismatchedPin() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(OLD_VERSION, version4);

        ContentValues data = createContentValues();

        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, OLD_VERSION, DB_VERSION);

        final SQLiteDatabase upgradedDatabase =  networkNodeDatabaseHelper.getReadableDatabase();
        Cursor cursor = upgradedDatabase.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
        cursor.moveToFirst();

        String mismatchedPin = cursor.getString(cursor.getColumnIndex(KEY_MISMATCHED_PIN));
        assertEquals(null, mismatchedPin);

        if(!cursor.isClosed()) {
            cursor.close();
        }
    }

    @NonNull
    private ContentValues createContentValues() {
        ContentValues data = new ContentValues();
        data.put(KEY_CPP_ID, "Some Cpp Id");
        data.put(KEY_BOOT_ID, 1337L);
        data.put(KEY_ENCRYPTION_KEY, "Some Encryption Key");
        data.put(KEY_DEVICE_NAME, "Some Device Name");
        data.put(KEY_LAST_KNOWN_NETWORK, "Some Network Name");
        data.put(KEY_IS_PAIRED, 2);
        data.put(KEY_LAST_PAIRED, -1L);
        data.put(KEY_IP_ADDRESS, "Some IP Address");
        data.put(KEY_MODEL_NAME, "Some Model Name");
        data.put(KEY_MODEL_ID, "Some Model Id");
        data.put(KEY_HTTPS, 1);
        return data;
    }
}