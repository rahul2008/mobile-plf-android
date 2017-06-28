/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database;

import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmMeasurement;
import com.philips.platform.dscdemo.database.table.OrmMeasurementDetail;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroup;
import com.philips.platform.dscdemo.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmMomentDetail;
import com.philips.platform.dscdemo.database.table.OrmSettings;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;
import com.philips.platform.dscdemo.utility.NotifyDBRequestListener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;


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
    private final Dao<OrmSettings, Integer> settingsDao;

    @NonNull
    private final Dao<OrmConsentDetail, Integer> ormConsentDetailDao;

    @NonNull
    private final Dao<OrmMeasurementGroup, Integer> measurementGroupDao;

    @NonNull
    private final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetailsDao;

    @NonNull
    private final Dao<OrmSynchronisationData, Integer> synchronisationDataDao;

    @NonNull
    private final Dao<OrmDCSync, Integer> dcSyncDao;



    public OrmUpdating(@NonNull final Dao<OrmMoment, Integer> momentDao,
                       @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                       @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                       @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                       @NonNull Dao<OrmSettings, Integer> settingsDao,
                       @NonNull Dao<OrmConsentDetail, Integer> ormConsentDetailDao, @NonNull Dao<OrmDCSync, Integer> dcSyncDao,
                       @NonNull final Dao<OrmMeasurementGroup, Integer> measurementGroup,
                       @NonNull final Dao<OrmSynchronisationData, Integer> synchronisationDataDao,
                       @NonNull final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetails) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.settingsDao = settingsDao;
        this.ormConsentDetailDao = ormConsentDetailDao;
        this.dcSyncDao = dcSyncDao;
        this.measurementGroupDao = measurementGroup;
        this.measurementGroupDetailsDao = measurementGroupDetails;
        this.synchronisationDataDao = synchronisationDataDao;
    }

    public void refreshMoment(OrmMoment moment) throws SQLException {
        momentDao.refresh(moment);
    }

    public void refreshMeasurement(OrmMeasurement measurement) throws SQLException {
        measurementDao.refresh(measurement);
    }



    public void refreshMeasurementDetail(OrmMeasurementDetail measurementDetail) throws SQLException {
        measurementDetailDao.refresh(measurementDetail);
    }

    public void refreshMomentDetail(OrmMomentDetail momentDetail) throws SQLException {
        momentDetailDao.refresh(momentDetail);
    }

    public int updateSubjectIdOfMoment(String subjectId, String clientGeneratedSubjectId) throws SQLException {
        UpdateBuilder<OrmMoment, Integer> updateBuilder = momentDao.updateBuilder();
        updateBuilder.updateColumnValue("subjectId", subjectId);
        updateBuilder.where().eq("subjectId", clientGeneratedSubjectId);

        return updateBuilder.update();
    }


    public void updateSettings(OrmSettings ormSettings) throws SQLException {
            settingsDao.update(ormSettings);
    }

    public void updateDCSync(int tableID,boolean isSynced) throws SQLException{

        UpdateBuilder<OrmDCSync, Integer> updateBuilder = dcSyncDao.updateBuilder();
        updateBuilder.updateColumnValue("isSynced",isSynced);
        updateBuilder.where().eq("tableID", tableID);

        updateBuilder.update();
    }

    public void updateMoment(OrmMoment moment) throws SQLException {
        assureSynchronisationDataIsUpdated(moment.getSynchronisationData());
        momentDao.update(moment);
        assureMomentDetailsAreUpdated(moment.getMomentDetails());
        assureMeasurementGroupsAreUpdated(moment);
        // assureMeasurementsAreSaved(moment.getMeasurements());
    }

    private void assureMeasurementGroupsAreUpdated(final OrmMoment moment) throws SQLException {
        Collection<? extends OrmMeasurementGroup> measurementGroups = moment.getMeasurementGroups();
        for (OrmMeasurementGroup group : measurementGroups) {
            updateMeasurementGroup(group);
            assureMeasurementGroupsInsideAreUpdated(group);
        }
    }

    private void assureMeasurementGroupsInsideAreUpdated(final OrmMeasurementGroup measurementGroup) throws SQLException {
        if(measurementGroup!=null) {
            ArrayList<? extends OrmMeasurementGroup> measurementGroups = new ArrayList<>(measurementGroup.getMeasurementGroups());
            for (OrmMeasurementGroup group : measurementGroups) {
                updateMeasurementGroupWithinGroup(group);
            }
        }
    }

    private void updateMeasurementGroupWithinGroup(final OrmMeasurementGroup group) throws SQLException {
        measurementGroupDao.update(group);
        assureMeasurementsAreUpdated(group.getMeasurements());
        assureMeasurementGroupDetailsAreUpdated(group.getMeasurementGroupDetails());
    }

    private void assureMeasurementGroupDetailsAreUpdated(final Collection<? extends OrmMeasurementGroupDetail> measurementGroupDetails) throws SQLException {
        for (OrmMeasurementGroupDetail measurementGroupDetail : measurementGroupDetails) {
            updateMeasurementGroupDetail(measurementGroupDetail);
        }
    }

    private void updateMeasurementGroupDetail(final OrmMeasurementGroupDetail measurementGroupDetail) throws SQLException {
        measurementGroupDetailsDao.update(measurementGroupDetail);
    }

    private void updateMeasurementGroup(final OrmMeasurementGroup measurementGroup) throws SQLException {
        if(measurementGroup!=null) {
            measurementGroupDao.update(measurementGroup);
            assureMeasurementsAreUpdated(measurementGroup.getMeasurements());
            assureMeasurementGroupDetailsAreUpdated(measurementGroup.getMeasurementGroupDetails());
        }
    }

    private void assureMeasurementsAreUpdated(final Collection<? extends OrmMeasurement> measurements) throws SQLException {
        for (OrmMeasurement measurement : measurements) {
            updateMeasurement(measurement);
        }
    }

    private void updateMeasurement(final OrmMeasurement measurement) throws SQLException {
        measurementDao.update(measurement);
        assureMeasurementDetailsAreUpdated(measurement.getMeasurementDetails());
    }

    private void assureMeasurementDetailsAreUpdated(final Collection<? extends OrmMeasurementDetail> measurementDetails) throws SQLException {
        for (OrmMeasurementDetail measurementDetail : measurementDetails) {
            updateMeasurementDetail(measurementDetail);
        }
    }

    private void updateMeasurementDetail(final OrmMeasurementDetail measurementDetail) throws SQLException {
        measurementDetailDao.update(measurementDetail);
    }

    private void assureMomentDetailsAreUpdated(final Collection<? extends OrmMomentDetail> momentDetails) throws SQLException {
        for (OrmMomentDetail momentDetail : momentDetails) {
            updateMomentDetail(momentDetail);
        }
    }

    private void updateMomentDetail(final OrmMomentDetail momentDetail) throws SQLException {
        momentDetailDao.update(momentDetail);
    }

    private void assureSynchronisationDataIsUpdated(final OrmSynchronisationData synchronisationData) throws SQLException {
        if (synchronisationData != null) {
            updateSynchronisationData(synchronisationData);
        }
    }

    private void updateSynchronisationData(final OrmSynchronisationData synchronisationData) throws SQLException {
        synchronisationDataDao.update(synchronisationData);
    }

    public void updateConsentDetails(ConsentDetail consentDetail) throws SQLException {

        UpdateBuilder<OrmConsentDetail, Integer> updateBuilder = ormConsentDetailDao.updateBuilder();
            updateBuilder.updateColumnValue("status",consentDetail.getStatus());
            updateBuilder.updateColumnValue("version",consentDetail.getVersion());
            updateBuilder.updateColumnValue("deviceIdentificationNumber",consentDetail.getDeviceIdentificationNumber());
            updateBuilder.where().eq("type", consentDetail.getType());

            updateBuilder.update();

    }

    public boolean updateMoments(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) {
        try {
            momentDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Moment moment : moments) {
                        moment.setSynced(false);
                        OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
                        updateMoment(ormMoment);
                        refreshMoment(ormMoment);
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new NotifyDBRequestListener().notifyFailure(e,dbRequestListener);
            return false;
        }
        return true;
    }

}
