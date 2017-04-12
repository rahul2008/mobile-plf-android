/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.consent;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.events.BackendDataRequestFailed;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.ConsentBackendSaveResponse;
import com.philips.platform.core.events.GetNonSynchronizedMomentsRequest;
import com.philips.platform.core.events.GetNonSynchronizedMomentsResponse;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;
import com.philips.platform.datasync.synchronisation.DataFetcher;
import com.philips.platform.datasync.synchronisation.DataSender;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class ConsentsDataFetcher extends DataFetcher {
    public static final String TAG = "ConsentsDataFetcher";
    @NonNull
    protected final AtomicInteger synchronizationState = new AtomicInteger(0);

    private List<ConsentDetail> consentDetails;

    @Inject
    Eventing eventing;

    @NonNull
    private final UCoreAdapter uCoreAdapter;

    @Inject
    UCoreAccessProvider uCoreAccessProvider;


    @NonNull
    private final GsonConverter gsonConverter;

    @NonNull
    private final ConsentsConverter consentsConverter;


    @Inject
    public ConsentsDataFetcher(@NonNull UCoreAdapter uCoreAdapter, @NonNull GsonConverter gsonConverter, @NonNull ConsentsConverter consentsConverter) {
        super(uCoreAdapter);
        this.uCoreAdapter = uCoreAdapter;
        this.gsonConverter = gsonConverter;
        this.consentsConverter = consentsConverter;
        DataServicesManager.getInstance().getAppComponant().injectConsentsDataFetcher(this);


    }

    @Nullable
    @Override
    public RetrofitError fetchDataSince(@Nullable DateTime sinceTimestamp) {
        registerEvent();
        eventing.post(new GetNonSynchronizedMomentsRequest(null));
        /*if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            eventing.post(new ConsentBackendGetRequest(1, consentDetails));
        }*/
        return null;
    }

    public List<ConsentDetail> getConsentDetails() {
        return consentDetails;
    }

    public void setConsentDetails(List<ConsentDetail> consentDetails) {
        this.consentDetails = consentDetails;
    }

    @Override
    public RetrofitError fetchAllData() {

        return super.fetchAllData();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetNonSynchronizedMomentsResponse response) {
        List<? extends ConsentDetail> nonSynchronizedConsent = response.getConsentDetails();
        if (synchronizationState.get() != DataSender.State.BUSY.getCode()) {
            getConsent(new ArrayList<>(nonSynchronizedConsent));
        }
    }

    public void getConsent(List<ConsentDetail> consentDetails) {

        if (isUserInvalid()) {
            postError(1, getNonLoggedInError());
            return;
        }
        if (consentDetails == null || consentDetails.size() == 0 || uCoreAccessProvider == null) {
            return;
        }

        ConsentsClient client = uCoreAdapter.getAppFrameworkClient(ConsentsClient.class, uCoreAccessProvider.getAccessToken(), gsonConverter);
        if(client==null) return;
        try {

            ArrayList<String> consentTypes = new ArrayList<>();
            ArrayList<String> deviceIdentificationList = new ArrayList<>();
            ArrayList<String> documentVersionList = new ArrayList<>();

            for (ConsentDetail consentDetail : consentDetails) {

                if(consentDetail==null)return;

                consentTypes.add(consentDetail.getType());
                deviceIdentificationList.add(consentDetail.getDeviceIdentificationNumber());
                documentVersionList.add(consentDetail.getVersion());
            }

            List<UCoreConsentDetail> consentDetailList = client.getConsent(uCoreAccessProvider.getUserId(), consentTypes,
                    deviceIdentificationList, documentVersionList);
            if (consentDetailList != null && !consentDetailList.isEmpty()) {
                List<ConsentDetail> appConsentDetails = consentsConverter.convertToAppConsentDetails(consentDetailList);

                if(appConsentDetails == null) return;

                eventing.post(new ConsentBackendSaveResponse(appConsentDetails, HttpURLConnection.HTTP_OK, null));
            }
        } catch (RetrofitError ex) {
            onError(ex);
            eventing.post(new BackendDataRequestFailed(ex));

        }
    }

    public boolean isUserInvalid() {
        if (uCoreAccessProvider != null) {
            String accessToken = uCoreAccessProvider.getAccessToken();
            return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
        }
        return false;
    }

    void postError(int referenceId, final RetrofitError error) {
        DSLog.i(DSLog.LOG, "Error In ConsentsMonitor - posterror");
        eventing.post(new BackendResponse(referenceId, error));
    }

    RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    public void registerEvent() {
        if (!eventing.isRegistered(this)) {
            eventing.register(this);
        }
    }



}

