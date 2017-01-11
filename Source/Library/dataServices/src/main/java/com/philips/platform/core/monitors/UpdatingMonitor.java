package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;

import java.sql.SQLException;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UpdatingMonitor extends EventMonitor {
    @NonNull
    DBUpdatingInterface dbUpdatingInterface;

    @NonNull
    DBDeletingInterface dbDeletingInterface;

    @NonNull
    DBFetchingInterface dbFetchingInterface;

    public UpdatingMonitor(DBUpdatingInterface dbUpdatingInterface, DBDeletingInterface dbDeletingInterface, DBFetchingInterface dbFetchingInterface) {
        this.dbUpdatingInterface = dbUpdatingInterface;
        this.dbDeletingInterface = dbDeletingInterface;
        this.dbFetchingInterface = dbFetchingInterface;
    }

    public void onEventAsync(final MomentUpdateRequest momentUpdateRequest) {
        Moment moment = momentUpdateRequest.getMoment();
        moment.setSynced(false);

        dbUpdatingInterface.updateMoment(moment,momentUpdateRequest.getDbRequestListener());
        //     eventing.post(new MomentChangeEvent(requestId, moment));
    }


    public void onEventAsync(final DatabaseConsentUpdateRequest consentUpdateRequest) {
        consentUpdateRequest.getConsent();
    }

    public void onEventBackgroundThread(ReadDataFromBackendResponse response) {
        DSLog.i("**SPO**", "In Updating Monitor ReadDataFromBackendResponse");
        try {
          //  DSLog.i("**SPO**", "In Updating Monitor before calling fetchMoments");
            dbFetchingInterface.fetchMoments(DataServicesManager.getInstance().getDbRequestListener());
        } catch (SQLException e) {
            //DSLog.i("**SPO**", "In Updating Monitor report exception");
            dbUpdatingInterface.updateFailed(e,DataServicesManager.getInstance().getDbRequestListener());
            e.printStackTrace();
        }
        // eventing.post(new WriteDataToBackendRequest());
    }

    public void onEventBackgroundThread(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        dbUpdatingInterface.processMomentsReceivedFromBackend(moments, DataServicesManager.getInstance().getDbRequestListener());
    }

    public void onEventBackgroundThread(final MomentDataSenderCreatedRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        dbUpdatingInterface.processCreatedMoment(moments,DataServicesManager.getInstance().getDbRequestListener());
    }

    public void onEventAsync(final ConsentBackendSaveResponse consentBackendSaveResponse) throws SQLException {
        dbUpdatingInterface.updateConsent(consentBackendSaveResponse.getConsent(),DataServicesManager.getInstance().getDbRequestListener());
    }
}
