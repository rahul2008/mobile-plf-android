/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.j256.ormlite.dao.Dao;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import cdp.philips.com.mydemoapp.database.table.OrmCharacteristics;
import cdp.philips.com.mydemoapp.database.table.OrmConsentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmDCSync;
import cdp.philips.com.mydemoapp.database.table.OrmInsight;
import cdp.philips.com.mydemoapp.database.table.OrmInsightMetaData;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurement;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroup;
import cdp.philips.com.mydemoapp.database.table.OrmMeasurementGroupDetail;
import cdp.philips.com.mydemoapp.database.table.OrmMoment;
import cdp.philips.com.mydemoapp.database.table.OrmMomentDetail;
import cdp.philips.com.mydemoapp.database.table.OrmSettings;
import cdp.philips.com.mydemoapp.database.table.OrmSynchronisationData;
import cdp.philips.com.mydemoapp.utility.NotifyDBRequestListener;

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
    private final Dao<OrmMeasurementGroup, Integer> measurementGroupDao;

    @NonNull
    private final Dao<OrmMeasurementDetail, Integer> measurementDetailDao;

    @NonNull
    private final Dao<OrmSynchronisationData, Integer> synchronisationDataDao;

    @NonNull
    private final Dao<OrmConsentDetail, Integer> consentDetailsDao;

    @NonNull
    private final Dao<OrmCharacteristics, Integer> characteristicsesDao;

    @NonNull
    private final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetailsDao;

    @NonNull
    private final Dao<OrmSettings, Integer> settingsDao;

    @NonNull
    private final Dao<OrmInsight, Integer> insightsDao;

    @NonNull
    private final Dao<OrmInsightMetaData, Integer> insightMetadataDao;

    @NonNull
    private final Dao<OrmDCSync, Integer> dcSyncDao;

    public OrmSaving(@NonNull final Dao<OrmMoment, Integer> momentDao,
                     @NonNull final Dao<OrmMomentDetail, Integer> momentDetailDao,
                     @NonNull final Dao<OrmMeasurement, Integer> measurementDao,
                     @NonNull final Dao<OrmMeasurementDetail, Integer> measurementDetailDao,
                     @NonNull final Dao<OrmSynchronisationData, Integer> synchronisationDataDao,
                     @NonNull final Dao<OrmConsentDetail, Integer> constentDetailsDao,
                     @NonNull final Dao<OrmMeasurementGroup, Integer> measurementGroup,
                     @NonNull final Dao<OrmMeasurementGroupDetail, Integer> measurementGroupDetails,
                     @NonNull final Dao<OrmCharacteristics, Integer> characteristicsesDao,
                     @NonNull Dao<OrmSettings, Integer> settingsDao,
                     @NonNull Dao<OrmInsight, Integer> insightsDao,
                     @NonNull Dao<OrmInsightMetaData, Integer> insightMetadataDao, @NonNull Dao<OrmDCSync, Integer> dcSyncDao) {
        this.momentDao = momentDao;
        this.momentDetailDao = momentDetailDao;
        this.measurementDao = measurementDao;
        this.measurementDetailDao = measurementDetailDao;
        this.synchronisationDataDao = synchronisationDataDao;

        this.consentDetailsDao = constentDetailsDao;
        this.measurementGroupDao = measurementGroup;
        this.measurementGroupDetailsDao = measurementGroupDetails;
        this.characteristicsesDao = characteristicsesDao;
        this.settingsDao = settingsDao;
        this.insightsDao = insightsDao;
        this.insightMetadataDao = insightMetadataDao;
        this.dcSyncDao = dcSyncDao;
    }

    public void saveMoment(OrmMoment moment) throws SQLException {
        momentDao.createOrUpdate(moment);
        assureSynchronisationDataIsSaved(moment.getSynchronisationData());
        assureMomentDetailsAreSaved(moment.getMomentDetails());
        assureMeasurementGroupsAreSaved(moment.getMeasurementGroups());
        // assureMeasurementsAreSaved(moment.getMeasurements());
    }


    private void saveSynchronisationData(final @NonNull OrmSynchronisationData synchronisationData) throws SQLException {
        synchronisationDataDao.createOrUpdate(synchronisationData);
    }

    public void saveMomentDetail(OrmMomentDetail momentDetail) throws SQLException {
        momentDetailDao.createOrUpdate(momentDetail);
    }

    private void assureMomentDetailsAreSaved(final Collection<? extends OrmMomentDetail> momentDetails) throws SQLException {
        for (OrmMomentDetail momentDetail : momentDetails) {
            saveMomentDetail(momentDetail);
        }
    }

    private void assureMeasurementGroupsAreSaved(Collection<? extends OrmMeasurementGroup> measurementGroups) throws SQLException {
        for (OrmMeasurementGroup group : measurementGroups) {
            measurementGroupDao.createOrUpdate(group);
            if (group.getMeasurementGroups() != null && !group.getMeasurementGroups().isEmpty()) {
                // Recursively save children groups
                assureMeasurementGroupsAreSaved(group.getMeasurementGroups());
            }
            saveDetails(group.getMeasurementGroupDetails());
            saveMeasurements(group.getMeasurements());
        }
    }

    private void saveMeasurements(Collection<? extends OrmMeasurement> measurements) throws SQLException {
        for (OrmMeasurement current : measurements) {
            measurementDao.createOrUpdate(current);
        }
    }

    private void saveDetails(Collection<? extends OrmMeasurementGroupDetail> momentDetails) throws SQLException {
        for (OrmMeasurementGroupDetail current : momentDetails) {
            measurementGroupDetailsDao.createOrUpdate(current);
        }
    }

    private void assureSynchronisationDataIsSaved(@Nullable final OrmSynchronisationData synchronisationData) throws SQLException {
        if (synchronisationData != null) {
            saveSynchronisationData(synchronisationData);
        }
    }

    public void saveConsentDetail(OrmConsentDetail consentDetail) throws SQLException {
        consentDetailsDao.createOrUpdate(consentDetail);
    }

    //User AppUserCharacteristics
    public void saveCharacteristics(OrmCharacteristics ormCharacteristics) throws SQLException {
        characteristicsesDao.createOrUpdate(ormCharacteristics);
    }

    public void saveSettings(OrmSettings settings) throws SQLException {
        settingsDao.createOrUpdate(settings);
    }

    public boolean saveMoments(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {

        try {
            momentDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Moment moment : moments) {
                        OrmMoment ormMoment = OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
                        saveMoment(ormMoment);
                        // momentDao.refresh(ormMoment);
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

    //Insights
    public boolean saveInsights(final List<Insight> insights, DBRequestListener<Insight> dbRequestListener) {
        try {
            insightsDao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Insight insight : insights) {
                        OrmInsight ormInsight = OrmTypeChecking.checkOrmType(insight, OrmInsight.class);
                        assureSynchronisationDataIsSaved(ormInsight.getSynchronisationData());
                        insightsDao.createOrUpdate(ormInsight);
                        assureInsightMetaDataSaved(ormInsight.getInsightMetaData());
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

    private void assureInsightMetaDataSaved(final Collection<? extends OrmInsightMetaData> insightMetaDatas) throws SQLException {
        for (OrmInsightMetaData insightMetaData : insightMetaDatas) {
            saveInsightMetaData(insightMetaData);
        }
    }

    public void saveInsightMetaData(OrmInsightMetaData insightMetaData) throws SQLException {
        insightMetadataDao.createOrUpdate(insightMetaData);
    }

    public void saveSyncBit(SyncType type, boolean isSynced) throws SQLException{
        dcSyncDao.createOrUpdate(new OrmDCSync(type.getId(),type.getDescription(),isSynced));
    }
}
