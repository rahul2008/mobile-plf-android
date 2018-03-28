package com.philips.platform.securedblibrary;

import android.content.Context;
import android.content.SharedPreferences;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.IOUtils;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.DatabaseTableConfigLoader;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.securedblibrary.ormlite.sqlcipher.android.AndroidConnectionSource;
import com.philips.platform.securedblibrary.ormlite.sqlcipher.android.AndroidDatabaseConnection;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase.CursorFactory;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.sql.SQLException;

import static android.content.Context.MODE_PRIVATE;

/**
 * SQLite database open helper which can be extended by your application to help manage when the application needs to
 * create or upgrade its database.
 * @since 1.0.0
 */
public abstract class SecureDbOrmLiteSqliteOpenHelper<T> extends SQLiteOpenHelper {

    protected AndroidConnectionSource connectionSource;
    protected boolean cancelQueriesEnabled;
    SecureStorageInterface mSecureStorage = null;
    private AppInfraInterface mAppInfraInterface;
    private volatile boolean isOpen = true;
    private String databaseKey;
    private Context context;
    private char[] key;
    private LoggingInterface appInfraLogger;

    /**
     * Constructor of SecureDbOrmLiteSqliteOpenHelper.
     * @param context         Associated content from the application. This is needed to locate the database.
     * @param dataBaseName    Name of the database we are opening.
     * @param factory         Cursor factory or null if none.
     * @param databaseVersion Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
     *                        called if the stored database is a different version.
     * @since 1.0.0
     */
    public SecureDbOrmLiteSqliteOpenHelper(Context context, AppInfraInterface mAppInfraInterface, String dataBaseName, CursorFactory factory, int databaseVersion, String databaseKey) {
        this(context, mAppInfraInterface, dataBaseName, factory, databaseVersion, databaseKey, new DefaultSqlLiteInitializer());
    }

    public SecureDbOrmLiteSqliteOpenHelper(Context context, AppInfraInterface mAppInfraInterface, String dataBaseName, CursorFactory factory, int databaseVersion, String databaseKey, SqlLiteInitializer initializer) {
        super(context, dataBaseName, factory, databaseVersion);
        this.context = context;
        initializer.loadLibs(context);
        this.mAppInfraInterface = mAppInfraInterface;
        this.databaseKey = databaseKey;
        appInfraLogger = mAppInfraInterface.getLogging().createInstanceForComponent("sdb:",
                getSecureDbAppVersion());
        createKey();
        key = getKeyValue(databaseKey, mAppInfraInterface);
        connectionSource = new AndroidConnectionSource(this, key, appInfraLogger);
        getSecureDBLogInstance().log(LoggingInterface.LogLevel.DEBUG, SecureDBLogEventID.SDB_ORMLITE_SQLITE_OPEN_HELPER, "{}: constructed connectionSource {}");


    }

    /**
     * Same as the other constructor with the addition of a file-id of the table config-file. See
     * {@link OrmLiteConfigUtil} for details.
     *
     * @param context         Associated content from the application. This is needed to locate the database.
     * @param databaseName    Name of the database we are opening.
     * @param factory         Cursor factory or null if none.
     * @param databaseVersion Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
     *                        called if the stored database is a different version.
     * @param configFileId    file-id which probably should be a R.raw.ormlite_config.txt or some static value.
     * @since 1.0.0
     */
    public SecureDbOrmLiteSqliteOpenHelper(Context context, AppInfraInterface mAppInfraInterface, String databaseName, CursorFactory factory, int databaseVersion,
                                           int configFileId, String password) {
        this(context, mAppInfraInterface, databaseName, factory, databaseVersion, openFileId(context, configFileId), password);
    }

    /**
     * Same as the other constructor with the addition of a config-file. See {@link OrmLiteConfigUtil} for details.
     *
     * @param context         Associated content from the application. This is needed to locate the database.
     * @param databaseName    Name of the database we are opening.
     * @param factory         Cursor factory or null if none.
     * @param databaseVersion Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
     *                        called if the stored database is a different version.
     * @param configFile      Configuration file to be loaded.
     * @since 1.0.0
     */
    public SecureDbOrmLiteSqliteOpenHelper(Context context, AppInfraInterface mAppInfraInterface, String databaseName, CursorFactory factory, int databaseVersion,
                                           File configFile, String password) {
        this(context, mAppInfraInterface, databaseName, factory, databaseVersion, openFile(configFile), password);

    }

    /**
     * Same as the other constructor with the addition of a input stream to the table config-file. See
     * {@link OrmLiteConfigUtil} for details.
     *
     * @param context         Associated content from the application. This is needed to locate the database.
     * @param databaseKey     Name of the database we are opening.
     * @param factory         Cursor factory or null if none.
     * @param databaseVersion Version of the database we are opening. This causes {@link #onUpgrade(SQLiteDatabase, int, int)} to be
     *                        called if the stored database is a different version.
     * @param stream          Stream opened to the configuration file to be loaded.
     * @since 1.0.0
     */
    public SecureDbOrmLiteSqliteOpenHelper(Context context, AppInfraInterface mAppInfraInterface, String dataBaseName, CursorFactory factory, int databaseVersion,
                                           InputStream stream, String databaseKey) {
        super(context, dataBaseName, factory, databaseVersion);
        this.context = context;
        SQLiteDatabase.loadLibs(context);
        this.mAppInfraInterface = mAppInfraInterface;
        this.databaseKey = databaseKey;
        appInfraLogger = mAppInfraInterface.getLogging().createInstanceForComponent("sdb:",
                getSecureDbAppVersion());
        createKey();
        key = getKeyValue(databaseKey, mAppInfraInterface);
        connectionSource = new AndroidConnectionSource(this, key, appInfraLogger);
        getSecureDBLogInstance().log(LoggingInterface.LogLevel.DEBUG, SecureDBLogEventID.SDB_ORMLITE_SQLITE_OPEN_HELPER, "{}: constructed connectionSource {}");

        if (stream == null) {
            return;
        }

        // if a config file-id was specified then load it into the DaoManager
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream), 4096);
            stream = null;
            DaoManager.addCachedDatabaseConfigs(DatabaseTableConfigLoader.loadDatabaseConfigFromReader(reader));
        } catch (SQLException e) {
            throw new IllegalStateException("Could not load object config file", e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(stream);
        }
    }

    private static InputStream openFileId(Context context, int fileId) {
        InputStream stream = context.getResources().openRawResource(fileId);
        if (stream == null) {
            throw new IllegalStateException("Could not find object config file with id " + fileId);
        }
        return stream;
    }

    private static InputStream openFile(File configFile) {
        try {
            if (configFile == null) {
                return null;
            } else {
                return new FileInputStream(configFile);
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not open config file " + configFile, e);
        }
    }

    private LoggingInterface getSecureDBLogInstance() {
        return appInfraLogger;
    }

    /**
     * What to do when your database needs to be created. Usually this entails creating the tables and loading any
     * initial data.
     * <p>
     * <p>
     * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method call or the one
     * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected results may result.
     * </p>
     *
     * @param database         Database being created.
     * @param connectionSource To use get connections to the database to be created.
     * @since 1.0.0
     */
    public abstract void onCreate(SQLiteDatabase database, ConnectionSource connectionSource);

    /**
     * What to do when your database needs to be updated. This could mean careful migration of old data to new data.
     * Maybe adding or deleting database columns, etc..
     * <p>
     * <p>
     * <b>NOTE:</b> You should use the connectionSource argument that is passed into this method call or the one
     * returned by getConnectionSource(). If you use your own, a recursive call or other unexpected results may result.
     * </p>
     *
     * @param database         Database being upgraded.
     * @param connectionSource To use get connections to the database to be updated.
     * @param oldVersion       The version of the current database so we can know what to do to the database.
     * @param newVersion       The version that we are upgrading the database to.
     * @since 1.0.0
     */
    public abstract void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion,
                                   int newVersion);

    /**
     * Get the connection source associated with the helper.
     * @return returns the connection source
     * @since 1.0.0
     */
    public ConnectionSource getConnectionSource() {
        if (!isOpen) {
            // we don't throw this exception, but log it for debugging purposes
            getSecureDBLogInstance().log(LoggingInterface.LogLevel.WARNING, SecureDBLogEventID.SDB_ORMLITE_SQLITE_OPEN_HELPER, "" + new IllegalStateException() + "Getting connectionSource was called after closed");
        }
        return connectionSource;
    }

    /**
     * Satisfies the {@link SQLiteOpenHelper#onCreate(SQLiteDatabase)} interface method.
     */
    @Override
    public final void onCreate(SQLiteDatabase db) {
        ConnectionSource cs = getConnectionSource();
        /*
         * The method is called by Android database helper's get-database calls when Android detects that we need to
		 * create or update the database. So we have to use the database argument and save a connection to it on the
		 * SecureAndroidConnectionSource, otherwise it will go recursive if the subclass calls getConnectionSource().
		 */
        DatabaseConnection conn = cs.getSpecialConnection(null);
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true, cancelQueriesEnabled);
            try {
                cs.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try {
            onCreate(db, cs);
        } finally {
            if (clearSpecial) {
                cs.clearSpecialConnection(conn);
            }
        }
    }

    /**
     * Satisfies the {@link SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)} interface method.
     */
    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ConnectionSource cs = getConnectionSource();
		/*
		 * The method is called by Android database helper's get-database calls when Android detects that we need to
		 * create or update the database. So we have to use the database argument and save a connection to it on the
		 * SecureAndroidConnectionSource, otherwise it will go recursive if the subclass calls getConnectionSource().
		 */
        DatabaseConnection conn = cs.getSpecialConnection(null);
        boolean clearSpecial = false;
        if (conn == null) {
            conn = new AndroidDatabaseConnection(db, true, cancelQueriesEnabled);
            try {
                cs.saveSpecialConnection(conn);
                clearSpecial = true;
            } catch (SQLException e) {
                throw new IllegalStateException("Could not save special connection", e);
            }
        }
        try {
            onUpgrade(db, cs, oldVersion, newVersion);
        } finally {
            if (clearSpecial) {
                cs.clearSpecialConnection(conn);
            }
        }
    }

    private void createKey() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences("com.philips.platform.database", MODE_PRIVATE);
        ;
        SharedPreferences.Editor editor;
        if (sharedPreferences.getBoolean("firstRun", true)) {
            mSecureStorage = mAppInfraInterface.getSecureStorage();
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, databaseKey, sse);
            editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();

        }
        getSecureDBLogInstance().log(LoggingInterface.LogLevel.DEBUG, SecureDBLogEventID.SDB_ORMLITE_SQLITE_OPEN_HELPER, "Successfully Created Key");
    }

    /**
     * Get the permission to write the DB
     * @return returns the database object
     * @throws SQLException
     * @since 1.1.0
     */
    public SQLiteDatabase getWriteDbPermission() throws SQLException {

        return getWritableDatabase(getKeyName());
    }


    private char[] getKeyName() {
        if (key != null) {
            return key;
        } else {
            return getKeyValue(databaseKey, mAppInfraInterface);
        }
    }


    /**
     * Close any open connections.
     * @since 1.0.0
     */
    @Override
    public void close() {
        super.close();
        connectionSource.close();
		/*
		 * We used to set connectionSource to null here but now we just set the closed flag and then log heavily if
		 * someone uses getConectionSource() after this point.
		 */
        isOpen = false;
    }

    /**
     * Return true if the helper is still open. Once {@link #close()} is called then this will return false.
     * @since 1.0.0
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Get a DAO for our class. This uses the {@link DaoManager} to cache the DAO for future gets.
     * <p>
     * <p>
     * NOTE: This routing does not return Dao<T, ID> because of casting issues if we are assigning it to a custom DAO.
     * Grumble.
     * </p>
     */
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws SQLException {
        // special reflection fu is now handled internally by create dao calling the database type
        Dao<T, ?> dao = DaoManager.createDao(getConnectionSource(), clazz);
        @SuppressWarnings("unchecked")
        D castDao = (D) dao;
        return castDao;
    }

    /**
     * Get a RuntimeExceptionDao for our class. This uses the {@link DaoManager} to cache the DAO for future gets.
     * <p>
     * <p>
     * NOTE: This routing does not return RuntimeExceptionDao<T, ID> because of casting issues if we are assigning it to
     * a custom DAO. Grumble.
     * </p>
     */
    public <D extends RuntimeExceptionDao<T, ?>, T> D getRuntimeExceptionDao(Class<T> clazz) {
        try {
            Dao<T, ?> dao = getDao(clazz);
            @SuppressWarnings({"unchecked", "rawtypes"})
            D castDao = (D) new RuntimeExceptionDao(dao);
            return castDao;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create RuntimeExcepitionDao for class " + clazz, e);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(super.hashCode());
    }

    private char[] getKeyValue(String databaseKey, AppInfraInterface mAppInfraInterface) {

        mSecureStorage = mAppInfraInterface.getSecureStorage();
        final SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
        if (mSecureStorage != null) {
            final Key key = mSecureStorage.getKey(databaseKey, sse);
            if (key != null) {
                final byte[] keyData = key.getEncoded();
                String keyString = null;   // if the charset is UTF-8
                try {
                    keyString = new String(keyData, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException("Could not key value", e);
                }
                //Directly byte[] to char[] typecast not good soluation in the case getKey contains numberic value like 67,68 that time char will return ASCII Value of 67,68.
                final char[] keyCharArray = keyString.toCharArray();
                //return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
                getSecureDBLogInstance().log(LoggingInterface.LogLevel.DEBUG, SecureDBLogEventID.SDB_ORMLITE_SQLITE_OPEN_HELPER, "Successfully get Key value");
                return keyCharArray;
            }
        }
        return null;


    }

    /**
     * Get the app version.
     * @return returns the app version
     * @since 2.2.0
     *
     */
    public String getSecureDbAppVersion() {
        String mAppVersion = null;
        try {
            mAppVersion = BuildConfig.VERSION_NAME;
        } catch (Exception e) {
            getSecureDBLogInstance().log(LoggingInterface.LogLevel.ERROR, SecureDBLogEventID.SDB_ORMLITE_SQLITE_OPEN_HELPER, " Error in SecureDb AppVersion"+e.getMessage());
        }
        if (mAppVersion != null && !mAppVersion.isEmpty()) {
            if (!mAppVersion.matches("[0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?")) {
                throw new IllegalArgumentException("AppVersion should in this format " +
                        "\" [0-9]+\\.[0-9]+\\.[0-9]+([_(-].*)?]\" ");
            }
        } else {
            throw new IllegalArgumentException("Secure DB Appversion cannot be null");
        }
        return mAppVersion;
    }
}
