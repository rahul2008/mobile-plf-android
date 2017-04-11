package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.ConsentDetail;
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
import com.philips.platform.core.events.SettingsBackendSaveRequest;
import com.philips.platform.core.events.SettingsBackendSaveResponse;
import com.philips.platform.core.events.SyncBitUpdateRequest;
import com.philips.platform.core.events.UCDBUpdateFromBackendRequest;
import com.philips.platform.core.events.FetchInsightsResponse;
import com.philips.platform.core.events.UpdateUcoreMetadataRequest;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.blob.BlobMetaData;
import com.philips.platform.datasync.characteristics.UserCharacteristicsSegregator;
import com.philips.platform.datasync.insights.InsightSegregator;
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
@SuppressWarnings({"rawtypes", "unchecked"})
public class UpdatingMonitor extends EventMonitor {
    @NonNull
    DBUpdatingInterface dbUpdatingInterface;

    @NonNull
    DBDeletingInterface dbDeletingInterface;

    @NonNull
    DBFetchingInterface dbFetchingInterface;


    @Inject
    InsightSegregator insightSegregator;

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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentUpdateRequest momentUpdateRequest) {
        Moment moment = momentUpdateRequest.getMoment();
        moment.setSynced(false);
        DBRequestListener<Moment> dbRequestListener = momentUpdateRequest.getDbRequestListener();
        try {
            dbUpdatingInterface.updateMoment(moment, dbRequestListener);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e,dbRequestListener);
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentsUpdateRequest momentsUpdateRequest) {
        List<Moment> moments = momentsUpdateRequest.getMoments();
        DBRequestListener<Moment> dbRequestListener = momentsUpdateRequest.getDbRequestListener();
        try {
            dbUpdatingInterface.updateMoments(moments, dbRequestListener);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e,dbRequestListener);
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final DatabaseConsentUpdateRequest consentUpdateRequest) {
        consentUpdateRequest.getConsentDetails();
        try {

            final DBRequestListener<ConsentDetail> dbRequestListener = consentUpdateRequest.getDbRequestListener();
            if(dbUpdatingInterface.updateConsent(consentUpdateRequest.getConsentDetails(), dbRequestListener))
            {
                dbUpdatingInterface.updateSyncBit(SyncType.CONSENT.getId(),false);
                eventing.post(new ConsentBackendSaveRequest((new ArrayList<>(consentUpdateRequest.getConsentDetails())), ConsentBackendSaveRequest.RequestType.SAVE));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final BackendMomentListSaveRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        try {
            momentsSegregator.processMomentsReceivedFromBackend(moments, null);
            DSLog.i(DSLog.LOG,"After Process Moment");
            notifyDBChangeSuccess(SyncType.MOMENT);
        }catch (SQLException e){
            notifyDBFailure(e);
        }
    }

    private void notifyDBChangeSuccess(SyncType moment) {
        DSLog.i(DSLog.LOG,"inside notifyDBChange UpdatingMonitor");
        DBChangeListener mDbChangeListener = DataServicesManager.getInstance().getDbChangeListener();
        if(mDbChangeListener !=null) {
            DSLog.i(DSLog.LOG,"inside notifyDBChange UpdatingMonitor - Listener registered and UI notified");
            mDbChangeListener.dBChangeSuccess(moment);
        }else{
            DSLog.i(DSLog.LOG,"inside notifyDBChange UpdatingMonitor - Listener not registered");
        }
    }

    private void notifyDBFailure(SQLException e) {
        DBChangeListener mDbChangeListener = DataServicesManager.getInstance().getDbChangeListener();
        if(mDbChangeListener !=null){
            mDbChangeListener.dBChangeFailed(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final MomentDataSenderCreatedRequest momentSaveRequest) {
        List<? extends Moment> moments = momentSaveRequest.getList();
        if (moments == null || moments.isEmpty()) {
            return;
        }
        momentsSegregator.processCreatedMoment(moments,null);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final ConsentBackendSaveResponse consentBackendSaveResponse) throws SQLException {
        try {
            if(dbFetchingInterface.isSynced(SyncType.CONSENT.getId())) {
                dbUpdatingInterface.updateConsent(consentBackendSaveResponse.getConsentDetailList(), null);
                notifyDBChangeSuccess(SyncType.CONSENT);
            }
        }catch (SQLException e){
            notifyDBFailure(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final UCDBUpdateFromBackendRequest userCharacteristicsSaveBackendRequest) throws SQLException {
        try {
            if (mUserCharacteristicsSegregator.isUCSynced()) {
                dbUpdatingInterface.updateCharacteristics(userCharacteristicsSaveBackendRequest.getUserCharacteristics(), null);
                notifyDBChangeSuccess(SyncType.CHARACTERISTICS);
            }
        } catch (SQLException e) {
            notifyDBFailure(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final DatabaseSettingsUpdateRequest databaseSettingsUpdateRequest) throws SQLException{
        try{
            dbUpdatingInterface.updateSettings(databaseSettingsUpdateRequest.getSettings(), databaseSettingsUpdateRequest.getDbRequestListener());
            dbUpdatingInterface.updateSyncBit(SyncType.SETTINGS.getId(),false);
            eventing.post(new SettingsBackendSaveRequest(databaseSettingsUpdateRequest.getSettings()));
        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e, databaseSettingsUpdateRequest.getDbRequestListener());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final SyncBitUpdateRequest syncBitUpdateRequest) throws SQLException{
        try{
            dbUpdatingInterface.updateSyncBit(syncBitUpdateRequest.getTableType().getId(),syncBitUpdateRequest.isSynced());
        }catch (SQLException e){
            dbUpdatingInterface.updateFailed(e,null);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final SettingsBackendSaveResponse settingsBackendSaveResponse) throws SQLException{
        DSLog.i(DSLog.LOG,"Settings updatingMonitor in SettingsBackendSaveResponse");
        try{
            if(dbFetchingInterface.isSynced(SyncType.SETTINGS.getId())){
                DSLog.i(DSLog.LOG,"Settings updatingMonitor in SettingsBackendSaveResponse inside if block");
                dbUpdatingInterface.updateSettings(settingsBackendSaveResponse.getSettings(),null);
                DSLog.i(DSLog.LOG,"Settings Fetch complete in updatingMonitor");
                notifyDBChangeSuccess(SyncType.SETTINGS);
            }

        } catch (SQLException e) {
            notifyDBFailure(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final FetchInsightsResponse updateInsightsBackendResponse) throws SQLException {
        try {
            insightSegregator.processInsights(updateInsightsBackendResponse.getInsights(), updateInsightsBackendResponse.getDbRequestListener());
            notifyDBChangeSuccess(SyncType.INSIGHT);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, null);
            notifyDBFailure(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventBackGround(final UpdateUcoreMetadataRequest updateUcoreMetadataRequest) throws SQLException {
        try {
            BlobMetaData blobMetaData = dbFetchingInterface.fetchBlobMetaDataByBlobID(updateUcoreMetadataRequest.getBlobMetaData().getBlobID());
            BlobMetaData uCoreBlobMetaData=updateUcoreMetadataRequest.getBlobMetaData();
            if(blobMetaData!=null){
                uCoreBlobMetaData.setId(blobMetaData.getId());
            }
            dbUpdatingInterface.updateUcoreBlobMetaData(uCoreBlobMetaData);
        } catch (SQLException e) {
            dbUpdatingInterface.updateFailed(e, null);
            notifyDBFailure(e);
        }
    }

}
