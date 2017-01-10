package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.events.DataClearRequest;
import com.philips.platform.core.events.DataClearResponse;
import com.philips.platform.core.events.MomentBackendDeleteResponse;
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
            dbInterface.deleteAllMoments();
            eventing.post(new DataClearResponse(event.getEventId()));
    }

    public void onEventAsync(@NonNull MomentDeleteRequest event) {
            dbInterface.markAsInActive(event.getMoment());
         //   eventing.post(new MomentChangeEvent(event.getEventId(), event.getMoment()));

    }

    public void onEventBackgroundThread(@NonNull MomentBackendDeleteResponse backendDeleteResponse) {
        dbInterface.deleteMoment(backendDeleteResponse.getMoment());
    }
}

