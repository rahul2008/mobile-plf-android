package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;

import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.DatabaseConsentSaveRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConsentsMonitor extends EventMonitor {

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @Inject
    UCoreAccessProvider uCoreAccessProvider;


    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final ConsentsConverter consentsConverter;



    @Inject
    public ConsentsMonitor(@NonNull final UCoreAdapter uCoreAdapter,
                           @NonNull final ConsentsConverter consentsConverter,
                           @NonNull final GsonConverter gsonConverter) {
        DataServicesManager.getInstance().getAppComponant().injectConsentsMonitor(this);
        this.uCoreAdapter = uCoreAdapter;
        this.consentsConverter = consentsConverter;
        this.gsonConverter = gsonConverter;
    }

    //TODO: Commented part can you clearify with Ajay
    //TODO: NO need to check to SAVE
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ConsentBackendSaveRequest event) {
        if (event.getRequestType() == ConsentBackendSaveRequest.RequestType.SAVE) {
            sendToBackend(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ConsentBackendListSaveRequest event) {
        for (Consent consent : event.getConsentList()) {
            sendToBackend(new ConsentBackendSaveRequest(ConsentBackendSaveRequest.RequestType.SAVE, consent));
        }
        eventing.post(new ConsentBackendListSaveResponse());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(ConsentBackendGetRequest event) {
        getConsent(event);
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    private void getConsent(ConsentBackendGetRequest event) {

        if (isUserInvalid()) {
            postError(event.getEventId(), getNonLoggedInError());
            return;
        }
        if(event.getConsentDetails()==null || uCoreAccessProvider==null){
            return;
        }

        ConsentsClient client = uCoreAdapter.getAppFrameworkClient(ConsentsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
        try {

            List<ConsentDetail> consentDetails=event.getConsentDetails();
            ArrayList<String> consentDetailTypes = new ArrayList<>();
            ArrayList<String> deviceIdentificationList = new ArrayList<>();
            ArrayList<String> documentVersionList = new ArrayList<>();
            for(ConsentDetail consentDetail:consentDetails){
                consentDetailTypes.add(consentDetail.getType());
                deviceIdentificationList.add(consentDetail.getDeviceIdentificationNumber());
                documentVersionList.add(consentDetail.getVersion());

            }

            List<UCoreConsentDetail> consentDetailList = client.getConsent(uCoreAccessProvider.getUserId(), consentDetailTypes,
                    deviceIdentificationList,documentVersionList);
            if (consentDetailList != null && !consentDetailList.isEmpty()) {
                Consent consent = consentsConverter.convertToAppConsentDetails(consentDetailList, uCoreAccessProvider.getUserId());
                for (ConsentDetail consentDetail : consent.getConsentDetails()) {
                    consentDetail.setBackEndSynchronized(true);
                }
                DSLog.i("***SPO***", "Get Consent called After ConsentsClient before sending consents response");
                eventing.post(new ConsentBackendSaveResponse(event.getEventId(), consent, HttpURLConnection.HTTP_OK, null));
            } else {
                eventing.post(new ConsentBackendSaveResponse(event.getEventId(), null, HttpURLConnection.HTTP_OK, null));
            }
        }  catch (RetrofitError ex) {
        eventing.post(new BackendDataRequestFailed(ex));
        }
    }

    private void postError(int referenceId, final RetrofitError error) {
        DSLog.i("***SPO***", "Error In ConsentsMonitor - posterror");
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void sendToBackend(ConsentBackendSaveRequest event) {
        if (isUserInvalid()) {
            postError(event.getEventId(), getNonLoggedInError());
            return;
        }
        if(uCoreAccessProvider==null){
            return;
        }
        ConsentsClient client = uCoreAdapter.getAppFrameworkClient(ConsentsClient.class,uCoreAccessProvider.getAccessToken(), gsonConverter);

        Consent consent = event.getConsent();

        if (consent == null) {
            return;
        }
        //Check if all Consents are synchronized ,then do not send to uCore
        boolean isAllConsentDetailsSynced = true;
        for (ConsentDetail consentDetail : consent.getConsentDetails()) {
            if (!consentDetail.getBackEndSynchronized()) {
                isAllConsentDetailsSynced = false;
                break;
            }
        }
        if (isAllConsentDetailsSynced) {
            return;
        }

        try {
            List<UCoreConsentDetail> consentDetailList = consentsConverter.convertToUCoreConsentDetails(consent.getConsentDetails());

            if (consentDetailList.isEmpty()) {
                return;
            }

            client.saveConsent(consent.getCreatorId(), consentDetailList);

            for (ConsentDetail consentDetail : consent.getConsentDetails()) {
                consentDetail.setBackEndSynchronized(true);
            }
            eventing.post(new DatabaseConsentSaveRequest(consent, true, null));
        } catch (RetrofitError error) {
            postError(event.getEventId(), error);
        }
    }

    public boolean isUserInvalid() {
        if(uCoreAccessProvider!=null) {
            String accessToken = uCoreAccessProvider.getAccessToken();
            return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
        }
        return false;
    }
}