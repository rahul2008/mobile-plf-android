/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_BOOT_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_DEVICE_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ENCRYPTION_KEY;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_HTTPS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IP_ADDRESS;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_IS_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_KNOWN_NETWORK;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_LAST_PAIRED;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_ID;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_MODEL_NAME;
import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_PIN;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE;
import static com.philips.cdp2.commlib.core.store.NonSecureNetworkNodeDatabaseHelperVersion1Test.VERSION_1_CREATE_QUERY;
import static com.philips.cdp2.commlib.core.store.NonSecureNetworkNodeDatabaseHelperVersion2Test.VERSION_2_CREATE_QUERY;
import static com.philips.cdp2.commlib.core.store.NonSecureNetworkNodeDatabaseHelperVersion3Test.VERSION_3_CREATE_QUERY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NonSecureNetworkNodeDatabaseHelperVersion4Test extends NonSecureNetworkNodeDatabaseHelperBaseTest {

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
        add(KEY_MODEL_ID);
        add(KEY_PIN);
    }};

    static String VERSION_4_CREATE_QUERY = "CREATE TABLE IF NOT EXISTS network_node("
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
    public void whenDatabaseIsCreatedOfVersion4_thenAllColumnsAreCreated() {
        prepareSqliteDatabase(VERSION_4, VERSION_4_CREATE_QUERY);

        final SQLiteDatabase database = networkNodeDatabaseHelper.getReadableDatabase();

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_thenDatabaseStructureShouldBeCorrect() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
    }

    @Test
    public void givenVersionIs2_whenUpgradingToVersion4_thenDatabaseStructureShouldBeCorrect() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_2, VERSION_2_CREATE_QUERY);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_2, VERSION_4);

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
    }

    @Test
    public void givenVersionIs3_whenUpgradingToVersion4_thenDatabaseStructureShouldBeCorrect() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_3, VERSION_3_CREATE_QUERY);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_3, VERSION_4);

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_cppId() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String cppId = cursor.getString(cursor.getColumnIndex(KEY_CPP_ID));
        assertEquals(CPP_ID, cppId);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_bootId() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        long bootId = cursor.getLong(cursor.getColumnIndex(KEY_BOOT_ID));
        assertEquals(BOOT_ID, bootId);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_encryptionKey() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String encryptionKey = cursor.getString(cursor.getColumnIndex(KEY_ENCRYPTION_KEY));
        assertEquals(ENCRYPTION_KEY, encryptionKey);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_name() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String name = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_NAME));
        assertEquals(DEVICE_NAME, name);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_lastKnownNetwork() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String lastKnownNetwork = cursor.getString(cursor.getColumnIndex(KEY_LAST_KNOWN_NETWORK));
        assertEquals(NETWORK_NAME, lastKnownNetwork);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_isPaired() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        int pairedStatus = cursor.getInt(cursor.getColumnIndex(KEY_IS_PAIRED));
        assertEquals(2, pairedStatus);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_lastPairedTime() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        long lastPairedTime = cursor.getLong(cursor.getColumnIndexOrThrow(KEY_LAST_PAIRED));
        assertEquals(-1L, lastPairedTime);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_ipAddress() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String ipAddress = cursor.getString(cursor.getColumnIndex(KEY_IP_ADDRESS));
        assertEquals(IP_ADDRESS, ipAddress);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_https() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        short https = cursor.getShort(cursor.getColumnIndex(KEY_HTTPS));
        assertEquals(0, https);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs2_whenUpgradingToVersion4_ThenDataShouldBeCorrect_https() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_2, VERSION_2_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_2);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_2, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        short https = cursor.getShort(cursor.getColumnIndex(KEY_HTTPS));
        assertEquals(1, https);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenDataShouldBeCorrect_modelId() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String modelId = cursor.getString(cursor.getColumnIndex(KEY_MODEL_ID));
        assertEquals(MODEL_ID, modelId);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs2_whenUpgradingToVersion4_ThenDataShouldBeCorrect_modelId() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_2, VERSION_2_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_2);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_2, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String modelId = cursor.getString(cursor.getColumnIndex(KEY_MODEL_ID));
        assertEquals(MODEL_ID, modelId);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs1_whenUpgradingToVersion4_ThenPinShouldHaveDefaultValue() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_1, VERSION_1_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_1);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_1, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));
        assertNull(pin);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs2_whenUpgradingToVersion4_ThenPinShouldHaveDefaultValue() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_2, VERSION_2_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_2);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_2, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));
        assertNull(pin);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs3_whenUpgradingToVersion4_ThenPinShouldHaveDefaultValue() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_3, VERSION_3_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_3);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        networkNodeDatabaseHelper.onUpgrade(database, VERSION_3, VERSION_4);

        Cursor cursor = getReadableDatabaseCursor();
        String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));
        assertNull(pin);

        closeCursor(cursor);
    }

    @Test
    public void givenVersionIs4_whenStoringPin_ThenDataHasCorrectPin() {
        final SQLiteDatabase database = prepareSqliteDatabase(VERSION_4, VERSION_4_CREATE_QUERY);
        ContentValues data = createContentValues(VERSION_4);
        database.insertWithOnConflict(TABLE_NETWORK_NODE, null, data, SQLiteDatabase.CONFLICT_REPLACE);

        Cursor cursor = getReadableDatabaseCursor();
        String pin = cursor.getString(cursor.getColumnIndex(KEY_PIN));
        assertEquals(PIN, pin);

        closeCursor(cursor);
    }
}