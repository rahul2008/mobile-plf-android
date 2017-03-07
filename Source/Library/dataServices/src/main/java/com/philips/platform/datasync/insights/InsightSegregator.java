/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.insights;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SynchronisationData;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBSavingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class InsightSegregator {

    @Inject
    DBUpdatingInterface updatingInterface;
    @Inject
    DBFetchingInterface dbFetchingInterface;
    @Inject
    DBDeletingInterface dbDeletingInterface;
    @Inject
    DBSavingInterface dbSavingInterface;


    public InsightSegregator() {
        DataServicesManager.getInstance().getAppComponant().injectInsightSegregator(this);
    }


    private int getVersionInDatabase(final Insight insightInDataBase) {
        if (insightInDataBase != null && insightInDataBase.getSynchronisationData() != null) {
            return insightInDataBase.getSynchronisationData().getVersion();
        }
        return -1;
    }

    private boolean hasDifferentInsightVersion(final Insight insight,
                                               final Insight insightInDatabase) throws SQLException {
        boolean isVersionDifferent = true;
        final SynchronisationData synchronisationData = insight.getSynchronisationData();

        if (synchronisationData != null) {
            final int versionInDatabase = getVersionInDatabase(insightInDatabase);
            if (versionInDatabase != -1) {
                isVersionDifferent = versionInDatabase != synchronisationData.getVersion();
            }
        }
        return isVersionDifferent;
    }

    private boolean isInsightActive(final SynchronisationData synchronisationData) {
        return synchronisationData == null || !synchronisationData.isInactive();
    }

    protected boolean InsightDeletedLocallyDuringSync(final Insight insightInDatabase) {
        if (insightInDatabase != null) {
            final SynchronisationData synchronisationData = insightInDatabase.getSynchronisationData();
            if (synchronisationData != null) {
                return synchronisationData.getGuid().
                        equals(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID);
            }
        }
        return false;
    }


    public void processInsights(final List<? extends Insight> insights, DBRequestListener dbRequestListener) throws SQLException {

        List<Insight> insightsToDelete = new ArrayList<>();
        List<Insight> insightsToDeleteAndSave = new ArrayList<>();

        for (Insight insight : insights) {

            final Insight insightInDatabase = getOrmInsightFromDatabase(insight, dbRequestListener);

            if (hasDifferentInsightVersion(insight, insightInDatabase)) {
                if (!isInsightActive(insight.getSynchronisationData())) {
                    insightsToDelete.add(insightInDatabase);
                    //deleteMomentInDatabaseIfExists(momentInDatabase, dbRequestListener);
                } else if (InsightDeletedLocallyDuringSync(insightInDatabase)) {
                    insight.setSynced(false);
                    insight.getSynchronisationData().setInactive(true);

                    if (insightInDatabase != null) {
                        insight.setId(insightInDatabase.getId());
                    }
                    insightsToDeleteAndSave.add(insight);
                    // deleteAndSaveMoment(momentInDatabase, moment, dbRequestListener);
                } else {
                    if (!isInsightModifiedLocallyDuringSync(insightInDatabase, insight)) {
                        insight.setSynced(true);
                    }
                    if (insightInDatabase != null) {
                        insight.setId(insightInDatabase.getId());
                    }
                    //This is required for deleting duplicate
                    // measurements, measurementDetails and momentDetails
                    //deleteAndSaveMoment(momentInDatabase, moment, dbRequestListener);
                    insightsToDeleteAndSave.add(insight);
                }
            }
        }
        deleteInsightsInDatabaseIfExists(insightsToDelete, dbRequestListener);
        deleteAndSaveInsights(insightsToDeleteAndSave, dbRequestListener);

    }

    private void deleteAndSaveInsights(List<Insight> insights, DBRequestListener dbRequestListener) {

        List<Insight> insightToDeleteList = new ArrayList<>();
        try {
            for (Insight insight : insights) {
                final Insight insightInDatabase;
                insightInDatabase = getOrmInsightFromDatabase(insight, dbRequestListener);
                insightToDeleteList.add(insightInDatabase);
            }
            deleteInsightsInDatabaseIfExists(insightToDeleteList, dbRequestListener);
            dbSavingInterface.saveInsights(insights, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void deleteInsightsInDatabaseIfExists(final List<Insight> insightsToDelete, DBRequestListener dbRequestListener)
            throws SQLException {
        dbDeletingInterface.deleteInsights(insightsToDelete, dbRequestListener);
    }


    private Insight getOrmInsightFromDatabase(Insight insight, DBRequestListener dbRequestListener) throws SQLException {
        Insight insightInDatabase = null;
        final SynchronisationData synchronisationData = insight.getSynchronisationData();

        if (synchronisationData != null) {
            insightInDatabase = (Insight) dbFetchingInterface.fetchInsightByGuid(synchronisationData.getGuid());
            if (insightInDatabase == null) {
                //TODO: Spoorti - Check what has to be passed listener
                insightInDatabase = (Insight) dbFetchingInterface.fetchInsightById(insight.getId(), null);
            }
        }
        return insightInDatabase;
    }

    private boolean isInsightModifiedLocallyDuringSync(final Insight insightInDatabase,
                                                       final Insight insight) {
        return insightInDatabase != null &&
                !insight.getTimeStamp().equals(insightInDatabase.getTimeStamp());
    }


}
