package com.philips.platform.database;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;

public class SecureDataBaseHelper<T> extends SecureDbOrmLiteSqliteOpenHelper {
    private  Class tableName;
    Context context;



    public SecureDataBaseHelper(Context context, AppInfraInterface mAppInfraInterface, Class tableName, String dataBaseName, int databaseVersion, String databaseKey)  {
        super(context, mAppInfraInterface,dataBaseName, null, databaseVersion, databaseKey);
        this.tableName=tableName;
        this.context=context;

    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource source) {
        createTables(source);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource source, int oldVersion, int newVersion) {
        dropTables(source);
        createTables(source);
    }


    private void createTables(ConnectionSource source) {
        try {
            TableUtils.createTable(source, tableName);
        } catch (SQLException e) {
            Log.d("SecureDataBaseHelper","Error in Create Table"+e.getMessage());
        }
    }

    private void dropTables(ConnectionSource source) {
        try {
            TableUtils.dropTable(source, tableName, true);
        } catch (SQLException e) {
            Log.d("SecureDataBaseHelper","Error in Delete or Drop Table"+e.getMessage());
        }

    }


    public String getAppVersion()
    {
      return getSecureDbAppVersion();
    }




}
