/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.philips.platform.core.datatypes.Settings;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.NotifyDBChangeListener;

import java.sql.SQLException;
import java.util.List;

import cdp.philips.com.mydemoapp.database.table.OrmConsent;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;

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

    @NonNull
    private final Dao<OrmSettings, Integer> settingsDao;

    public OrmUpdating(@NonNull final Dao<OrmMoment, Integer> momentDao,
                       @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                       @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                       @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                       @NonNull final Dao<OrmConsent, Integer> constentDao, @NonNull Dao<OrmSettings, Integer> settingsDao) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.constentDao = constentDao;
        this.settingsDao = settingsDao;
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


    public void updateSettings(List<Settings> settingsList, DBRequestListener dbRequestListener) throws SQLException {
        UpdateBuilder<OrmSettings, Integer> updateBuilder = settingsDao.updateBuilder();
        for(Settings settings:settingsList) {

            updateBuilder.updateColumnValue("value",settings.getValue());
            updateBuilder.where().eq("type", settings.getType());

            updateBuilder.update();
        }
    }
}
