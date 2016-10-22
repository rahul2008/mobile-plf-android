package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.MomentType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.BackendMomentRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.MomentChangeEvent;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.events.WriteDataToBackendRequest;

import java.sql.SQLException;
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

    @NonNull
    DBFetchingInterface dbFetchingInterface;


    public UpdatingMonitor(DBUpdatingInterface dbUpdatingInterface, DBDeletingInterface dbDeletingInterface, DBFetchingInterface dbFetchingInterface){
        this.dbUpdatingInterface = dbUpdatingInterface;
        this.dbDeletingInterface = dbDeletingInterface;
        this.dbFetchingInterface = dbFetchingInterface;
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

    public void onEventBackgroundThread(final BackendResponse error) {
        dbUpdatingInterface.postRetrofitError(error.getCallException());
    }

    public void onEventBackgroundThread(final BackendMomentRequestFailed momentSaveRequestFailed) {
        dbUpdatingInterface.updateFailed(momentSaveRequestFailed.getException());
    }

    public void onEventBackgroundThread(ReadDataFromBackendResponse response) {
        Log.i("**SPO**","In Updating Monitor ReadDataFromBackendResponse");
        /*try {
            Log.i("**SPO**","In Updating Monitor before calling fetchMoments");
            dbFetchingInterface.fetchMoments(MomentType.TEMPERATURE);
        } catch (SQLException e) {
            Log.i("**SPO**","In Updating Monitor report exception");
            dbUpdatingInterface.updateFailed(e);
            e.printStackTrace();
        }*/
        eventing.post(new WriteDataToBackendRequest());
    }

    public void onEventBackgroundThread(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        //int requestId = momentSaveRequest.getEventId();

        int updatedCount = dbUpdatingInterface.processMomentsReceivedFromBackend(moments);
       // boolean savedAllMoments = updatedCount == moments.size();
        /*if(savedAllMoments) {
            try {
                dbFetchingInterface.fetchMoments(MomentType.TEMPERATURE);
            } catch (SQLException e) {
                dbUpdatingInterface.updateFailed(e);
                e.printStackTrace();
            }
        }*/
        //eventing.post(new ListSaveResponse(requestId, savedAllMoments));
    }
}
