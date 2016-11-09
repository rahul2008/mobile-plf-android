package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;


import com.philips.platform.core.datatypes.Consent;
import com.philips.platform.core.datatypes.ConsentDetailType;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendGetRequest;
import com.philips.platform.core.events.ConsentBackendListSaveRequest;
import com.philips.platform.core.events.ConsentBackendListSaveResponse;
import com.philips.platform.core.events.ConsentBackendSaveRequest;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import java.net.HttpURLConnection;
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
    private final UCoreAccessProvider accessProvider;

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final ConsentsConverter consentsConverter;

    DataServicesManager mDataServicesManager;

    @Inject
    public ConsentsMonitor(@NonNull final UCoreAdapter uCoreAdapter,
            @NonNull final ConsentsConverter consentsConverter,
            @NonNull final GsonConverter gsonConverter) {

        mDataServicesManager=DataServicesManager.getInstance();
        this.accessProvider = mDataServicesManager.getUCoreAccessProvider();;
        this.uCoreAdapter = uCoreAdapter;
        this.consentsConverter = consentsConverter;
        this.gsonConverter = gsonConverter;
    }

    public void onEventAsync(ConsentBackendSaveRequest event) {
        if (event.getRequestType() == ConsentBackendSaveRequest.RequestType.SAVE) {
            saveConsent(event);
        }// There is no updateConsent for this UserStory, will be implemented when we take this story

        //For tesing
        //eventing.post(new ConsentBackendGetRequest(1));
    }

    public void onEventAsync(ConsentBackendListSaveRequest event) {
        for (Consent consent : event.getConsentList()) {
            saveConsent(new ConsentBackendSaveRequest(ConsentBackendSaveRequest.RequestType.SAVE, consent));
        }
        eventing.post(new ConsentBackendListSaveResponse());
    }

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
        ConsentsClient client = uCoreAdapter.getAppFrameworkClient(ConsentsClient.class, accessProvider.getAccessToken(), gsonConverter);

        try {
            List<UCoreConsentDetail> consentDetailList = client.getConsent(accessProvider.getUserId(), ConsentDetailType.getDescriptionAsList(),
                    consentsConverter.getDeviceIdentificationNumberList(), consentsConverter.getDocumentVersionList());
            if (consentDetailList != null && !consentDetailList.isEmpty()) {
                Consent consent = consentsConverter.convertToAppConsentDetails(consentDetailList, accessProvider.getUserId());
                consent.setBackEndSynchronized(true);

                eventing.post(new ConsentBackendSaveResponse(event.getEventId(), consent, HttpURLConnection.HTTP_OK));
            } else {
                eventing.post(new ConsentBackendSaveResponse(event.getEventId(), null, HttpURLConnection.HTTP_OK));
            }
        } catch (Exception e) {
            eventing.post(new ConsentBackendSaveResponse(event.getEventId(), null, HttpURLConnection.HTTP_GATEWAY_TIMEOUT));
        }
    }

    private void postError(int referenceId, final RetrofitError error) {
        eventing.post(new BackendResponse(referenceId, error));
    }

    private void saveConsent(ConsentBackendSaveRequest event) {
        if (isUserInvalid()) {
            postError(event.getEventId(), getNonLoggedInError());
            return;
        }
        ConsentsClient client = uCoreAdapter.getAppFrameworkClient(ConsentsClient.class, accessProvider.getAccessToken(), gsonConverter);

        Consent consent = event.getConsent();

        if (consent == null) {
            return;
        }

        try {
            List<UCoreConsentDetail> consentDetailList = consentsConverter.convertToUCoreConsentDetails(consent.getConsentDetails());

            if (consentDetailList.isEmpty()) {
                return;
            }

            client.saveConsent(consent.getCreatorId(), consentDetailList);

            consent.setBackEndSynchronized(true);

            eventing.post(new ConsentBackendSaveResponse(event.getEventId(), consent, HttpURLConnection.HTTP_OK));
        } catch (RetrofitError error) {
            postError(event.getEventId(), error);
        }
    }

    public boolean isUserInvalid() {
        final String accessToken = accessProvider.getAccessToken();
        return !accessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }
}