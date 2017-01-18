package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentDeleteRequest;
import com.philips.platform.core.listeners.DBRequestListener;

import java.sql.SQLException;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class DeletingMonitor extends EventMonitor {

    private DBDeletingInterface dbInterface;

    @Inject
    public DeletingMonitor(DBDeletingInterface dbInterface) {
        this.dbInterface = dbInterface;

    }

    public void onEventBackgroundThread(@NonNull DataClearRequest event) {
        DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.deleteAll(dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
            e.printStackTrace();
        }
        //eventing.post(new DataClearResponse(event.getEventId()));
    }

    public void onEventAsync(@NonNull MomentDeleteRequest event) {
        DBRequestListener dbRequestListener = event.getDbRequestListener();
        try {
            dbInterface.markAsInActive(event.getMoment(), dbRequestListener);
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
            e.printStackTrace();
        }
        //   eventing.post(new MomentChangeEvent(event.getEventId(), event.getMoment()));

    }

    public void onEventBackgroundThread(@NonNull MomentBackendDeleteResponse backendDeleteResponse) {
        DBRequestListener dbRequestListener = backendDeleteResponse.getDbRequestListener();
        try {
            dbInterface.deleteMoment(backendDeleteResponse.getMoment(), backendDeleteResponse.getDbRequestListener());
        } catch (SQLException e) {
            dbInterface.deleteFailed(e, dbRequestListener);
            e.printStackTrace();
        }
    }
}

