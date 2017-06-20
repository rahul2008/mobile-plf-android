/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.database;

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

import java.sql.SQLException;
import java.util.List;

import cdp.philips.com.consents.ConsentDetailType;
import cdp.philips.com.database.datatypes.MeasurementDetailType;
import cdp.philips.com.database.datatypes.MeasurementGroupDetailType;
import cdp.philips.com.database.datatypes.MeasurementType;
import cdp.philips.com.database.datatypes.MomentDetailType;
import cdp.philips.com.database.datatypes.MomentType;
import cdp.philips.com.database.table.OrmCharacteristics;
import cdp.philips.com.database.table.OrmConsentDetail;
import cdp.philips.com.database.table.OrmDCSync;
import cdp.philips.com.database.table.OrmInsight;
import cdp.philips.com.database.table.OrmInsightMetaData;
import cdp.philips.com.database.table.OrmMeasurement;
import cdp.philips.com.database.table.OrmMeasurementDetail;
import cdp.philips.com.database.table.OrmMeasurementDetailType;
import cdp.philips.com.database.table.OrmMeasurementGroup;
import cdp.philips.com.database.table.OrmMeasurementGroupDetail;
import cdp.philips.com.database.table.OrmMeasurementGroupDetailType;
import cdp.philips.com.database.table.OrmMeasurementType;
import cdp.philips.com.database.table.OrmMoment;
import cdp.philips.com.database.table.OrmMomentDetail;
import cdp.philips.com.database.table.OrmMomentDetailType;
import cdp.philips.com.database.table.OrmMomentType;
import cdp.philips.com.database.table.OrmSettings;
import cdp.philips.com.database.table.OrmSynchronisationData;


/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 * @author kevingalligan
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "DataService.db";
    private static final int DATABASE_VERSION = 1;
    private final UuidGenerator uuidGenerator;
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

    public DatabaseHelper(Context context, final UuidGenerator uuidGenerator) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.uuidGenerator = uuidGenerator;
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
        insertDefaultConsent();
    }

    private void insertDefaultConsent() {

        try {
            consentDetailDao=getConsentDetailsDao();
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED.getDescription(),ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED.getDescription(),ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED.getDescription(),ConsentDetail.DEFAULT_DOCUMENT_VERSION,
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


    private void addNewMomentDetailTypeAndAddedUUIDForTagging() throws SQLException {
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

    public Dao<OrmSettings, Integer> getSettingsDao() throws SQLException {
        if (settingDao == null) {
            settingDao = getDao(OrmSettings.class);
        }
        return settingDao;
    }

    public Dao<OrmDCSync, Integer> getDCSyncDao() throws SQLException {
        if (ormDCSyncDao == null) {
            ormDCSyncDao = getDao(OrmDCSync.class);
        }
        return ormDCSyncDao;
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
