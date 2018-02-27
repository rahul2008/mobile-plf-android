/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.dscdemo.database.table.OrmInsight;
import com.philips.platform.dscdemo.database.table.OrmMoment;
import com.philips.platform.dscdemo.database.table.OrmSynchronisationData;
import com.philips.platform.dscdemo.utility.NotifyDBRequestListener;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class OrmDeletingInterfaceImpl implements DBDeletingInterface {

    @NonNull
    private final OrmDeleting ormDeleting;

    @NonNull
    private final OrmSaving ormSaving;

    @NonNull
    private final OrmFetchingInterfaceImpl fetching;

    private NotifyDBRequestListener notifyDBRequestListener;

    @Inject
    public OrmDeletingInterfaceImpl(@NonNull final OrmDeleting ormDeleting,
                                    final OrmSaving ormSaving, OrmFetchingInterfaceImpl fetching) {
        this.ormDeleting = ormDeleting;
        this.ormSaving = ormSaving;
        this.fetching = fetching;
        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    //Delete All
    @Override
    public void deleteAll(DBRequestListener dbRequestListener) throws SQLException {
        ormDeleting.deleteAll();
        notifyDBRequestListener.notifyPrepareForDeletion(dbRequestListener);
    }

    //Moments
    @Override
    public void markAsInActive(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        if (isMomentSyncedToBackend(moment)) {
            prepareMomentForDeletion(moment, dbRequestListener);
        } else {
            moment.setSynchronisationData(
                    new OrmSynchronisationData(Moment.MOMENT_NEVER_SYNCED_AND_DELETED_GUID, true,
                            DateTime.now(), 0));
            saveMoment(moment, dbRequestListener);
        }
        notifyDBRequestListener.notifyPrepareForDeletion(dbRequestListener);
    }


    @Override
    public void markMomentsAsInActive(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        for (Moment moment : moments) {
            markAsInActive(moment, dbRequestListener);
        }
        notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.MOMENT);
    }

    @Override
    public void deleteAllMoments(DBRequestListener<Moment> dbRequestListener) throws SQLException {
        List<? extends Moment> moments = fetching.fetchMoments(null);
        markMomentsAsInActive((List<Moment>) moments, dbRequestListener);
    }

    @Override
    public void deleteSyncedMoments(DBRequestListener<Moment> dbRequestListener) throws SQLException {
        ormDeleting.deleteSyncedMoments();
        notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.MOMENT);
    }

    @Override
    public void deleteMoment(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        ormDeleting.ormDeleteMoment((OrmMoment) moment);
        notifyDBRequestListener.notifySuccess(dbRequestListener, (OrmMoment) moment, SyncType.MOMENT);
    }

    @Override
    public boolean deleteMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        boolean isDeleted = ormDeleting.deleteMoments(moments, dbRequestListener);
        if (isDeleted) {
            notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.MOMENT);
        }
        return isDeleted;
    }

    @Override
    public void deleteAllExpiredMoments(DBRequestListener<Integer> dbRequestListener) throws SQLException {
        int affected = ormDeleting.deleteAllExpiredMoments();
        notifyDBRequestListener.notifySuccess(dbRequestListener, affected);
    }

    @Override
    public void deleteMomentDetail(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        ormDeleting.deleteMomentDetails(moment.getId());
    }

    @Override
    public void deleteMeasurementGroup(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        ormDeleting.deleteMeasurementGroups((OrmMoment) moment);
    }

    private boolean isMomentSyncedToBackend(final Moment moment) {
        return moment.getSynchronisationData() != null;
    }

    private void saveMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        ormSaving.saveMoment(getOrmMoment(moment, dbRequestListener));
    }

    private OrmMoment getOrmMoment(final Moment moment, DBRequestListener<Moment> dbRequestListener) {
        try {
            return OrmTypeChecking.checkOrmType(moment, OrmMoment.class);
        } catch (OrmTypeChecking.OrmTypeException e) {
            notifyDBRequestListener.notifyOrmTypeCheckingFailure(dbRequestListener, e, "type check failed!");
            if (e.getMessage() != null) {
            }
        }
        return null;
    }

    private void prepareMomentForDeletion(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
        moment.setSynced(false);
        moment.getSynchronisationData().setInactive(true);
        saveMoment(moment, dbRequestListener);
    }

    //User characteristics
    @Override
    public void deleteUserCharacteristics() throws SQLException {
        ormDeleting.deleteCharacteristics();
    }

    //Insights
    @Override
    public boolean markInsightsAsInActive(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        List<Insight> insightsToDelete = new ArrayList<>();
        for (Insight insight : insights) {
            if (insight.getSynchronisationData() == null)
                insight.setSynchronisationData(new OrmSynchronisationData(Insight.INSIGHT_NEVER_SYNCED_AND_DELETED_GUID, true,
                        DateTime.now(), 0));
            insight.setSynced(false);
            insight.setInactive(true);
            insight.getSynchronisationData().setInactive(true);
            insightsToDelete.add(insight);
        }
        ormSaving.saveInsights(insightsToDelete, dbRequestListener);
        return true;
    }

    @Override
    public boolean deleteInsights(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        boolean isDeleted = ormDeleting.deleteInsights(insights, dbRequestListener);
        if (isDeleted) {
            notifyDBRequestListener.notifyDBChange(SyncType.INSIGHT);
        }
        return isDeleted;
    }

    @Override
    public void deleteInsight(Insight insight, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        ormDeleting.deleteInsight((OrmInsight) insight);
        notifyDBRequestListener.notifyDBChange(SyncType.INSIGHT);
    }

    @Override
    public void deleteAllInsights(DBRequestListener<Insight> dbRequestListener) throws SQLException {
        ormDeleting.deleteAllInsights();
        notifyDBRequestListener.notifySuccess(dbRequestListener, SyncType.INSIGHT);
    }

    //Sync
    @Override
    public int deleteSyncBit(SyncType syncType) throws SQLException {
        return ormDeleting.deleteSyncBit(syncType);
    }

    //Post Error
    @Override
    public void deleteFailed(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }

}
