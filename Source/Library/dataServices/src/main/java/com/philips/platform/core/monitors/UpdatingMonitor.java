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
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.core.utils.NotifyDBChangeListener;
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

    NotifyDBChangeListener notifyDBChangeListener;

    @Inject
    MomentsSegregator momentsSegregator;

    @Inject
    UserCharacteristicsSegregator mUserCharacteristicsSegregator;


    public UpdatingMonitor(DBUpdatingInterface dbUpdatingInterface, DBDeletingInterface dbDeletingInterface, DBFetchingInterface dbFetchingInterface) {
        this.dbUpdatingInterface = dbUpdatingInterface;
        this.dbDeletingInterface = dbDeletingInterface;
        this.dbFetchingInterface = dbFetchingInterface;
        DataServicesManager.getInstance().mAppComponent.injectUpdatingMonitor(this);
        notifyDBChangeListener=new NotifyDBChangeListener();
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
        int count = momentsSegregator.processMomentsReceivedFromBackend(moments,null);
       /* if(count == moments.size()){
           // new NotifyDBChangeListener().notifyDBChangeSuccess(momentSaveRequest.getDbChangeListener());
        }*/
    }

    public void onEventBackgroundThread(final MomentDataSenderCreatedRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        momentsSegregator.processCreatedMoment(moments,null);
       /* if(DataServicesManager.getInstance().getDbRequestListener()!=null){
            DataServicesManager.getInstance().getDbRequestListener().onSuccess(moments);
        }*/
    }

    public void onEventAsync(final ConsentBackendSaveResponse consentBackendSaveResponse) throws SQLException {
        try {
            dbUpdatingInterface.updateConsent(consentBackendSaveResponse.getConsent(), null);
        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e, null);
        }
    }

    public void onEventAsync(final UCDBUpdateFromBackendRequest userCharacteristicsSaveBackendRequest) throws SQLException {
        try {
            DSLog.i(DSLog.LOG, "Inder Updating Monitor onEventAsync updateMonitor UCDBUpdateFromBackendRequest");
            boolean isSynced = mUserCharacteristicsSegregator.processCharacteristicsReceivedFromDataCore(userCharacteristicsSaveBackendRequest.getUserCharacteristics(), null);
            if (isSynced) {
                dbUpdatingInterface.updateCharacteristics(userCharacteristicsSaveBackendRequest.getUserCharacteristics(), null);
            }
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, userCharacteristicsSaveBackendRequest.getDbRequestListener());
        }
    }

}
