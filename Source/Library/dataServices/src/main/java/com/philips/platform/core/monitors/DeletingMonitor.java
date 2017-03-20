/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DeleteAllMomentsRequest;
import com.philips.platform.core.events.DeleteInsightResponse;
import com.philips.platform.core.events.DeleteInsightFromDB;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.events.MomentsDeleteRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;

import javax.inject.Inject;

public class DeletingMonitor extends EventMonitor {

    private final DBDeletingInterface dbInterface;

    @Inject
    public DeletingMonitor(DBDeletingInterface dbInterface) {
        this.dbInterface = dbInterface;

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(DataClearRequest event) {
        final DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAll(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
        //eventing.post(new DataClearResponse(event.getEventId()));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(DeleteAllMomentsRequest event) {
        final DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAllMoments(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
        //eventing.post(new DataClearResponse(event.getEventId()));
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(MomentDeleteRequest event) {
        final DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.markAsInActive(event.getMoment(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
        //   eventing.post(new MomentChangeEvent(event.getEventId(), event.getMoments()));

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(MomentsDeleteRequest event) {
        final DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.markMomentsAsInActive(event.getMoments(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
        //   eventing.post(new MomentChangeEvent(event.getEventId(), event.getMoments()));

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(MomentBackendDeleteResponse backendDeleteResponse) {
        final DBRequestListener dbRequestListener = backendDeleteResponse.getDbRequestListener();
        try {
            dbInterface.deleteMoment(backendDeleteResponse.getMoment(),
                    backendDeleteResponse.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(DeleteInsightFromDB insightDeleteDBRequest) {
        final DBRequestListener dbRequestListener = insightDeleteDBRequest.getDbRequestListener();
        try {
            dbInterface.markInsightsAsInActive(insightDeleteDBRequest.getInsights(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(DeleteInsightResponse insightBackendDeleteResponse) {
        final DBRequestListener dbRequestListener = insightBackendDeleteResponse.getDBRequestListener();
        try {
            dbInterface.deleteInsight(insightBackendDeleteResponse.getInsight(),
                    dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }
}

