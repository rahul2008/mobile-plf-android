/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.dataservices.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.baseapp.screens.dataservices.consents.ConsentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementGroupDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MeasurementType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MomentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.datatypes.MomentType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmDCSync;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmInsight;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmInsightMetaData;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurement;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroup;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroupDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSettings;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.UuidGenerator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 * @author kevingalligan
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME = "DataService.db";
    private static final int DATABASE_VERSION = 1;
   // private final UuidGenerator uuidGenerator;
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

    public static synchronized DatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context.getApplicationContext());
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //this.uuidGenerator = uuidGenerator;
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
        insertMomentTypes();
        insertMeasurementTypes();
        insertMomentDetailsTypes();
        insertMeasurementDetailTypes();
        insertMeasurementGroupDetailType();
        insertDefaultSettings();
        insertDefaultUCSync();
        insertDefaultConsent();
    }

    private void insertDefaultConsent() {
        DataServicesManager mDataServices = DataServicesManager.getInstance();
        List<ConsentDetail> consentDetails = new ArrayList<>();

        consentDetails.add(mDataServices.createConsentDetail
                (ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED,ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                        ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));

        consentDetails.add(mDataServices.createConsentDetail
                (ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED,ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                        ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        consentDetails.add(mDataServices.createConsentDetail
                (ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED,ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                        ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        mDataServices.saveConsentDetails(consentDetails, null);
        insertDefaultDCSyncValues(SyncType.CONSENT);
    }

    private void insertDefaultDCSyncValues() {

        for (SyncType tableType : SyncType.values()) {

            ormDCSyncDao = getDCSyncDao();
            try {
                ormDCSyncDao.createOrUpdate(new OrmDCSync(tableType.getId(), tableType.getDescription(), true));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void insertDefaultDCSyncValues(SyncType type) {
            ormDCSyncDao = getDCSyncDao();
            try {
                ormDCSyncDao.createOrUpdate(new OrmDCSync(type.getId(), type.getDescription(), true));
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }

    private void insertDefaultSettings() {
        Settings settings = DataServicesManager.getInstance().createUserSettings("en_US" ,"metric");
        DataServicesManager.getInstance().saveUserSettings(settings, null);
        insertDefaultDCSyncValues(SyncType.SETTINGS);
    }

    private void insertDefaultUCSync(){
        insertDefaultDCSyncValues(SyncType.CHARACTERISTICS);
    }

    public Dao<OrmSettings, Integer> getSettingsDao() {
        if (settingDao == null) {
            try {
                settingDao = getDao(OrmSettings.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return settingDao;
    }

    public Dao<OrmDCSync, Integer> getDCSyncDao() {
        if (ormDCSyncDao == null) {
            try {
                ormDCSyncDao = getDao(OrmDCSync.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ormDCSyncDao;
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


    private void createTables(final ConnectionSource connectionSource) throws SQLException {
        TableUtils.createTable(connectionSource, OrmMoment.class);
        TableUtils.createTable(connectionSource, OrmMomentType.class);
        TableUtils.createTable(connectionSource, OrmMomentDetail.class);
        TableUtils.createTable(connectionSource, OrmMomentDetailType.class);
        TableUtils.createTable(connectionSource, OrmMeasurement.class);
        TableUtils.createTable(connectionSource, OrmMeasurementType.class);
        TableUtils.createTable(connectionSource, OrmMeasurementDetail.class);
        TableUtils.createTable(connectionSource, OrmMeasurementDetailType.class);
        TableUtils.createTable(connectionSource, OrmSynchronisationData.class);
        TableUtils.createTable(connectionSource, OrmConsentDetail.class);
        TableUtils.createTable(connectionSource, OrmMeasurementGroup.class);
        TableUtils.createTable(connectionSource, OrmMeasurementGroupDetail.class);
        TableUtils.createTable(connectionSource, OrmMeasurementGroupDetailType.class);
        TableUtils.createTable(connectionSource, OrmCharacteristics.class);
        TableUtils.createTable(connectionSource, OrmSettings.class);
        TableUtils.createTable(connectionSource, OrmDCSync.class);
        TableUtils.createTable(connectionSource, OrmInsight.class);
        TableUtils.createTable(connectionSource, OrmInsightMetaData.class);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        DSLog.i(TAG + "onUpgrade", "olderVer =" + oldVer + " newerVer =" + newVer);
        if (newVer > oldVer) {
            //Alter your table here...
        }
    }


    private void addMeasurementTypes(MeasurementType... measurementTypes) throws SQLException {
        final Dao<OrmMeasurementType, Integer> measurementTypeDao = getMeasurementTypeDao();
        // for (MeasurementType measurementType : measurementTypes) {
        measurementTypeDao.createOrUpdate(new OrmMeasurementType(41, "TEMPERATURE", "\u2103"));
//        }
    }

    private void addMomentTypes(MomentType... momentTypes) throws SQLException {
        final Dao<OrmMomentType, Integer> ormMomentTypeDao = getMomentTypeDao();
        for (MomentType momentType : momentTypes) {
            ormMomentTypeDao.createOrUpdate(new OrmMomentType(MomentType.getIDFromDescription("TEMPERATURE"),
                    MomentType.getDescriptionFromID(25)));
        }
    }


    /*private void addNewMomentDetailTypeAndAddedUUIDForTagging() throws SQLException {
        final Dao<OrmMomentDetailType, Integer> momentDetailTypeDao = getMomentDetailTypeDao();
        momentDetailTypeDao.createOrUpdate(new OrmMomentDetailType(MomentDetailType.getIDFromDescription("TAGGING_ID"),
                MomentDetailType.getDescriptionFromID(54)));

        final Dao<OrmMoment, Integer> ormMomentDao = getDao(OrmMoment.class);
        List<OrmMoment> moments = ormMomentDao.queryForAll();
        for (OrmMoment moment : moments) {
            final OrmMomentDetailType detailType = new OrmMomentDetailType(MomentDetailType.getIDFromDescription("TAGGING_ID"),
                    MomentDetailType.getDescriptionFromID(54));
            if (OrmMoment.NO_ID.equals(moment.getAnalyticsId())) {
                OrmMomentDetail detail = new OrmMomentDetail(detailType, moment);
                detail.setValue(uuidGenerator.generateRandomUUID());
                moment.addMomentDetail(detail);
                moment.setSynced(false);
                ormMomentDao.update(moment);
            }
        }
    }*/


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
            momentDao = getDao(OrmMoment.class);
        }
        return momentDao;
    }

    private Dao<OrmMomentType, Integer> getMomentTypeDao() throws SQLException {
        if (momentTypeDao == null) {
            momentTypeDao = getDao(OrmMomentType.class);
        }
        return momentTypeDao;
    }

    public Dao<OrmMeasurementGroupDetailType, Integer> getMeasurementGroupDetailTypeDao() throws SQLException {
        if (measurementGroupDetailTypes == null) {
            measurementGroupDetailTypes = getDao(OrmMeasurementGroupDetailType.class);
        }
        return measurementGroupDetailTypes;
    }

    public Dao<OrmMeasurementGroup, Integer> getMeasurementGroupDao() throws SQLException {
        if (measurementGroup == null) {
            measurementGroup = getDao(OrmMeasurementGroup.class);
        }
        return measurementGroup;
    }

    public Dao<OrmMeasurementGroupDetail, Integer> getMeasurementGroupDetailDao() throws SQLException {
        if (measurementGroupDetails == null) {
            measurementGroupDetails = getDao(OrmMeasurementGroupDetail.class);
        }
        return measurementGroupDetails;
    }

    public Dao<OrmMomentDetail, Integer> getMomentDetailDao() throws SQLException {
        if (momentDetailDao == null) {
            momentDetailDao = getDao(OrmMomentDetail.class);
        }
        return momentDetailDao;
    }

    private Dao<OrmMomentDetailType, Integer> getMomentDetailTypeDao() throws SQLException {
        if (momentDetailTypeDao == null) {
            momentDetailTypeDao = getDao(OrmMomentDetailType.class);
        }
        return momentDetailTypeDao;
    }

    public Dao<OrmMeasurement, Integer> getMeasurementDao() throws SQLException {
        if (measurementDao == null) {
            measurementDao = getDao(OrmMeasurement.class);
        }
        return measurementDao;
    }

    private Dao<OrmMeasurementType, Integer> getMeasurementTypeDao() throws SQLException {
        if (measurementTypeDao == null) {
            measurementTypeDao = getDao(OrmMeasurementType.class);
        }
        return measurementTypeDao;
    }

    public Dao<OrmMeasurementDetail, Integer> getMeasurementDetailDao() throws SQLException {
        if (measurementDetailDao == null) {
            measurementDetailDao = getDao(OrmMeasurementDetail.class);
        }
        return measurementDetailDao;
    }

    private Dao<OrmMeasurementDetailType, Integer> getMeasurementDetailTypeDao() throws SQLException {
        if (measurementDetailTypeDao == null) {
            measurementDetailTypeDao = getDao(OrmMeasurementDetailType.class);
        }
        return measurementDetailTypeDao;
    }

    public Dao<OrmSynchronisationData, Integer> getSynchronisationDataDao() throws SQLException {
        if (synchronisationDataDao == null) {
            synchronisationDataDao = getDao(OrmSynchronisationData.class);
        }
        return synchronisationDataDao;
    }


    public Dao<OrmConsentDetail, Integer> getConsentDetailsDao() throws SQLException {
        if (consentDetailDao == null) {
            consentDetailDao = getDao(OrmConsentDetail.class);
        }
        return consentDetailDao;
    }

    public Dao<OrmCharacteristics, Integer> getCharacteristicsDao() throws SQLException {
        if (characteristicsDao == null) {
            characteristicsDao = getDao(OrmCharacteristics.class);
        }
        return characteristicsDao;
    }


    public Dao<OrmInsight, Integer> getInsightDao() throws SQLException {
        if (ormInsightDao == null) {
            ormInsightDao = getDao(OrmInsight.class);
        }
        return ormInsightDao;
    }

    public Dao<OrmInsightMetaData, Integer> getInsightMetaDataDao() throws SQLException {
        if (ormInsightMetaDataDao == null) {
            ormInsightMetaDataDao = getDao(OrmInsightMetaData.class);
        }
        return ormInsightMetaDataDao;
    }
}
