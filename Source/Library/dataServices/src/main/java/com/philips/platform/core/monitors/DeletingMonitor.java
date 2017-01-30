/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.core.monitors;

import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
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
    public void onEventBackgroundThread(DataClearRequest event) {
        final DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAll(dbRequestListener);
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
        //   eventing.post(new MomentChangeEvent(event.getEventId(), event.getMoment()));

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackgroundThread(MomentBackendDeleteResponse backendDeleteResponse) {
        final DBRequestListener dbRequestListener = backendDeleteResponse.getDbRequestListener();
        try {
            dbInterface.deleteMoment(backendDeleteResponse.getMoment(),
                    backendDeleteResponse.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
        }
    }
}

