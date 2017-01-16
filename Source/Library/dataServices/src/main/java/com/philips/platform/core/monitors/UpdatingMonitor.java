package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;
import android.util.Log;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.events.UserCharacteristicsSaveRequest;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.moments.MomentsSegregator;

import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

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

    DBRequestListener mDbRequestListener;

    @Inject
    MomentsSegregator momentsSegregator;


    public UpdatingMonitor(DBUpdatingInterface dbUpdatingInterface, DBDeletingInterface dbDeletingInterface, DBFetchingInterface dbFetchingInterface) {
        this.dbUpdatingInterface = dbUpdatingInterface;
        this.dbDeletingInterface = dbDeletingInterface;
        this.dbFetchingInterface = dbFetchingInterface;
        DataServicesManager.getInstance().mAppComponent.injectUpdatingMonitor(this);
        this.mDbRequestListener = DataServicesManager.getInstance().getDbChangeListener();
    }

    public void onEventAsync(final MomentUpdateRequest momentUpdateRequest) {
        Moment moment = momentUpdateRequest.getMoment();
        moment.setSynced(false);
        DBRequestListener dbRequestListener = momentUpdateRequest.getDbRequestListener();
        try {
            dbUpdatingInterface.updateMoment(moment, dbRequestListener);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e,dbRequestListener);
            e.printStackTrace();
        }
        //     eventing.post(new MomentChangeEvent(requestId, moment));
    }


    public void onEventAsync(final DatabaseConsentUpdateRequest consentUpdateRequest) {
        consentUpdateRequest.getConsent();
    }

    public void onEventBackgroundThread(ReadDataFromBackendResponse response) {
        DSLog.i("**SPO**", "In Updating Monitor ReadDataFromBackendResponse");
        try {
           // DSLog.i("**SPO**", "In Updating Monitor before calling fetchMoments");
            dbFetchingInterface.fetchMoments(response.getDbRequestListener());
        } catch (SQLException e) {
            DSLog.i("**SPO**", "In Updating Monitor report exception");
            dbUpdatingInterface.updateFailed(e, response.getDbRequestListener());
            e.printStackTrace();
        }
        // eventing.post(new WriteDataToBackendRequest());
    }

    public void onEventBackgroundThread(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        int count = momentsSegregator.processMomentsReceivedFromBackend(moments);
        if(count == moments.size()){
            if(DataServicesManager.getInstance().getDbChangeListener()!=null){
                DataServicesManager.getInstance().getDbChangeListener().onSuccess(moments);
            }
        }
    }

    public void onEventBackgroundThread(final MomentDataSenderCreatedRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        momentsSegregator.processCreatedMoment(moments,momentSaveRequest.getDbRequestListener());
        if(DataServicesManager.getInstance().getDbChangeListener()!=null){
            DataServicesManager.getInstance().getDbChangeListener().onSuccess(moments);
        }
    }

    public void onEventAsync(final ConsentBackendSaveResponse consentBackendSaveResponse) throws SQLException {
        try {
            dbUpdatingInterface.updateConsent(consentBackendSaveResponse.getConsent(), mDbRequestListener);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, mDbRequestListener);
        }
    }

    public void onEventAsync(final UCDBUpdateFromBackendRequest userCharacteristicsSaveBackendRequest) throws SQLException {
        try {
            DSLog.i(DSLog.LOG, "Inder Updating Monitor onEventAsync UCDBUpdateFromBackendRequest");
            dbUpdatingInterface.processCharacteristicsReceivedFromDataCore(userCharacteristicsSaveBackendRequest.getCharacteristics(), DataServicesManager.getInstance().getDbChangeListener());
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, DataServicesManager.getInstance().getDbChangeListener());
        }
    }
}
