/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.After;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import java.util.HashSet;
import java.util.Set;

import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.DB_SCHEMA;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.DB_VERSION;
import static com.philips.cdp.dicommclient.networknode.NetworkNodeDatabaseHelper.TABLE_NETWORK_NODE;
import static junit.framework.Assert.assertEquals;

public class NetworkNodeDatabaseHelperTest extends RobolectricTest {
    private NetworkNodeDatabaseHelper networkNodeDatabaseHelper;

    String version1 = "CREATE TABLE IF NOT EXISTS network_node("
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
            + "PRIMARY KEY(_id)"
            + ");";

    String version2 = "CREATE TABLE IF NOT EXISTS network_node("
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

    String version3 = "CREATE TABLE IF NOT EXISTS network_node("
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
            + "PRIMARY KEY(_id)"
            + ");";

    String version4 = "CREATE TABLE IF NOT EXISTS network_node("
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

    String version5 = "CREATE TABLE IF NOT EXISTS network_node("
            + "_id INTEGER NOT NULL UNIQUE,"
            + "cppid TEXT UNIQUE,"
            + "bootid NUMERIC,"
            + "encryption_key TEXT,"
            + "dev_name TEXT,"
            + "lastknown_network TEXT,"
            + "is_paired SMALLINT NOT NULL DEFAULT 0,"
            + "last_paired NUMERIC,"
            + "ip_address TEXT,"
            + "device_type TEXT,"
            + "model_id TEXT,"
            + "https SMALLINT NOT NULL DEFAULT 0,"
            + "pin TEXT,"
            + "PRIMARY KEY(_id)"
            + ");";

    String version6 = "CREATE TABLE IF NOT EXISTS network_node("
            + "_id INTEGER NOT NULL UNIQUE,"
            + "cppid TEXT UNIQUE,"
            + "bootid NUMERIC,"
            + "encryption_key TEXT,"
            + "dev_name TEXT,"
            + "lastknown_network TEXT,"
            + "is_paired SMALLINT NOT NULL DEFAULT 0,"
            + "last_paired NUMERIC,"
            + "ip_address TEXT,"
            + "device_type TEXT,"
            + "model_id TEXT,"
            + "https SMALLINT NOT NULL DEFAULT 0,"
            + "pin TEXT,"
            + "mismatched_pin TEXT,"
            + "PRIMARY KEY(_id)"
            + ");";

    @Override
    @After
    public void tearDown() throws Exception {
        if (networkNodeDatabaseHelper == null) {
            networkNodeDatabaseHelper.close();
        }
    }

    @Test
    public void whenStartingFromDatabaseVersion1_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(1, version1);
    }

    @Test
    public void whenStartingFromDatabaseVersion2_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(2, version2);
    }

    @Test
    public void whenStartingFromDatabaseVersion3_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(3, version3);
    }

    @Test
    public void whenStartingFromDatabaseVersion4_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(4, version4);
    }

    @Test
    public void whenStartingFromDatabaseVersion5_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(5, version5);
    }

    @Test
    public void whenStartingFromDatabaseVersion6_AndUpgrade_ThenDatabaseStructureShouldBeCorrect() throws Exception {
        verifyDatabaseUpgrade(6, version6);
    }

    private void verifyDatabaseUpgrade(int version, String networkNodeTableStructure) {
        networkNodeDatabaseHelper = new NetworkNodeDatabaseHelper(RuntimeEnvironment.application, version) {
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

        networkNodeDatabaseHelper.onUpgrade(database, version, DB_VERSION);

        Set<String> columnNames = getColumns(database);
        assertEquals(DB_SCHEMA, columnNames);
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