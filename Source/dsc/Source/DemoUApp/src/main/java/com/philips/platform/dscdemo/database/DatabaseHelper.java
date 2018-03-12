/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.dscdemo.consents.ConsentDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.datatypes.MeasurementType;
import com.philips.platform.dscdemo.database.datatypes.MomentDetailType;
import com.philips.platform.dscdemo.database.datatypes.MomentType;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmInsightMetaData;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetailType;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroup;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetailType;
import com.philips.platform.dscdemo.database.table.OrmMeasurementType;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentDetail;
import com.philips.platform.dscdemo.database.table.OrmMomentDetailType;
import com.philips.platform.dscdemo.database.table.OrmMomentType;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;
import com.philips.platform.securedblibrary.SecureDbOrmLiteSqliteOpenHelper;
import com.philips.platform.securedblibrary.SqlLiteInitializer;

import net.sqlcipher.database.SQLiteDatabase;

import java.sql.SQLException;
import java.util.List;


/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 */
public class DatabaseHelper extends SecureDbOrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "DataService.db";
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_PASSWORD_KEY = "dataservices";

    private static DatabaseHelper sDatabaseHelper;

    private Dao<OrmMoment, Integer> momentDao;
    private Dao<OrmMomentType, Integer> momentTypeDao;
    private Dao<OrmMomentDetail, Integer> momentDetailDao;
    private Dao<OrmMomentDetailType, Integer> momentDetailTypeDao;
    private Dao<OrmMeasurement, Integer> measurementDao;
    private Dao<OrmMeasurementType, Integer> measurementTypeDao;
    private Dao<OrmMeasurementDetail, Integer> measurementDetailDao;
    private Dao<OrmMeasurementDetailType, Integer> measurementDetailTypeDao;
    private Dao<OrmMeasurementGroupDetailType, Integer> measurementGroupDetailTypes;
    private Dao<OrmMeasurementGroup, Integer> measurementGroup;
    private Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetails;
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;
    private Dao<OrmConsentDetail, Integer> consentDetailDao;
    private Dao<OrmSettings, Integer> settingDao;
    private Dao<OrmCharacteristics, Integer> characteristicsDao;
    private Dao<OrmDCSync, Integer> ormDCSyncDao;
    private Dao<OrmInsight, Integer> ormInsightDao;
    private Dao<OrmInsightMetaData, Integer> ormInsightMetaDataDao;

    private DaoProvider daoProvider;

    public static synchronized DatabaseHelper getInstance(Context context, AppInfraInterface appInfraInterface) {
        if (sDatabaseHelper == null) {
            return sDatabaseHelper = new DatabaseHelper(context, appInfraInterface);
        }
        return sDatabaseHelper;
    }

    private DatabaseHelper(Context context, AppInfraInterface appInfraInterface) {
        super(context, appInfraInterface, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_PASSWORD_KEY);
    }

    DatabaseHelper(Context context, AppInfraInterface appInfraInterface, SqlLiteInitializer initializer) {
        super(context, appInfraInterface, DATABASE_NAME, null, DATABASE_VERSION, DATABASE_PASSWORD_KEY, initializer);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        OrmLiteDatabase database = new OrmLiteDatabase(connectionSource);
        OrmDaoProvider daoProvider = new OrmDaoProvider(this);
        onCreate(database, daoProvider);
    }

    public void onCreate(final OrmDatabase database, final DaoProvider daoProvider) {
        this.daoProvider = daoProvider;
        try {
            createTables(database);
            insertDictionaries();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertDictionaries() throws SQLException {
        insertMomentTypes();
        insertMeasurementTypes();
        insertMomentDetailsTypes();
        insertMeasurementDetailTypes();
        insertMeasurementGroupDetailType();
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


    private void insertMeasurementTypes() throws SQLException {
        final Dao<OrmMeasurementType, Integer> measurementTypeDao = getMeasurementTypeDao();
        List<String> values = MeasurementType.getMeasurementTypes();
        for (final String value : values) {
            measurementTypeDao.createOrUpdate(new OrmMeasurementType(MeasurementType.getIDFromDescription(value),
                    value,
                    MeasurementType.getUnitFromDescription(value)));
        }
    }

    private void insertMomentDetailsTypes() throws SQLException {
        final Dao<OrmMomentDetailType, Integer> momentDetailTypeDao = getMomentDetailTypeDao();
        List<String> values = MomentDetailType.getMomentDetailTypes();
        for (final String value : values) {
            momentDetailTypeDao.createOrUpdate(new OrmMomentDetailType(MomentDetailType.getIDFromDescription(value), value));
        }
    }

    private void insertMeasurementDetailTypes() throws SQLException {
        Dao<OrmMeasurementDetailType, Integer> measurementDetailTypeDao = getMeasurementDetailTypeDao();
        List<String> values = MeasurementDetailType.getMeasurementDetailTypes();
        for (final String value : values) {
            measurementDetailTypeDao.createOrUpdate(new OrmMeasurementDetailType(MeasurementDetailType.getIDFromDescription(value), value));
        }
    }

    private void insertMomentTypes() throws SQLException {
        Dao<OrmMomentType, Integer> momentTypeDao = getMomentTypeDao();
        List<String> values = MomentType.getMomentTypes();
        for (final String value : values) {
            momentTypeDao.createOrUpdate(new OrmMomentType(MomentType.getIDFromDescription(value), value));
        }
    }

    private void insertMeasurementGroupDetailType() throws SQLException {
        Dao<OrmMeasurementGroupDetailType, Integer> measurementGroupDetailTypes = getMeasurementGroupDetailTypeDao();
        // MeasurementGroupDetailType[] values = MeasurementGroupDetailType.values();
        List<String> values = MeasurementGroupDetailType.getMeasurementGroupDetailType();
        for (final String value : values) {
            measurementGroupDetailTypes.createOrUpdate(new OrmMeasurementGroupDetailType(MeasurementGroupDetailType.getIDFromDescription(value), value));
        }
    }

    private void createTables(final OrmDatabase database) throws SQLException {
        database.createTable(OrmMoment.class);
        database.createTable(OrmMomentType.class);
        database.createTable(OrmMomentDetail.class);
        database.createTable(OrmMomentDetailType.class);
        database.createTable(OrmMeasurement.class);
        database.createTable(OrmMeasurementType.class);
        database.createTable(OrmMeasurementDetail.class);
        database.createTable(OrmMeasurementDetailType.class);
        database.createTable(OrmSynchronisationData.class);
        database.createTable(OrmConsentDetail.class);
        database.createTable(OrmMeasurementGroup.class);
        database.createTable(OrmMeasurementGroupDetail.class);
        database.createTable(OrmMeasurementGroupDetailType.class);
        database.createTable(OrmCharacteristics.class);
        database.createTable(OrmSettings.class);
        database.createTable(OrmDCSync.class);
        database.createTable(OrmInsight.class);
        database.createTable(OrmInsightMetaData.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        if (newVer > oldVer) {
            if (newVer >= 2 && oldVer == 1) {
                try {
                    this.getMomentDao().executeRaw("ALTER TABLE `OrmMoment` ADD COLUMN expirationDate INTEGER NULL");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void dropTables(final ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, OrmMoment.class, true);
        TableUtils.dropTable(connectionSource, OrmMomentType.class, true);
        TableUtils.dropTable(connectionSource, OrmMomentDetail.class, true);
        TableUtils.dropTable(connectionSource, OrmMomentDetailType.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurement.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurementType.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurementDetail.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurementDetailType.class, true);
        TableUtils.dropTable(connectionSource, OrmSynchronisationData.class, true);
        TableUtils.dropTable(connectionSource, OrmConsentDetail.class, true);
        TableUtils.dropTable(connectionSource, OrmSettings.class, true);
        TableUtils.dropTable(connectionSource, OrmCharacteristics.class, true);
        TableUtils.dropTable(connectionSource, OrmDCSync.class, true);
        TableUtils.dropTable(connectionSource, OrmInsight.class, true);
        TableUtils.dropTable(connectionSource, OrmInsightMetaData.class, true);
    }

    public Dao<OrmMoment, Integer> getMomentDao() throws SQLException {
        if (momentDao == null) {
            momentDao = getOrmDao(OrmMoment.class);
        }
        return momentDao;
    }

    public Dao getOrmDao(final Class<?> clazz) throws SQLException {
        return daoProvider.getOrmDao(clazz);
    }

    private Dao<OrmMomentType, Integer> getMomentTypeDao() throws SQLException {
        if (momentTypeDao == null) {
            momentTypeDao = getOrmDao(OrmMomentType.class);
        }
        return momentTypeDao;
    }

    public Dao<OrmMeasurementGroupDetailType, Integer> getMeasurementGroupDetailTypeDao() throws SQLException {
        if (measurementGroupDetailTypes == null) {
            measurementGroupDetailTypes = getOrmDao(OrmMeasurementGroupDetailType.class);
        }
        return measurementGroupDetailTypes;
    }

    public Dao<OrmMeasurementGroup, Integer> getMeasurementGroupDao() throws SQLException {
        if (measurementGroup == null) {
            measurementGroup = getOrmDao(OrmMeasurementGroup.class);
        }
        return measurementGroup;
    }

    public Dao<OrmMeasurementGroupDetail, Integer> getMeasurementGroupDetailDao() throws SQLException {
        if (measurementGroupDetails == null) {
            measurementGroupDetails = getOrmDao(OrmMeasurementGroupDetail.class);
        }
        return measurementGroupDetails;
    }

    public Dao<OrmMomentDetail, Integer> getMomentDetailDao() throws SQLException {
        if (momentDetailDao == null) {
            momentDetailDao = getOrmDao(OrmMomentDetail.class);
        }
        return momentDetailDao;
    }

    private Dao<OrmMomentDetailType, Integer> getMomentDetailTypeDao() throws SQLException {
        if (momentDetailTypeDao == null) {
            momentDetailTypeDao = getOrmDao(OrmMomentDetailType.class);
        }
        return momentDetailTypeDao;
    }

    public Dao<OrmMeasurement, Integer> getMeasurementDao() throws SQLException {
        if (measurementDao == null) {
            measurementDao = getOrmDao(OrmMeasurement.class);
        }
        return measurementDao;
    }

    private Dao<OrmMeasurementType, Integer> getMeasurementTypeDao() throws SQLException {
        if (measurementTypeDao == null) {
            measurementTypeDao = getOrmDao(OrmMeasurementType.class);
        }
        return measurementTypeDao;
    }

    public Dao<OrmMeasurementDetail, Integer> getMeasurementDetailDao() throws SQLException {
        if (measurementDetailDao == null) {
            measurementDetailDao = getOrmDao(OrmMeasurementDetail.class);
        }
        return measurementDetailDao;
    }

    private Dao<OrmMeasurementDetailType, Integer> getMeasurementDetailTypeDao() throws SQLException {
        if (measurementDetailTypeDao == null) {
            measurementDetailTypeDao = getOrmDao(OrmMeasurementDetailType.class);
        }
        return measurementDetailTypeDao;
    }

    public Dao<OrmSynchronisationData, Integer> getSynchronisationDataDao() throws SQLException {
        if (synchronisationDataDao == null) {
            synchronisationDataDao = getOrmDao(OrmSynchronisationData.class);
        }
        return synchronisationDataDao;
    }


    public Dao<OrmConsentDetail, Integer> getConsentDetailsDao() throws SQLException {
        if (consentDetailDao == null) {
            consentDetailDao = getOrmDao(OrmConsentDetail.class);
        }
        return consentDetailDao;
    }

    public Dao<OrmCharacteristics, Integer> getCharacteristicsDao() throws SQLException {
        if (characteristicsDao == null) {
            characteristicsDao = getOrmDao(OrmCharacteristics.class);
        }
        return characteristicsDao;
    }

    public Dao<OrmSettings, Integer> getSettingsDao() throws SQLException {
        if (settingDao == null) {
            settingDao = getOrmDao(OrmSettings.class);
        }
        return settingDao;
    }

    public Dao<OrmDCSync, Integer> getDCSyncDao() throws SQLException {
        if (ormDCSyncDao == null) {
            ormDCSyncDao = getOrmDao(OrmDCSync.class);
        }
        return ormDCSyncDao;
    }

    public Dao<OrmInsight, Integer> getInsightDao() throws SQLException {
        if (ormInsightDao == null) {
            ormInsightDao = getOrmDao(OrmInsight.class);
        }
        return ormInsightDao;
    }

    public Dao<OrmInsightMetaData, Integer> getInsightMetaDataDao() throws SQLException {
        if (ormInsightMetaDataDao == null) {
            ormInsightMetaDataDao = getOrmDao(OrmInsightMetaData.class);
        }
        return ormInsightMetaDataDao;
    }
}
