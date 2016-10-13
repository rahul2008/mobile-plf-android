package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.ListSaveResponse;
import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.events.MomentUpdateRequest;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UpdatingMonitor extends EventMonitor{
    @NonNull
    DBUpdatingInterface dbUpdatingInterface;

    @NonNull
    DBDeletingInterface dbDeletingInterface;


    public UpdatingMonitor(DBUpdatingInterface dbUpdatingInterface, DBDeletingInterface dbDeletingInterface){
        this.dbUpdatingInterface = dbUpdatingInterface;
        this.dbDeletingInterface = dbDeletingInterface;
    }

    public void onEventAsync(final MomentUpdateRequest momentUpdateRequest) {
        int requestId = momentUpdateRequest.getEventId();
        Moment moment = momentUpdateRequest.getMoment();
        moment.setSynced(false);
        Moment ormMoment = dbUpdatingInterface.getOrmMoment(moment);
        if (ormMoment == null) {
            return;
        }
            dbDeletingInterface.ormDeletingDeleteMoment(ormMoment);
            dbUpdatingInterface.updateOrSaveMomentInDatabase(ormMoment);
            eventing.post(new MomentChangeEvent(requestId, moment));

    }

    public void onEventBackgroundThread(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        int requestId = momentSaveRequest.getEventId();

        int updatedCount = dbUpdatingInterface.processMomentsReceivedFromBackend(moments);
        boolean savedAllMoments = updatedCount == moments.size();

        eventing.post(new ListSaveResponse(requestId, savedAllMoments));
    }
}
