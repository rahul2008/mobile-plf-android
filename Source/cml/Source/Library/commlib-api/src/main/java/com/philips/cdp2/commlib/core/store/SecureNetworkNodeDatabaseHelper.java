package com.philips.cdp2.commlib.core.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import com.j256.ormlite.support.ConnectionSource;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.util.ContextProvider;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;
import com.philips.platform.securedblibrary.SqlLiteInitializer;
import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;

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

class SecureNetworkNodeDatabaseHelper extends SecureDbOrmLiteSqliteOpenHelper<NetworkNode> implements NetworkNodeDBHelper {

    private static final String DB_NAME = "secure_network_node.db";
    private static final String TABLE_NETWORK_NODE = "secure_network_node";
    private static final String DB_KEY = "secure_network_node";

    private static final int DB_VERSION = 2;

    SecureNetworkNodeDatabaseHelper(@NonNull final AppInfraInterface appInfra) {
        super(ContextProvider.get(), appInfra, DB_NAME, null, DB_VERSION, DB_KEY);
    }

    @VisibleForTesting
    SecureNetworkNodeDatabaseHelper(@NonNull final AppInfraInterface appInfra, @NonNull final SqlLiteInitializer sqlLiteInitializer) {
        super(ContextProvider.get(), appInfra, DB_NAME, null, DB_VERSION, DB_KEY, sqlLiteInitializer);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        String createNetworkNodeTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NETWORK_NODE + "("
                + KEY_ID + " INTEGER NOT NULL UNIQUE,"
                + KEY_CPP_ID + " TEXT UNIQUE,"
                + KEY_MAC_ADDRESS + " TEXT,"
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

        database.execSQL(createNetworkNodeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            database.rawExecSQL(
                    "ALTER TABLE " + TABLE_NETWORK_NODE + " ADD COLUMN " +
                            KEY_MAC_ADDRESS + " STRING NULL");

            database.rawExecSQL(
                    "UPDATE " + TABLE_NETWORK_NODE + " SET " +
                            KEY_MAC_ADDRESS + " = " + KEY_CPP_ID);
        }
    }

    @Override
    public Cursor query(String selection, String[] selectionArgs) {
        try {
            SQLiteDatabase db = this.getWriteDbPermission();
            return db.query(TABLE_NETWORK_NODE, null, selection, selectionArgs, null, null, null);
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    @Override
    public long insertRow(ContentValues values) {
        try {
            SQLiteDatabase db = this.getWriteDbPermission();
            return db.insertWithOnConflict(TABLE_NETWORK_NODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    @Override
    public int deleteNetworkNodeWithCppId(String cppId) {
        try {
            SQLiteDatabase db = this.getWriteDbPermission();
            return db.delete(TABLE_NETWORK_NODE, KEY_CPP_ID + "= ?", new String[]{cppId});
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }
}
