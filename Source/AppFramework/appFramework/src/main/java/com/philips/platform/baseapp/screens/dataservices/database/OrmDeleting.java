/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.dataservices.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.philips.platform.baseapp.screens.dataservices.consents.ConsentDetailType;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmCharacteristics;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmConsentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurement;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroup;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMeasurementGroupDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMoment;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmMomentDetail;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSettings;
import com.philips.platform.baseapp.screens.dataservices.database.table.OrmSynchronisationData;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;


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
    private final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetailDao;

    @NonNull
    private final Dao<OrmSynchronisationData, Integer> synchronisationDataDao;

    @NonNull
    private final Dao<OrmMeasurementGroup, Integer> measurementGroupsDao;

    @NonNull
    private final Dao<OrmConsentDetail, Integer> consentDetailDao;


    @NonNull
    private final Dao<OrmCharacteristics, Integer> characteristicsDao;

    @NonNull
    private final Dao<OrmSettings, Integer> settingsDao;


    public OrmDeleting(@NonNull final Dao<OrmMoment, Integer> momentDao,
                       @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                       @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                       @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                       @NonNull final Dao<OrmSynchronisationData, Integer> synchronisationDataDao,
                       @NonNull final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetailDao,
                       @NonNull final Dao<OrmMeasurementGroup, Integer> measurementGroupsDao,
                       @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
                       @NonNull Dao<OrmCharacteristics, Integer> characteristicsesDao, Dao<OrmSettings, Integer> settingsDao) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.synchronisationDataDao = synchronisationDataDao;
        this.measurementGroupDetailDao = measurementGroupDetailDao;
        this.measurementGroupsDao = measurementGroupsDao;

        this.consentDetailDao = constentDetailsDao;
        this.characteristicsDao = characteristicsesDao;
        this.settingsDao = settingsDao;
    }

    public void deleteAll() throws SQLException {
        momentDao.executeRawNoArgs("DELETE FROM `ormmoment`");
        momentDetailDao.executeRawNoArgs("DELETE FROM `ormmomentdetail`");
        measurementDao.executeRawNoArgs("DELETE FROM `ormmeasurement`");
        measurementDetailDao.executeRawNoArgs("DELETE FROM `ormmeasurementdetail`");
        synchronisationDataDao.executeRawNoArgs("DELETE FROM `ormsynchronisationdata`");
        consentDetailDao.executeRawNoArgs("DELETE FROM `ormconsentdetail`");
        setDefaultConsentDetails();
        characteristicsDao.executeRawNoArgs("DELETE FROM `ormcharacteristics`");
        settingsDao.executeRawNoArgs("DELETE FROM `ormsettings`");
    }

    private void setDefaultConsentDetails() {

        try {
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED.getDescription(), ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllMoments() throws SQLException {
        momentDao.executeRawNoArgs("DELETE FROM `ormmoment`");
        momentDetailDao.executeRawNoArgs("DELETE FROM `ormmomentdetail`");
        measurementDao.executeRawNoArgs("DELETE FROM `ormmeasurement`");
        measurementDetailDao.executeRawNoArgs("DELETE FROM `ormmeasurementdetail`");
        synchronisationDataDao.executeRawNoArgs("DELETE FROM `ormsynchronisationdata`");
    }

    public void deleteAllConsentDetails() throws SQLException {
        consentDetailDao.executeRawNoArgs("DELETE FROM `ormconsentdetail`");
    }

    public void ormDeleteMoment(@NonNull final OrmMoment moment) throws SQLException {
        deleteMomentDetails(moment);
        deleteMeasurementGroups(moment);
        deleteSynchronisationData(moment.getSynchronisationData());
        momentDao.delete(moment);
    }

    public void deleteMeasurementGroups(OrmMoment moment) throws SQLException {
        ArrayList<? extends OrmMeasurementGroup> measurementGroups = new ArrayList<>(moment.getMeasurementGroups());
        for (OrmMeasurementGroup ormMeasurementGroup : measurementGroups) {
            deleteMeasurementGroupDetails(ormMeasurementGroup.getId());
            deleteMeasurements(ormMeasurementGroup);
            deleteGroupsInside(ormMeasurementGroup.getMeasurementGroups());
            deleteMeasurementGroupByMeasurementGroup(ormMeasurementGroup.getId());
        }
        deleteMeasurementGroupByMoment(moment.getId());
    }

    private void deleteGroupsInside(Collection<? extends OrmMeasurementGroup> measurementGroups) throws SQLException {
        for (OrmMeasurementGroup group : measurementGroups) {
            deleteMeasurementGroupDetails(group.getId());
            deleteMeasurements(group);
            deleteMeasurementGroupByMeasurementGroup(group.getId());
        }
    }

    public void deleteMomentAndMeasurementGroupDetails(@NonNull final OrmMoment ormMoment) throws SQLException {
        deleteMeasurementGroups(ormMoment);
        deleteMomentDetails(ormMoment);
    }

    private void deleteSynchronisationData(@Nullable final OrmSynchronisationData synchronisationData) throws SQLException {
        if (synchronisationData != null) {
            synchronisationDataDao.delete(synchronisationData);
        }
    }

    private void deleteMeasurements(@NonNull final OrmMeasurementGroup measurementGroup) throws SQLException {
        for (OrmMeasurement measurement : measurementGroup.getMeasurements()) {
            deleteMeasurementDetails(measurement.getId());
        }
        deleteMeasurements(measurementGroup.getId());
    }

    private void deleteMeasurementGroup(@NonNull final OrmMeasurementGroup measurementGroup) throws SQLException {
        for (OrmMeasurementGroupDetail measurementGroupDetail : measurementGroup.getMeasurementGroupDetails()) {
            deleteMeasurementGroupDetails(measurementGroupDetail.getId());
        }
        deleteMeasurementGroupByMeasurementGroup(measurementGroup.getId());
    }

    private int deleteMeasurementGroupByMeasurementGroup(int id) throws SQLException {
        DeleteBuilder<OrmMeasurementGroup, Integer> updateBuilder = measurementGroupsDao.deleteBuilder();
        updateBuilder.where().eq("ormMeasurementGroup_id", id);

        return updateBuilder.delete();
    }

    private int deleteMeasurementGroupByMoment(int id) throws SQLException {
        DeleteBuilder<OrmMeasurementGroup, Integer> updateBuilder = measurementGroupsDao.deleteBuilder();
        updateBuilder.where().eq("ormMoment_id", id);

        return updateBuilder.delete();
    }

    private int deleteMeasurementGroupDetails(int id) throws SQLException {
        DeleteBuilder<OrmMeasurementGroupDetail, Integer> updateBuilder = measurementGroupDetailDao.deleteBuilder();
        updateBuilder.where().eq("ormMeasurementGroup_id", id);

        return updateBuilder.delete();
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
        updateBuilder.where().eq("ormMeasurementGroup_id", id);

        return updateBuilder.delete();
    }

    public int deleteMeasurementDetails(final int id) throws SQLException {
        DeleteBuilder<OrmMeasurementDetail, Integer> updateBuilder = measurementDetailDao.deleteBuilder();
        updateBuilder.where().eq("ormMeasurement_id", id);

        return updateBuilder.delete();
    }


    public void deleteConsents() throws SQLException {
        DeleteBuilder<OrmConsentDetail, Integer> ormConsentDetailDeleteBuilder = consentDetailDao.deleteBuilder();
        ormConsentDetailDeleteBuilder.delete();
    }

    public void deleteCharacteristics() throws SQLException {
        DeleteBuilder<OrmCharacteristics, Integer> characteristicsDeleteBuilder = characteristicsDao.deleteBuilder();
        characteristicsDeleteBuilder.delete();
    }

    public void deleteSettings() throws SQLException {

        DeleteBuilder<OrmSettings, Integer> ormSettingsDeleteBuilder = settingsDao.deleteBuilder();
        ormSettingsDeleteBuilder.delete();
    }
}
