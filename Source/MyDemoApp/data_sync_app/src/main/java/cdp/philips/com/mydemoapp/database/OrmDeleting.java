/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.philips.platform.core.datatypes.ConsentDetail;

import java.sql.SQLException;

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
public class OrmDeleting {

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
    private final Dao<OrmConsentDetail, Integer> consentDetailDao;

    @NonNull
    private final Dao<OrmConsentDetailType, Integer> consentDetailTypeDao;



    public OrmDeleting(@NonNull final Dao<OrmMoment, Integer> momentDao,
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

        this.consentDetailDao = constentDetailsDao;
        this.consentDetailTypeDao = constentDetailTypeDao;
    }

    public void deleteAll() throws SQLException {
        momentDao.executeRawNoArgs("DELETE FROM `ormmoment`");
        momentDetailDao.executeRawNoArgs("DELETE FROM `ormmomentdetail`");
        measurementDao.executeRawNoArgs("DELETE FROM `ormmeasurement`");
        measurementDetailDao.executeRawNoArgs("DELETE FROM `ormmeasurementdetail`");
        synchronisationDataDao.executeRawNoArgs("DELETE FROM `ormsynchronisationdata`");
        consentDao.executeRawNoArgs("DELETE FROM `ormconsent`");
        consentDetailDao.executeRawNoArgs("DELETE FROM `ormconsentdetail`");
    }

    public void ormDeleteMoment(@NonNull final OrmMoment moment) throws SQLException {
        deleteMomentDetails(moment);
        deleteMomentMeasurements(moment);
        deleteSynchronisationData(moment.getSynchronisationData());
        momentDao.delete(moment);
    }

    public void deleteMomentAndMeasurementDetails(@NonNull final OrmMoment ormMoment) throws SQLException {
        deleteMomentMeasurements(ormMoment);
        deleteMomentDetails(ormMoment);
    }

    private void deleteSynchronisationData(@Nullable final OrmSynchronisationData synchronisationData) throws SQLException {
        if (synchronisationData != null) {
            synchronisationDataDao.delete(synchronisationData);
        }
    }

    private void deleteMomentMeasurements(@NonNull final OrmMoment moment) throws SQLException {
        for (OrmMeasurement measurement : moment.getMeasurements()) {
            deleteMeasurementDetails(measurement.getId());
        }
        deleteMeasurements(moment.getId());
    }

    private void deleteMomentDetails(@NonNull final OrmMoment moment) throws SQLException {
        deleteMomentDetails(moment.getId());
    }



    public int deleteMomentDetails(final int id) throws SQLException {
        DeleteBuilder<OrmMomentDetail, Integer> updateBuilder = momentDetailDao.deleteBuilder();
        updateBuilder.where().eq("ormMoment_id", id);

        return updateBuilder.delete();
    }

    public int deleteMeasurements(final int id) throws SQLException {
        DeleteBuilder<OrmMeasurement, Integer> updateBuilder = measurementDao.deleteBuilder();
        updateBuilder.where().eq("ormMoment_id", id);

        return updateBuilder.delete();
    }

    public int deleteMeasurementDetails(final int id) throws SQLException {
        DeleteBuilder<OrmMeasurementDetail, Integer> updateBuilder = measurementDetailDao.deleteBuilder();
        updateBuilder.where().eq("ormMeasurement_id", id);

        return updateBuilder.delete();
    }

    public void deleteConsent(OrmConsent ormConsent) throws SQLException {
        deleteConsentDetails(ormConsent);
        consentDao.delete(ormConsent);
    }

    private void deleteConsentDetails(@NonNull final OrmConsent ormConsent) throws SQLException {
        for (OrmConsentDetail consentDetail : ormConsent.getConsentDetails()) {
            consentDetailDao.delete(consentDetail);
        }
    }

}
