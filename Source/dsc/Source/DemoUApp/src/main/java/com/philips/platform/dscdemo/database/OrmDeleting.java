/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.dscdemo.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.ConsentDetailStatusType;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dscdemo.consents.ConsentDetailType;
import com.philips.platform.dscdemo.database.table.OrmCharacteristics;
import com.philips.platform.dscdemo.database.table.OrmConsentDetail;
import com.philips.platform.dscdemo.database.table.OrmDCSync;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmInsightMetaData;
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
    private final Dao<OrmDCSync,Integer> syncDao;

    @NonNull
    private final Dao<OrmInsight, Integer> ormInsightDao;

    @NonNull
    private final Dao<OrmInsightMetaData, Integer> ormInsightMetadataDao;


    public OrmDeleting(@NonNull final Dao<OrmMoment, Integer> momentDao,
                       @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                       @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                       @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                       @NonNull final Dao<OrmSynchronisationData, Integer> synchronisationDataDao,
                       @NonNull final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetailDao,
                       @NonNull final Dao<OrmMeasurementGroup, Integer> measurementGroupsDao,
                       @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
                       @NonNull Dao<OrmCharacteristics, Integer> characteristicsesDao, Dao<OrmSettings, Integer> settingsDao, Dao<OrmDCSync, Integer> syncDao, @NonNull Dao<OrmInsight, Integer> ormInsightDao,
                       @NonNull Dao<OrmInsightMetaData, Integer> ormInsightMetadataDao) {

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
        this.syncDao = syncDao;
        this.ormInsightDao = ormInsightDao;
        this.ormInsightMetadataDao = ormInsightMetadataDao;
    }

    public void deleteAll() throws SQLException {
        momentDao.executeRawNoArgs("DELETE FROM `ormmoment`");
        momentDetailDao.executeRawNoArgs("DELETE FROM `ormmomentdetail`");
        measurementDao.executeRawNoArgs("DELETE FROM `ormmeasurement`");
        measurementDetailDao.executeRawNoArgs("DELETE FROM `ormmeasurementdetail`");
        synchronisationDataDao.executeRawNoArgs("DELETE FROM `ormsynchronisationdata`");
        consentDetailDao.executeRawNoArgs("DELETE FROM `ormconsentdetail`");
        characteristicsDao.executeRawNoArgs("DELETE FROM `ormcharacteristics`");
        settingsDao.executeRawNoArgs("DELETE FROM `ormsettings`");
        ormInsightDao.executeRawNoArgs("DELETE FROM `orminsight`");
        ormInsightMetadataDao.executeRawNoArgs("DELETE FROM `orminsightmetaData`");
        syncDao.executeRawNoArgs("DELETE FROM `ormdcsync`"); //OrmDCSync


        insertDefaultConsentAndSyncBit();
    }

    private void insertDefaultConsentAndSyncBit() {
        try {
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.SLEEP, ConsentDetailStatusType.REFUSED.getDescription(),ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.TEMPERATURE, ConsentDetailStatusType.REFUSED.getDescription(),ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            consentDetailDao.createOrUpdate(new OrmConsentDetail(ConsentDetailType.WEIGHT, ConsentDetailStatusType.REFUSED.getDescription(),ConsentDetail.DEFAULT_DOCUMENT_VERSION,
                    ConsentDetail.DEFAULT_DEVICE_IDENTIFICATION_NUMBER));
            syncDao.createOrUpdate(new OrmDCSync(SyncType.CONSENT.getId(), SyncType.CONSENT.getDescription(), true));

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

    public int deleteAllExpiredMoments() throws SQLException {

        Long currentUnixTimestamp = System.currentTimeMillis();

        measurementDetailDao.executeRaw("DELETE FROM 'ormmeasurementdetail' WHERE ormMeasurement_id IN" +
                " (SELECT id FROM `ormmeasurement` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? ))))", currentUnixTimestamp.toString());

        measurementDao.executeRaw("DELETE FROM `ormmeasurement` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? )))", currentUnixTimestamp.toString());

        synchronisationDataDao.executeRaw("DELETE FROM `ormsynchronisationdata` WHERE guid IN" +
                " (SELECT synchronisationData_id FROM `ormmoment` WHERE expirationDate < ? )", currentUnixTimestamp.toString());

        momentDetailDao.executeRaw("DELETE FROM `ormmomentdetail` WHERE ormmoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? )", currentUnixTimestamp.toString());

        measurementGroupDetailDao.executeRaw("DELETE FROM `ormmeasurementgroupdetail` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? )))", currentUnixTimestamp.toString());

        measurementGroupsDao.executeRaw("DELETE FROM `ormmeasurementgroup` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? ))", currentUnixTimestamp.toString());

        measurementGroupDetailDao.executeRaw("DELETE FROM `ormmeasurementgroupdetail` WHERE ormMeasurementGroup_id IN" +
                " (SELECT id FROM `ormmeasurementgroup` WHERE ormMoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? ))", currentUnixTimestamp.toString());

        measurementGroupsDao.executeRaw("DELETE FROM `ormmeasurementgroup` WHERE ormMoment_id IN" +
                " (SELECT id FROM `ormmoment` WHERE expirationDate < ? )", currentUnixTimestamp.toString());

        return momentDao.executeRaw("DELETE FROM `ormmoment` WHERE expirationDate < ?", currentUnixTimestamp.toString());
    }

    public void deleteAllConsentDetails() throws  SQLException{
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
        for(OrmMeasurementGroup ormMeasurementGroup : measurementGroups) {
            deleteMeasurementGroupDetails(ormMeasurementGroup.getId());
            deleteMeasurements(ormMeasurementGroup);
            deleteGroupsInside(ormMeasurementGroup.getMeasurementGroups());
            deleteMeasurementGroupByMeasurementGroup(ormMeasurementGroup.getId());
        }
        deleteMeasurementGroupByMoment(moment.getId());
    }

    private void deleteGroupsInside(Collection<? extends OrmMeasurementGroup> measurementGroups) throws SQLException {
        for(OrmMeasurementGroup group : measurementGroups) {
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

    public void deleteCharacteristics() throws SQLException{
        DeleteBuilder<OrmCharacteristics, Integer> characteristicsDeleteBuilder = characteristicsDao.deleteBuilder();
        characteristicsDeleteBuilder.delete();
    }

    public void deleteSettings() throws  SQLException{

        DeleteBuilder<OrmSettings, Integer> ormSettingsDeleteBuilder = settingsDao.deleteBuilder();
        ormSettingsDeleteBuilder.delete();
    }

    public boolean deleteMoments(final List<? extends Moment> moments, DBRequestListener<Moment> dbRequestListener) {

        try {
            momentDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Moment moment : moments) {
                        //moment.setSynced(false);
                       // OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
                        ormDeleteMoment((OrmMoment)moment);
                        // momentDao.refresh(ormMoment);
                    }

                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            //dbRequestListener.onFailure(e);
            new NotifyDBRequestListener().notifyFailure(e,dbRequestListener);
            return false;
        }
        return true;
    }

    //Insights
    public boolean deleteInsights(final List<? extends Insight> insights, DBRequestListener<? extends Insight> dbRequestListener) {
        try {
            momentDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Insight insight : insights) {
                        deleteInsight((OrmInsight) insight);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new NotifyDBRequestListener().notifyFailure(e, dbRequestListener);
            return false;
        }
        return true;
    }

    public int deleteInsightMetaData(@NonNull final OrmInsight ormInsight) throws SQLException {
        DeleteBuilder<OrmInsightMetaData, Integer> deleteBuilder = ormInsightMetadataDao.deleteBuilder();
        deleteBuilder.where().eq("ormInsight_id", ormInsight.getId());
        return deleteBuilder.delete();
    }

    public void deleteInsight(@NonNull final OrmInsight insight) throws SQLException {
        if (insight.getInsightMetaData() != null && insight.getInsightMetaData().size() > 0)
            deleteInsightMetaData(insight);
        deleteSynchronisationData(insight.getSynchronisationData());
        ormInsightDao.delete(insight);
    }
    public int deleteSyncBit(SyncType type) throws SQLException{
        DeleteBuilder<OrmDCSync, Integer> deleteBuilder = syncDao.deleteBuilder();
        deleteBuilder.where().eq("tableid",type.getId());
        return deleteBuilder.delete();
    }

    public int deleteSyncedInsights() throws SQLException {
        DeleteBuilder<OrmInsight, Integer> deleteBuilder = ormInsightDao.deleteBuilder();
        deleteBuilder.where().eq("synced", true);
        return deleteBuilder.delete();
    }

    public int deleteSyncedMoments() throws SQLException {
        DeleteBuilder<OrmMoment, Integer> deleteBuilder = momentDao.deleteBuilder();
        deleteBuilder.where().eq("synced", true);
        return deleteBuilder.delete();
    }
}
