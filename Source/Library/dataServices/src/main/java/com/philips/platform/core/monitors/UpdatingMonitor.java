package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.BackendMomentRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.events.WriteDataToBackendRequest;
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
        int requestId = momentUpdateRequest.getEventId();
        Moment moment = momentUpdateRequest.getMoment();
        moment.setSynced(false);
        Moment ormMoment = dbUpdatingInterface.getOrmMoment(moment);
        if (ormMoment == null) {
            return;
        }
        //  dbDeletingInterface.ormDeletingDeleteMoment(ormMoment);
        dbUpdatingInterface.updateOrSaveMomentInDatabase(ormMoment);
        //     eventing.post(new MomentChangeEvent(requestId, moment));
    }


    public void onEventAsync(final DatabaseConsentUpdateRequest consentUpdateRequest) {
        int requestId = consentUpdateRequest.getEventId();
        Consent consent = consentUpdateRequest.getConsent();

    }

    public void onEventBackgroundThread(final BackendResponse error) {
        dbUpdatingInterface.postRetrofitError(error.getCallException());
    }

    public void onEventBackgroundThread(final BackendMomentRequestFailed momentSaveRequestFailed) {
        dbUpdatingInterface.updateFailed(momentSaveRequestFailed.getException());
    }

    public void onEventBackgroundThread(ReadDataFromBackendResponse response) {
        DSLog.i("**SPO**", "In Updating Monitor ReadDataFromBackendResponse");
        try {
            DSLog.i("**SPO**", "In Updating Monitor before calling fetchMoments");
            dbFetchingInterface.fetchMoments();
        } catch (SQLException e) {
            DSLog.i("**SPO**", "In Updating Monitor report exception");
            dbUpdatingInterface.updateFailed(e);
            e.printStackTrace();
        }
       // eventing.post(new WriteDataToBackendRequest());
    }

    public void onEventBackgroundThread(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            DSLog.i("***SPO***","In updatingMonitor moments null hence start push");
            eventing.post(new WriteDataToBackendRequest());
            return;
        }
        int updatedCount = dbUpdatingInterface.processMomentsReceivedFromBackend(moments);
        DSLog.i("***SPO***","In updatingMonitor start push after processing moments");
        eventing.post(new WriteDataToBackendRequest());
    }

    public void onEventBackgroundThread(final MomentDataSenderCreatedRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }

        dbUpdatingInterface.processCreatedMoment(moments);
    }

    public void onEventAsync(final ConsentBackendSaveResponse consentBackendSaveResponse) throws SQLException {

        dbUpdatingInterface.updateConsent(consentBackendSaveResponse.getConsent());

    }
}
