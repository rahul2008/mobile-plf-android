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
import android.util.Log;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.ContextProvider;

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

class NonSecureNetworkNodeDatabaseHelper extends SQLiteOpenHelper implements DatabaseHelper {

    static final int DB_VERSION = 6;

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
    }};

    static final String DB_NAME = "network_node.db";
    static final String TABLE_NETWORK_NODE = "network_node";

    NonSecureNetworkNodeDatabaseHelper() {
        this(ContextProvider.get(), DB_VERSION);
    }

    @VisibleForTesting
    NonSecureNetworkNodeDatabaseHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DICommLog.w(DICommLog.DATABASE, "Create table " + TABLE_NETWORK_NODE);

        String createNetworkNodeTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NETWORK_NODE + "("
                + KEY_ID + " INTEGER NOT NULL UNIQUE,"
                + KEY_CPP_ID + " TEXT UNIQUE,"
                + KEY_BOOT_ID + " NUMERIC,"
                + KEY_ENCRYPTION_KEY + " TEXT,"
                + KEY_DEVICE_NAME + " TEXT,"
                + KEY_LAST_KNOWN_NETWORK + " TEXT,"
                + KEY_IS_PAIRED + " SMALLINT NOT NULL DEFAULT 0,"
                + KEY_LAST_PAIRED + " NUMERIC,"
                + KEY_IP_ADDRESS + " TEXT,"
                + KEY_DEVICE_TYPE + " TEXT,"
                + KEY_MODEL_ID + " TEXT,"
                + KEY_HTTPS + " SMALLINT NOT NULL DEFAULT 0,"
                + KEY_PIN + " TEXT,"
                + KEY_MISMATCHED_PIN + " TEXT,"
                + "PRIMARY KEY(" + KEY_ID + ")"
                + ");";

        try {
            db.execSQL(createNetworkNodeTable);
        } catch (SQLException e) {
            Log.e(DICommLog.DATABASE, "Table creation error", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        for (int currentVersion = oldVersion; currentVersion < newVersion; currentVersion++) {
            onBeforeUpgrade(db, currentVersion);

            switch (currentVersion + 1) {
                case 2:
                    upgradeToVersion2(db);
                    break;
                case 3:
                    upgradeToVersion3(db);
                    break;
                case 4:
                    upgradeToVersion4(db);
                    break;
                case 5:
                    upgradeToVersion5(db);
                    break;
                case 6:
                    upgradeToVersion6(db);
                    break;
                default:
                    DICommLog.e(DICommLog.DATABASE, "Table creation error");
                    break;
            }
        }
    }

    @VisibleForTesting
    void onBeforeUpgrade(SQLiteDatabase db, int currentVersion) {
    }

    private void upgradeToVersion2(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_HTTPS + " SMALLINT NOT NULL DEFAULT 0;");
    }

    /**
     * This will rename the 'model_type' column to 'model_id'.
     *
     * @param db the database to perform the upgrade on
     */
    private void upgradeToVersion3(SQLiteDatabase db) {
        db.execSQL("BEGIN TRANSACTION;");

        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " RENAME TO tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("CREATE TABLE IF NOT EXISTS network_node("
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
                + ");");

        db.execSQL("INSERT INTO " + TABLE_NETWORK_NODE + "(" + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_MODEL_NAME + "," + KEY_MODEL_ID + "," + KEY_HTTPS + ")\n" +
                "SELECT " + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_MODEL_NAME + ",model_type," + KEY_HTTPS + "\n" +
                "FROM tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("DROP TABLE tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("COMMIT;");
    }

    /**
     * This will add the 'pin' column.
     *
     * @param db the database to perform the upgrade on
     */
    private void upgradeToVersion4(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_PIN + " TEXT;");
    }

    /**
     * This will rename the 'model_name' column to 'device_type'.
     *
     * @param db the database to perform the upgrade on
     */
    private void upgradeToVersion5(SQLiteDatabase db) {
        db.execSQL("BEGIN TRANSACTION;");

        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " RENAME TO tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("CREATE TABLE IF NOT EXISTS network_node("
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
                + ");");

        db.execSQL("INSERT INTO " + TABLE_NETWORK_NODE + "(" + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_DEVICE_TYPE + "," + KEY_MODEL_ID + "," + KEY_HTTPS + ")\n" +

                "SELECT " + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                KEY_DEVICE_NAME + "," + KEY_LAST_KNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + ",model_name," + KEY_MODEL_ID + "," + KEY_HTTPS + "\n" +

                "FROM tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("DROP TABLE tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("COMMIT;");
    }

    private void upgradeToVersion6(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_MISMATCHED_PIN + " TEXT;");
    }

    @Override
    public Cursor query(String selection, String[] selectionArgs) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NETWORK_NODE, null, null, null, null, null, null);
    }

    @Override
    public long insertRow(ContentValues values) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insertWithOnConflict(TABLE_NETWORK_NODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public int delete(String selection) throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NETWORK_NODE, KEY_CPP_ID + "= ?", new String[]{selection});
    }
}
