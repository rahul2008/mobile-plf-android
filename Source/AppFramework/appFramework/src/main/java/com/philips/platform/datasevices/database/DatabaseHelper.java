/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasevices.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.philips.platform.core.datatypes.MeasurementDetailType;
import com.philips.platform.core.datatypes.MeasurementType;
import com.philips.platform.core.datatypes.MomentDetailType;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.datasevices.database.table.OrmMeasurement;
import com.philips.platform.datasevices.database.table.OrmMeasurementDetail;
import com.philips.platform.datasevices.database.table.OrmMeasurementDetailType;
import com.philips.platform.datasevices.database.table.OrmMeasurementType;
import com.philips.platform.datasevices.database.table.OrmMoment;
import com.philips.platform.datasevices.database.table.OrmMomentDetail;
import com.philips.platform.datasevices.database.table.OrmMomentDetailType;
import com.philips.platform.datasevices.database.table.OrmMomentType;
import com.philips.platform.datasevices.database.table.OrmSynchronisationData;

import java.sql.SQLException;
import java.util.List;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 *
 * @author kevingalligan
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();
    public static final String DATABASE_NAME = "DataService.db";
    public static final int DATABASE_VERSION = 1;
    private final UuidGenerator uuidGenerator;
    private final String packageName;
    private Dao<OrmMoment, Integer> momentDao;
    private Dao<OrmMomentType, Integer> momentTypeDao;
    private Dao<OrmMomentDetail, Integer> momentDetailDao;
    private Dao<OrmMomentDetailType, Integer> momentDetailTypeDao;
    private Dao<OrmMeasurement, Integer> measurementDao;
    private Dao<OrmMeasurementType, Integer> measurementTypeDao;
    private Dao<OrmMeasurementDetail, Integer> measurementDetailDao;
    private Dao<OrmMeasurementDetailType, Integer> measurementDetailTypeDao;
    private Dao<OrmSynchronisationData, Integer> synchronisationDataDao;

    public DatabaseHelper(Context context, final UuidGenerator uuidGenerator) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.uuidGenerator = uuidGenerator;
        this.packageName = context.getPackageName();
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        Log.d(TAG, "onCreate DatabaseHelper");
        try {
            createTables(connectionSource);
            insertDictionaries();
        } catch (SQLException e) {
            Log.e(TAG, "Unable to create databases", e);
        }
    }

    private void insertDictionaries() throws SQLException {
        insertMomentTypes();
        insertMeasurementTypes();
        insertMomentDetailsTypes();
        insertMeasurementDetailTypes();
    }

    private void insertMeasurementTypes() throws SQLException {
        MeasurementType[] values = MeasurementType.values();
        final Dao<OrmMeasurementType, Integer> measurementTypeDao = getMeasurementTypeDao();
        for (final MeasurementType value : values) {
            measurementTypeDao.createOrUpdate(new OrmMeasurementType(value));
        }
    }

    private void insertMomentDetailsTypes() throws SQLException {
        MomentDetailType[] values = MomentDetailType.values();
        final Dao<OrmMomentDetailType, Integer> momentDetailTypeDao = getMomentDetailTypeDao();
        for (final MomentDetailType value : values) {
            momentDetailTypeDao.createOrUpdate(new OrmMomentDetailType(value));
        }
    }

    private void insertMeasurementDetailTypes() throws SQLException {
        Dao<OrmMeasurementDetailType, Integer> measurementDetailTypeDao = getMeasurementDetailTypeDao();
        MeasurementDetailType[] values = MeasurementDetailType.values();
        for (final MeasurementDetailType value : values) {
            measurementDetailTypeDao.createOrUpdate(new OrmMeasurementDetailType(value));
        }
    }

    private void insertMomentTypes() throws SQLException {
        Dao<OrmMomentType, Integer> momentTypeDao = getMomentTypeDao();
        MomentType[] values = MomentType.values();
        for (final MomentType value : values) {
            momentTypeDao.createOrUpdate(new OrmMomentType(value));
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {

    }


    private void addMeasurementTypes(MeasurementType... measurementTypes) throws SQLException {
        final Dao<OrmMeasurementType, Integer> measurementTypeDao = getMeasurementTypeDao();
        for (MeasurementType measurementType : measurementTypes) {
            measurementTypeDao.createOrUpdate(new OrmMeasurementType(measurementType));
        }
    }

    private void addMomentTypes(MomentType... momentTypes) throws SQLException {
        final Dao<OrmMomentType, Integer> ormMomentTypeDao = getMomentTypeDao();
        for (MomentType momentType : momentTypes) {
            ormMomentTypeDao.createOrUpdate(new OrmMomentType(momentType));
        }
    }


    private void addNewMomentDetailTypeAndAddedUUIDForTagging() throws SQLException {
        final Dao<OrmMomentDetailType, Integer> momentDetailTypeDao = getMomentDetailTypeDao();
        momentDetailTypeDao.createOrUpdate(new OrmMomentDetailType(MomentDetailType.TAGGING_ID));

        final Dao<OrmMoment, Integer> ormMomentDao = getDao(OrmMoment.class);
        List<OrmMoment> moments = ormMomentDao.queryForAll();
        for (OrmMoment moment : moments) {
            final OrmMomentDetailType detailType = new OrmMomentDetailType(MomentDetailType.TAGGING_ID);
            if (OrmMoment.NO_ID.equals(moment.getAnalyticsId())) {
                OrmMomentDetail detail = new OrmMomentDetail(detailType, moment);
                detail.setValue(uuidGenerator.generateRandomUUID());
                moment.addMomentDetail(detail);
                moment.setSynced(false);
                ormMomentDao.update(moment);
            }
        }
    }


    private void dropTables(final ConnectionSource connectionSource) throws SQLException {
        TableUtils.dropTable(connectionSource, OrmMoment.class, true);
        TableUtils.dropTable(connectionSource, OrmMomentType.class, true);
        TableUtils.dropTable(connectionSource, OrmMomentDetail.class, true);
        TableUtils.dropTable(connectionSource, OrmMomentDetailType.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurement.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurementType.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurementDetail.class, true);
        TableUtils.dropTable(connectionSource, OrmMeasurementDetailType.class, true);
        TableUtils.dropTable(connectionSource, OrmSynchronisationData.class, true);
    }

    public Dao<OrmMoment, Integer> getMomentDao() throws SQLException {
        if (momentDao == null) {
            momentDao = getDao(OrmMoment.class);
        }
        return momentDao;
    }

    public Dao<OrmMomentType, Integer> getMomentTypeDao() throws SQLException {
        if (momentTypeDao == null) {
            momentTypeDao = getDao(OrmMomentType.class);
        }
        return momentTypeDao;
    }

    public Dao<OrmMomentDetail, Integer> getMomentDetailDao() throws SQLException {
        if (momentDetailDao == null) {
            momentDetailDao = getDao(OrmMomentDetail.class);
        }
        return momentDetailDao;
    }

    public Dao<OrmMomentDetailType, Integer> getMomentDetailTypeDao() throws SQLException {
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

    public Dao<OrmMeasurementType, Integer> getMeasurementTypeDao() throws SQLException {
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

    public Dao<OrmMeasurementDetailType, Integer> getMeasurementDetailTypeDao() throws SQLException {
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

}
