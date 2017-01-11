package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DataClearResponse;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.events.MomentDeleteRequest;

import javax.inject.Inject;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

public class DeletingMonitor extends EventMonitor{

    private DBDeletingInterface dbInterface;

    @Inject
    public DeletingMonitor(DBDeletingInterface dbInterface){
        this.dbInterface = dbInterface;

    }

    public void onEventBackgroundThread(@NonNull DataClearRequest event) {
            dbInterface.deleteAllMoments(event.getDbRequestListener());
            //eventing.post(new DataClearResponse(event.getEventId()));
    }

    public void onEventAsync(@NonNull MomentDeleteRequest event) {
            dbInterface.deleteMoment(event.getMoment(),event.getDbRequestListener());
         //   eventing.post(new MomentChangeEvent(event.getEventId(), event.getMoment()));

    }

    public void onEventBackgroundThread(@NonNull MomentBackendDeleteResponse backendDeleteResponse) {
        dbInterface.ormDeletingDeleteMoment(backendDeleteResponse.getMoment(),backendDeleteResponse.getDbRequestListener());
    }
}

