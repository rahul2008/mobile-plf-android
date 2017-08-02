package cdp.philips.com.demoapp.database;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import cdp.philips.com.demoapp.NotifyDBRequestListener;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class OrmDeletingInterfaceImpl implements DBDeletingInterface {

    @NonNull
    private final OrmDeleting ormDeleting;

    @NonNull
    private final OrmSaving ormSaving;

    @NonNull
    private final OrmFetchingInterfaceImpl fetching;

    NotifyDBRequestListener notifyDBRequestListener;

    @Inject
    public OrmDeletingInterfaceImpl(@NonNull final OrmDeleting ormDeleting,
                                    final OrmSaving ormSaving, OrmFetchingInterfaceImpl fetching) {
        this.ormDeleting = ormDeleting;
        this.ormSaving = ormSaving;
        this.fetching = fetching;
        notifyDBRequestListener = new NotifyDBRequestListener();
    }

    @Override
    public void deleteAll(DBRequestListener dbRequestListener) throws SQLException {
        ormDeleting.deleteAll();
        notifyDBRequestListener.notifyPrepareForDeletion(dbRequestListener);
    }

    @Override
    public void markAsInActive(final Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {

    }


    @Override
    public void markMomentsAsInActive(final List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {

    }

    @Override
    public void deleteMoment(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {

    }

    @Override
    public boolean deleteMoments(List<Moment> moments, DBRequestListener<Moment> dbRequestListener) throws SQLException {

        return false;
    }


    @Override
    public void deleteMomentDetail(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
    }

    @Override
    public void deleteMeasurementGroup(Moment moment, DBRequestListener<Moment> dbRequestListener) throws SQLException {
    }

    @Override
    public void deleteFailed(Exception e, DBRequestListener dbRequestListener) {
        notifyDBRequestListener.notifyFailure(e, dbRequestListener);
    }

    @Override
    public void deleteAllMoments(DBRequestListener<Moment> dbRequestListener) throws SQLException {
        List<? extends Moment> moments = fetching.fetchMoments(null);
        markMomentsAsInActive((List<Moment>) moments, dbRequestListener);
    }

    //Insights
    @Override
    public boolean markInsightsAsInActive(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {

        return false;
    }

    @Override
    public boolean deleteInsights(List<Insight> insights, DBRequestListener<Insight> dbRequestListener) throws SQLException {
        return false;
    }

    @Override
    public void deleteInsight(Insight insight, DBRequestListener<Insight> dbRequestListener) throws SQLException {

    }

    @Override
    public int deleteSyncBit(SyncType syncType) throws SQLException {
        return ormDeleting.deleteSyncBit(syncType);
    }

    @Override
    public void deleteUserCharacteristics() throws SQLException {
    }
}
