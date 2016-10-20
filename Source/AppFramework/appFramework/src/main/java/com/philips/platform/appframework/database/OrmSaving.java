/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;
import java.sql.SQLException;
import java.util.Collection;

import com.philips.platform.appframework.database.table.OrmMeasurement;
import com.philips.platform.appframework.database.table.OrmMeasurementDetail;
import com.philips.platform.appframework.database.table.OrmMoment;
import com.philips.platform.appframework.database.table.OrmMomentDetail;
import com.philips.platform.appframework.database.table.OrmSynchronisationData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmSaving {

    @NonNull
    private final Dao<OrmMoment, Integer> momentDao;

    @NonNull
    private final Dao<OrmMomentDetail, Integer> momentDetailDao;

    @NonNull
    private final Dao<OrmMeasurement, Integer> measurementDao;

    @NonNull
    private final Dao<OrmMeasurementDetail, Integer> measurementDetailDao;

    @NonNull
    private final Dao<OrmSynchronisationData, Integer> synchronisationDataDao;



    public OrmSaving(@NonNull final Dao<OrmMoment, Integer> momentDao,
                     @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                     @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                     @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                     @NonNull final Dao<OrmSynchronisationData, Integer> synchronisationDataDao) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.synchronisationDataDao = synchronisationDataDao;

    }

    public void saveMoment(OrmMoment moment) throws SQLException {
        assureSynchronisationDataIsSaved(moment.getSynchronisationData());
        momentDao.createOrUpdate(moment);
        assureMeasurementsAreSaved(moment.getMeasurements());
        assureMomentDetailsAreSaved(moment.getMomentDetails());
    }


    private void saveSynchronisationData(final @NonNull OrmSynchronisationData synchronisationData) throws SQLException {
        synchronisationDataDao.createOrUpdate(synchronisationData);
    }

    public void saveMomentDetail(OrmMomentDetail momentDetail) throws SQLException {
        momentDetailDao.createOrUpdate(momentDetail);
    }

    public void saveMeasurement(OrmMeasurement measurement) throws SQLException {
        measurementDao.createOrUpdate(measurement);
        assureMeasurementDetailsAreSaved(measurement.getMeasurementDetails());
    }

    public void saveMeasurementDetail(OrmMeasurementDetail measurementDetail) throws SQLException {
        measurementDetailDao.createOrUpdate(measurementDetail);
    }



    private void assureMomentDetailsAreSaved(final Collection<? extends OrmMomentDetail> momentDetails) throws SQLException {
        for (OrmMomentDetail momentDetail : momentDetails) {
            saveMomentDetail(momentDetail);
        }
    }

    private void assureMeasurementsAreSaved(final Collection<? extends OrmMeasurement> measurements) throws SQLException {
        for (OrmMeasurement measurement : measurements) {
            saveMeasurement(measurement);
        }
    }

    private void assureMeasurementDetailsAreSaved(final Collection<? extends OrmMeasurementDetail> measurementDetails) throws SQLException {
        for (OrmMeasurementDetail measurementDetail : measurementDetails) {
            saveMeasurementDetail(measurementDetail);
        }
    }

    private void assureSynchronisationDataIsSaved(@Nullable final OrmSynchronisationData synchronisationData) throws SQLException {
        if (synchronisationData != null) {
            saveSynchronisationData(synchronisationData);
        }
    }


}
