/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.core.monitors;

import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DeleteAllInsights;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteExpiredInsightRequest;
import com.philips.platform.core.events.DeleteExpiredMomentRequest;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.DeleteInsightRequest;
import com.philips.platform.core.events.DeleteInsightResponse;
import com.philips.platform.core.events.DeleteSyncedMomentsRequest;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

import javax.inject.Inject;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DeletingMonitor extends EventMonitor {

    private final DBDeletingInterface dbInterface;

    @Inject
    public DeletingMonitor(DBDeletingInterface dbInterface) {
        this.dbInterface = dbInterface;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DataClearRequest event) {
        final DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAll(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(MomentBackendDeleteResponse backendDeleteResponse) {
        final DBRequestListener<Moment> dbRequestListener = backendDeleteResponse.getDbRequestListener();
        try {
            dbInterface.deleteMoment(backendDeleteResponse.getMoment(),
                    backendDeleteResponse.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteAllMomentsRequest event) {
        final DBRequestListener<Moment> dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAllMoments(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(MomentDeleteRequest event) {
        final DBRequestListener<Moment> dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.markAsInActive(event.getMoment(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(MomentsDeleteRequest event) {
        final DBRequestListener<Moment> dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.markMomentsAsInActive(event.getMoments(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteSyncedMomentsRequest event) {
        final DBRequestListener<Moment> dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteSyncedMoments(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteExpiredMomentRequest event) {
        DBRequestListener<Integer> dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAllExpiredMoments(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteExpiredInsightRequest event) {
        DBRequestListener<Insight> dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAllExpiredInsights(dbRequestListener);
        }
        catch(SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteInsightFromDB deleteInsightFromDB) {
        final DBRequestListener<Insight> dbRequestListener = deleteInsightFromDB.getDbRequestListener();
        try {
            dbInterface.markInsightsAsInActive(deleteInsightFromDB.getInsights(), dbRequestListener);
            eventing.post(new DeleteInsightRequest(deleteInsightFromDB.getInsights())); //is it good to have here?
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteInsightResponse deleteInsightResponse) {
        final DBRequestListener<Insight> dbRequestListener = deleteInsightResponse.getDBRequestListener();
        try {
            dbInterface.deleteInsight(deleteInsightResponse.getInsight(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(DeleteAllInsights deleteAllInsights) {
        DBRequestListener<Insight> dbRequestListener = deleteAllInsights.getDbRequestListener();
        try {
            dbInterface.deleteAllInsights(dbRequestListener);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

