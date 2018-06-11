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
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MISMATCHED_PIN;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.DB_VERSION;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE;
import static com.philips.cdp2.commlib.core.store.NonSecureNetworkNodeDatabaseHelperVersion1Test.VERSION_1_CREATE_QUERY;
import static org.junit.Assert.assertEquals;

public class NonSecureNetworkNodeDatabaseHelperVersion2Test extends NonSecureNetworkNodeDatabaseHelperBaseTest {

    private static final Set<String> DB_SCHEMA = new HashSet<String>() {{
        add(KEY_BOOT_ID);
        add(KEY_CPP_ID);
        add(KEY_DEVICE_NAME);
        add(KEY_MODEL_NAME);
        add(KEY_ENCRYPTION_KEY);
        add(KEY_HTTPS);
        add(KEY_ID);
        add(KEY_IP_ADDRESS);
        add(KEY_IS_PAIRED);
        add(KEY_LAST_KNOWN_NETWORK);
        add(KEY_LAST_PAIRED);
        add("model_type");
    }};

    private static final int VERSION = 2;
    private String VERSION_2_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS network_node("
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
            + "model_type TEXT,"
            + "https SMALLINT NOT NULL DEFAULT 0,"
            + "PRIMARY KEY(_id)"
            + ");";

    @Test
    public void givenVersionIs1_whenUpgradingToVersion2_thenDatabaseStructureShouldBeCorrect() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(1, VERSION_1_CREATE_QUERY);

        networkNodeDatabaseHelper.onUpgrade(database, 1, VERSION);

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_cppId() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String cppId = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
        assertEquals("Some Cpp Id", cppId);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_bootId() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
        assertEquals(1337L, bootId);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_encryptionKey() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
        assertEquals("Some Encryption Key", encryptionKey);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_name() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
        assertEquals("Some Device Name", name);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_lastKnownNetwork() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LAST_KNOWN_NETWORK));
        assertEquals("Some Network Name", lastKnownNetwork);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_isPaired() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
        assertEquals(2, pairedStatus);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_lastPairedTime() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
        assertEquals(-1L, lastPairedTime);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_ipAddress() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
        assertEquals("Some IP Address", ipAddress);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_deviceType() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String deviceType = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_TYPE));
        assertEquals("Some Model Name", deviceType);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_modelId() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String modelId = cursor.getString(cursor.getColumnIndex(KEY_MODEL_ID));
        assertEquals("Some Model Type", modelId);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_https() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        short https = cursor.getShort(cursor.getColumnIndex(KEY_HTTPS));
        assertEquals(1, https);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_pin() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));
        assertEquals(null, pin);

        closeCursor(cursor);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDataShouldBeCorrect_mismatchedPin() throws Exception {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION, VERSION_2_CREATE_QUERY);
        ContentValues data = createOldContentValues();
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION, DB_VERSION);

        Cursor cursor = getReadableDatabaseCursor();
        String mismatchedPin = cursor.getString(cursor.getColumnIndex(KEY_MISMATCHED_PIN));
        assertEquals(null, mismatchedPin);

        closeCursor(cursor);
    }

    @NonNull
    private ContentValues createOldContentValues() {
        ContentValues data = new ContentValues();
        data.put(KEY_CPP_ID, "Some Cpp Id");
        data.put(KEY_BOOT_ID, 1337L);
        data.put(KEY_ENCRYPTION_KEY, "Some Encryption Key");
        data.put(KEY_DEVICE_NAME, "Some Device Name");
        data.put(KEY_LAST_KNOWN_NETWORK, "Some Network Name");
        data.put(KEY_IS_PAIRED, 2);
        data.put(KEY_LAST_PAIRED, -1L);
        data.put(KEY_IP_ADDRESS, "Some IP Address");
        data.put("model_type", "Some Model Type");
        data.put(KEY_MODEL_NAME, "Some Model Name");
        data.put(KEY_HTTPS, 1);
        return data;
    }
}