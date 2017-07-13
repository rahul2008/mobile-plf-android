/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.demoapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.dprdemo.consents.ConsentDetailType;
import cdp.philips.com.demoapp.database.table.OrmConsentDetail;
import cdp.philips.com.demoapp.database.table.OrmDCSync;

import java.sql.SQLException;


/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 * @author kevingalligan
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "DeviceParing.db";
    private static final int DATABASE_VERSION = 1;
    private final UuidGenerator uuidGenerator;
    private Dao<OrmConsentDetail, Integer> consentDetailDao;
    private Dao<OrmDCSync, Integer> ormDCSyncDao;

    private static DatabaseHelper sDatabaseHelper;

    private DatabaseHelper(Context context, final UuidGenerator uuidGenerator) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.uuidGenerator = uuidGenerator;
    }

    public static synchronized DatabaseHelper getInstance(Context context, final UuidGenerator uuidGenerator) {
        if (sDatabaseHelper == null) {
            return sDatabaseHelper = new DatabaseHelper(context, uuidGenerator);
        }
        return sDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        DSLog.d(TAG, "onCreate DatabaseHelper");
        try {
            createTables(connectionSource);
            insertDictionaries();
        } catch (SQLException e) {
            DSLog.e(TAG, "Error Unable to create databases" + e);
        }
    }

    private void insertDictionaries() throws SQLException {
        insertDefaultConsent();
    }

    private void insertDefaultConsent() {

        try {
            consentDetailDao = getConsentDetailsDao();
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            insertDefaultDCSyncValues(SyncType.CONSENT);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void insertDefaultDCSyncValues(SyncType tableType) {

        try {
            ormDCSyncDao = getDCSyncDao();
            ormDCSyncDao.createOrUpdate(new OrmDCSync(tableType.getId(), tableType.getDescription(), true));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void createTables(final ConnectionSource connectionSource) throws SQLException {

        TableUtils.createTable(connectionSource, OrmConsentDetail.class);
        TableUtils.createTable(connectionSource, OrmDCSync.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        DSLog.i(TAG + "onUpgrade", "olderVer =" + oldVer + " newerVer =" + newVer);
        if (newVer > oldVer) {
            //Alter your table here...
        }
    }


    public void dropTables(final ConnectionSource connectionSource) throws SQLException {

        TableUtils.dropTable(connectionSource, OrmConsentDetail.class, true);
        TableUtils.dropTable(connectionSource, OrmDCSync.class, true);
    }

    public Dao<OrmConsentDetail, Integer> getConsentDetailsDao() throws SQLException {
        if (consentDetailDao == null) {
            consentDetailDao = getDao(OrmConsentDetail.class);
        }
        return consentDetailDao;
    }

    public Dao<OrmDCSync, Integer> getDCSyncDao() throws SQLException {
        if (ormDCSyncDao == null) {
            ormDCSyncDao = getDao(OrmDCSync.class);
        }
        return ormDCSyncDao;
    }

}
