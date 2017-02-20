package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.BackendMomentListSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentUpdateRequest;
import com.philips.platform.core.events.DatabaseSettingsUpdateRequest;
import com.philips.platform.core.events.MomentDataSenderCreatedRequest;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.core.events.MomentsUpdateRequest;
import com.philips.platform.core.events.ReadDataFromBackendResponse;
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.datasync.moments.MomentsSegregator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.sql.SQLException;
import java.util.ArrayList;
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


    @Inject
    MomentsSegregator momentsSegregator;

    @Inject
    UserCharacteristicsSegregator mUserCharacteristicsSegregator;


    public UpdatingMonitor(DBUpdatingInterface dbUpdatingInterface, DBDeletingInterface dbDeletingInterface, DBFetchingInterface dbFetchingInterface) {
        this.dbUpdatingInterface = dbUpdatingInterface;
        this.dbDeletingInterface = dbDeletingInterface;
        this.dbFetchingInterface = dbFetchingInterface;
        DataServicesManager.getInstance().getAppComponant().injectUpdatingMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
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
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final MomentsUpdateRequest momentsUpdateRequest) {
        List<Moment> moments = momentsUpdateRequest.getMoments();
        DBRequestListener dbRequestListener = momentsUpdateRequest.getDbRequestListener();
        try {
            dbUpdatingInterface.updateMoments(moments, dbRequestListener);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e,dbRequestListener);
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final DatabaseConsentUpdateRequest consentUpdateRequest) {
        consentUpdateRequest.getConsentDetails();
        try {

            if(dbUpdatingInterface.updateConsent(consentUpdateRequest.getConsentDetails(),consentUpdateRequest.getDbRequestListener()))
            {
                dbUpdatingInterface.updateSyncBit(SyncType.CONSENT.getId(),false);
                eventing.post(new ConsentBackendSaveRequest((new ArrayList<>(consentUpdateRequest.getConsentDetails())), ConsentBackendSaveRequest.RequestType.SAVE));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ReadDataFromBackendResponse response) {
        try {
            dbFetchingInterface.fetchMoments(response.getDbRequestListener());
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, response.getDbRequestListener());
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        momentsSegregator.processMomentsReceivedFromBackend(moments, null);
        sendDBChanged(momentSaveRequest);
    }

    private void sendDBChanged(BackendMomentListSaveRequest momentSaveRequest) {
        if (momentSaveRequest.getDbChangeListener() != null) {
            momentSaveRequest.getDbChangeListener().dBChangeSuccess();
        } else if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess();
        } else {
            //No Callback registered
            DSLog.i(DataServicesManager.TAG, "No callback registered");
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(final MomentDataSenderCreatedRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        momentsSegregator.processCreatedMoment(moments,null);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final ConsentBackendSaveResponse consentBackendSaveResponse) throws SQLException {
        try {
            if(dbFetchingInterface.isSynced(SyncType.CONSENT.getId())) {
                dbUpdatingInterface.updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
            }
        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e, null);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final UCDBUpdateFromBackendRequest userCharacteristicsSaveBackendRequest) throws SQLException {
        try {
            if (mUserCharacteristicsSegregator.isUCSynced()) {
                dbUpdatingInterface.updateCharacteristics(userCharacteristicsSaveBackendRequest.getUserCharacteristics(), null);
            }
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, userCharacteristicsSaveBackendRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final DatabaseSettingsUpdateRequest databaseSettingsUpdateRequest) throws SQLException{
        try{
            dbUpdatingInterface.updateSettings(databaseSettingsUpdateRequest.getSettings(), databaseSettingsUpdateRequest.getDbRequestListener());
            dbUpdatingInterface.updateSyncBit(SyncType.SETTINGS.getId(),false);
            eventing.post(new SettingsBackendSaveRequest(databaseSettingsUpdateRequest.getSettings()));
        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e, databaseSettingsUpdateRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final SyncBitUpdateRequest syncBitUpdateRequest) throws SQLException{
        try{
            dbUpdatingInterface.updateSyncBit(syncBitUpdateRequest.getTableType().getId(),syncBitUpdateRequest.isSynced());
        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e,null);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final SettingsBackendSaveResponse settingsBackendSaveResponse) throws SQLException{
        try{
            if(dbFetchingInterface.isSynced(SyncType.SETTINGS.getId())){
                dbUpdatingInterface.updateSettings(settingsBackendSaveResponse.getSettings(),null);
            }

        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e,null);
        }
    }
}
