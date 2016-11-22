/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.datasevices.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmConsent;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmConsentDetailType;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmMeasurement;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmMeasurementDetail;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.datasevices.database.table.OrmMomentDetail;

import java.sql.SQLException;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmUpdating {

    private final Dao<OrmMoment, Integer> momentDao;
    private final Dao<OrmMomentDetail, Integer> momentDetailDao;
    private final Dao<OrmMeasurement, Integer> measurementDao;
    private final Dao<OrmMeasurementDetail, Integer> measurementDetailDao;

    @NonNull
    private final Dao<OrmConsent, Integer> constentDao;

    //TODO: Spoorti - Remove Unused
    @NonNull
    private final Dao<OrmConsentDetail, Integer> constentDetailsDao;

    //TODO: Spoorti - Remove Unused
    @NonNull
    private final Dao<OrmConsentDetailType, Integer> constentDetailTypeDao;

    public OrmUpdating(@NonNull final Dao<OrmMoment, Integer> momentDao,
                       @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                       @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                       @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                       @NonNull final Dao<OrmConsent, Integer> constentDao,
                       @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
                       @NonNull final Dao<OrmConsentDetailType, Integer> constentDetailTypeDao) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.constentDao = constentDao;
        this.constentDetailsDao = constentDetailsDao;

        this.constentDetailTypeDao = constentDetailTypeDao;
    }

    public void updateMoment(OrmMoment moment) throws SQLException {
        momentDao.refresh(moment);
    }

    public void updateMeasurement(OrmMeasurement measurement) throws SQLException {
        measurementDao.refresh(measurement);
    }

    public void updateConsent(OrmConsent consent) throws SQLException{
        constentDao.refresh(consent);
    }

    public void updateMeasurementDetail(OrmMeasurementDetail measurementDetail) throws SQLException {
        measurementDetailDao.refresh(measurementDetail);
    }

    public void updateMomentDetail(OrmMomentDetail momentDetail) throws SQLException {
        momentDetailDao.refresh(momentDetail);
    }

    public int updateSubjectIdOfMoment(String subjectId, String clientGeneratedSubjectId) throws SQLException {
        UpdateBuilder<OrmMoment, Integer> updateBuilder = momentDao.updateBuilder();
        updateBuilder.updateColumnValue("subjectId", subjectId);
        updateBuilder.where().eq("subjectId", clientGeneratedSubjectId);

        return updateBuilder.update();
    }


}
