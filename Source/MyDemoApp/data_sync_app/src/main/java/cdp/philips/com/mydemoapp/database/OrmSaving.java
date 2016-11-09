/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Collection;

import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetailType;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;

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

    @NonNull
    private final Dao<OrmConsent, Integer> consentDao;

    @NonNull
    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;

    @NonNull
    private final Dao<OrmConsentDetailType, Integer> consentDetailTypeDao;


    public OrmSaving(@NonNull final Dao<OrmMoment, Integer> momentDao,
                     @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                     @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                     @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                     @NonNull final Dao<OrmSynchronisationData, Integer> synchronisationDataDao,
                     @NonNull final Dao<OrmConsent, Integer> constentDao,
                     @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
                     @NonNull final Dao<OrmConsentDetailType, Integer> constentDetailTypeDao) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.synchronisationDataDao = synchronisationDataDao;

        this.consentDao = constentDao;
        this.consentDetailsDao = constentDetailsDao;
        this.consentDetailTypeDao = constentDetailTypeDao;
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

    public void saveConsent(OrmConsent consent) throws SQLException {
        consentDao.createOrUpdate(consent);
        assureConsentDetailsAreSaved((Collection<? extends OrmConsentDetail>) consent.getConsentDetails());
    }

    private void assureConsentDetailsAreSaved(Collection<? extends OrmConsentDetail> consentDetails) throws SQLException {
        for (OrmConsentDetail consentDetail : consentDetails) {
            saveConsentDetail(consentDetail);
        }
    }

    public void saveConsentDetail(OrmConsentDetail consentDetail) throws SQLException {
        consentDetailsDao.createOrUpdate(consentDetail);
    }

}
