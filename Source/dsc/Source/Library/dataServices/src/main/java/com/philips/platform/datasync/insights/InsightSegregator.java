/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.datasync.insights;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class InsightSegregator {

    @Inject
    DBUpdatingInterface mDBUpdatingInterface;
    @Inject
    DBFetchingInterface mDBFetchingInterface;
    @Inject
    DBDeletingInterface mDBDeletingInterface;
    @Inject
    DBSavingInterface mDBSavingInterface;
    @Inject
    BaseAppDataCreator mBaseAppDataCreator;

    public InsightSegregator() {
        DataServicesManager.getInstance().getAppComponent().injectInsightSegregator(this);
    }

    private Insight getOrmInsightFromDatabase(Insight insight) throws SQLException {
        Insight insightInDatabase = null;
        final SynchronisationData synchronisationData = insight.getSynchronisationData();

        if (synchronisationData != null) {
            insightInDatabase = mDBFetchingInterface.fetchInsightByGuid(synchronisationData.getGuid());
            if (insightInDatabase == null) {
                insightInDatabase = mDBFetchingInterface.fetchInsightById(insight.getId(), null);
            }
        }
        return insightInDatabase;
    }

    private boolean hasDifferentInsightVersion(final Insight insight, final Insight insightInDatabase) throws SQLException {
        return insight.getSynchronisationData() != null && insightInDatabase.getSynchronisationData() != null
                && insight.getSynchronisationData().getVersion() != insightInDatabase.getSynchronisationData().getVersion();
    }

    private boolean isInsightDeletedFromBackend(final SynchronisationData synchronisationData) {
        return synchronisationData == null || synchronisationData.isInactive();
    }

    private boolean isInsightDeletedFromApplicationDB(final Insight insightInDatabase) {
        final SynchronisationData synchronisationData = insightInDatabase.getSynchronisationData();
        return synchronisationData != null && synchronisationData.getGuid().equals(Insight.INSIGHT_NEVER_SYNCED_AND_DELETED_GUID);
    }

    private boolean isInsightUpdatedFromBackend(final Insight insight, final Insight insightInDatabase) {
        return insightInDatabase != null && !insight.getTimeStamp().equals(insightInDatabase.getTimeStamp());
    }

    public void processInsights(final List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        List<Insight> insightsToCreate = new ArrayList<>();
        List<Insight> insightsToUpdate = new ArrayList<>();
        List<Insight> insightsToDelete = new ArrayList<>();

        for (Insight insight : insights) {
            final Insight insightInDatabase = getOrmInsightFromDatabase(insight);
            if (insightInDatabase == null) {
                if (!insight.isInactive()) {
                    SynchronisationData synchronisationData =
                            mBaseAppDataCreator.createSynchronisationData(insight.getGUId(), insight.isInactive(),
                                    new DateTime(insight.getTimeStamp()), insight.getVersion());
                    insight.setSynchronisationData(synchronisationData);
                    insight.setSynced(true);
                    insightsToCreate.add(insight);
                }
            } else if (hasDifferentInsightVersion(insight, insightInDatabase)) {
                if (isInsightDeletedFromBackend(insight.getSynchronisationData())) {
                    insightsToDelete.add(insightInDatabase);
                } else if (isInsightDeletedFromApplicationDB(insightInDatabase)) {
                    insight.setSynced(false);
                    insight.getSynchronisationData().setInactive(true);
                    insight.setId(insightInDatabase.getId());
                    insightsToUpdate.add(insight);
                } else if (!isInsightUpdatedFromBackend(insight, insightInDatabase)) {
                    insight.setSynced(true);
                    insight.setId(insightInDatabase.getId());
                    insightsToUpdate.add(insight);
                }
            }
        }
        if (insightsToCreate.size() > 0)
            mDBSavingInterface.saveInsights(insightsToCreate, dbRequestListener);
        if (insightsToDelete.size() > 0)
            mDBDeletingInterface.deleteInsights(insightsToDelete, dbRequestListener);
        if (insightsToUpdate.size() > 0)
            deleteAndSaveInsights(insightsToUpdate, dbRequestListener);
    }

    private void deleteAndSaveInsights(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        List<Insight> insightsToDelete = new ArrayList<>();
        for (Insight insight : insights) {
            final Insight insightInDatabase;
            insightInDatabase = getOrmInsightFromDatabase(insight);
            insightsToDelete.add(insightInDatabase);
        }
        mDBDeletingInterface.deleteInsights(insightsToDelete, dbRequestListener);
        mDBSavingInterface.saveInsights(insights, dbRequestListener);
    }

    public Map<Class, List<?>> putInsightForSync(Map<Class, List<?>> dataToSync) {
        List<? extends Insight> insights = null;
        try {
            insights = (List<? extends Insight>) mDBFetchingInterface.fetchNonSynchronizedInsights();
        } catch (SQLException e) {
            //Debug Log
        }
        dataToSync.put(Insight.class, insights);
        return dataToSync;
    }
}
