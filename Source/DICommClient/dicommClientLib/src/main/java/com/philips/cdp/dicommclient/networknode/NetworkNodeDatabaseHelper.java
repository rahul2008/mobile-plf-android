/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.networknode;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.philips.cdp.dicommclient.util.DICommLog;

public class NetworkNodeDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;

    // NetworkNode table
    public static final String DB_NAME = "network_node.db";
    public static final String TABLE_NETWORK_NODE = "network_node";
    public static final String KEY_ID = "_id";
    public static final String KEY_CPP_ID = "cppid";
    public static final String KEY_BOOT_ID = "bootid";
    public static final String KEY_ENCRYPTION_KEY = "encryption_key"; //TODO was airpur_key
    public static final String KEY_DEVICE_NAME = "dev_name";
    public static final String KEY_LASTKNOWN_NETWORK = "lastknown_network";
    public static final String KEY_IS_PAIRED = "is_paired";
    public static final String KEY_LAST_PAIRED = "last_paired";
    public static final String KEY_IP_ADDRESS = "ip_address";
    public static final String KEY_DEVICE_TYPE = "model_name"; // Value explicitly not renamed to prevent schema update
    public static final String KEY_MODEL_ID = "model_id";
    public static final String KEY_HTTPS = "https";

    public NetworkNodeDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public NetworkNodeDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
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
                + KEY_LASTKNOWN_NETWORK + " TEXT,"
                + KEY_IS_PAIRED + " SMALLINT NOT NULL DEFAULT 0,"
                + KEY_LAST_PAIRED + " NUMERIC,"
                + KEY_IP_ADDRESS + " TEXT,"
                + KEY_DEVICE_TYPE + " TEXT,"
                + KEY_MODEL_ID + " TEXT,"
                + KEY_HTTPS + " SMALLINT NOT NULL DEFAULT 0,"
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
            switch (currentVersion + 1) {
                case 2:
                    upgradeToVersion2(db);
                    break;
                case 3:
                    upgradeToVersion3(db);
                    break;
                default:
                    DICommLog.e(DICommLog.DATABASE, "Table creation error");
                    break;
            }
        }
    }

    private void upgradeToVersion2(SQLiteDatabase db) {
        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " + KEY_HTTPS + " SMALLINT NOT NULL DEFAULT 0;");
    }

    private void upgradeToVersion3(SQLiteDatabase db) {
        db.execSQL("BEGIN TRANSACTION;");

        db.execSQL("ALTER TABLE " + TABLE_NETWORK_NODE + " RENAME TO tmp_" + TABLE_NETWORK_NODE + ";");

        onCreate(db); // This will recreate the original table

        db.execSQL("INSERT INTO " + TABLE_NETWORK_NODE + "(" + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                KEY_DEVICE_NAME + "," + KEY_LASTKNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_DEVICE_TYPE + "," + KEY_MODEL_ID + "," + KEY_HTTPS + ")\n" +
                "SELECT " + KEY_ID + "," + KEY_CPP_ID + "," + KEY_BOOT_ID + "," + KEY_ENCRYPTION_KEY + "," +
                KEY_DEVICE_NAME + "," + KEY_LASTKNOWN_NETWORK + "," + KEY_IS_PAIRED + "," + KEY_LAST_PAIRED + "," + KEY_IP_ADDRESS + "," + KEY_DEVICE_TYPE + ",model_type," + KEY_HTTPS + "\n" +
                "FROM tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("DROP TABLE tmp_" + TABLE_NETWORK_NODE + ";");

        db.execSQL("COMMIT;");
    }
}
