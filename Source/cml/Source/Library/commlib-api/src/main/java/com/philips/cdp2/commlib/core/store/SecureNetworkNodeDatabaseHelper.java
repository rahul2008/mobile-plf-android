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

import static com.philips.cdp.dicommclient.networknode.NetworkNode.KEY_CPP_ID;
import static com.philips.cdp2.commlib.core.store.NetworkNodeDatabaseSchema.DB_VERSION;

class SecureNetworkNodeDatabaseHelper extends SecureDbOrmLiteSqliteOpenHelper<NetworkNode> implements DatabaseHelper {

    private static final String DB_NAME = "secure_network_node.db";
    private static final String DB_KEY = "secure_network_node";

    SecureNetworkNodeDatabaseHelper(@NonNull final AppInfraInterface appInfra) {
        super(ContextProvider.get(), appInfra, DB_NAME, null, DB_VERSION, DB_KEY);
    }

    @VisibleForTesting
    SecureNetworkNodeDatabaseHelper(@NonNull final AppInfraInterface appInfra, @NonNull final SqlLiteInitializer sqlLiteInitializer) {
        super(ContextProvider.get(), appInfra, DB_NAME, null, DB_VERSION, DB_KEY, sqlLiteInitializer);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        database.execSQL(NetworkNodeDatabaseSchema.CREATE_NETWORK_NODE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        final String[] upgradeQuery = NetworkNodeDatabaseSchema.getUpgradeQuery(oldVersion, newVersion);

        for (String query : upgradeQuery) {
            // todo: raw vs exec
            database.rawExecSQL(query);
        }
    }

    @Override
    public Cursor query(String selection, String[] selectionArgs) {
        try {
            SQLiteDatabase db = this.getWriteDbPermission();
            return db.query(NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE, null, selection, selectionArgs, null, null, null);
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    @Override
    public long insertRow(ContentValues values) {
        try {
            SQLiteDatabase db = this.getWriteDbPermission();
            return db.insertWithOnConflict(NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }

    @Override
    public int delete(String selection) {
        try {
            SQLiteDatabase db = this.getWriteDbPermission();
            return db.delete(NetworkNodeDatabaseSchema.TABLE_NETWORK_NODE, KEY_CPP_ID + "= ?", new String[]{selection});
        } catch (SQLException e) {
            throw new android.database.SQLException(e.getMessage());
        }
    }
}
