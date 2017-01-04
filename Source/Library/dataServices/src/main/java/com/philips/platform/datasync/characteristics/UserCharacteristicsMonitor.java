/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.datasync.characteristics;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.events.CharacteristicsBackendGetRequest;
import com.philips.platform.core.events.CharacteristicsBackendSaveRequest;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import java.util.Collections;

import javax.inject.Inject;

import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

public class UserCharacteristicsMonitor extends EventMonitor {
    private final UserCharacteristicsConverter mUserCharacteristicsConverter;
    private final DataServicesManager mDataServicesManager;
    private final UCoreAccessProvider mUCoreAccessProvider;
    private final UCoreAdapter mUCoreAdapter;
    private final GsonConverter mGsonConverter;
    private final Eventing mEventing;

    private UserCharacteristicsSender mUserCharacteristicsSender;
    private UserCharacteristicsFetcher mUserCharacteristicsFetcher;

    @Inject
    public UserCharacteristicsMonitor(UCoreAdapter uCoreAdapter,
                                      GsonConverter gsonConverter,
                                      Eventing eventing,
                                      UserCharacteristicsConverter userCharacteristicsConvertor,
                                      UserCharacteristicsSender sender,
                                      UserCharacteristicsFetcher fetcher) {
        this.mUCoreAdapter = uCoreAdapter;
        this.mGsonConverter = gsonConverter;
        this.mEventing = eventing;

        mUserCharacteristicsSender = sender;
        mUserCharacteristicsFetcher = fetcher;
        mUserCharacteristicsConverter = userCharacteristicsConvertor;
        mDataServicesManager = DataServicesManager.getInstance();
        this.mUCoreAccessProvider = mDataServicesManager.getUCoreAccessProvider();
    }

    //Save Request
    public void onEventAsync(CharacteristicsBackendSaveRequest characteristicsBackendSaveRequest) {
        sendToBackend(characteristicsBackendSaveRequest);
    }

    //Fetch Request
    public void onEventAsync(CharacteristicsBackendGetRequest characteristicsBackendGetRequest) {
        mUserCharacteristicsFetcher.fetchDataSince(null);
    }

    public void sendToBackend(CharacteristicsBackendSaveRequest characteristicsBackendSaveRequest) {
        if (isUserInvalid()) {
            postError(characteristicsBackendSaveRequest.getEventId(), getNonLoggedInError());
            return;
        }
        mUserCharacteristicsSender.sendDataToBackend(Collections.singletonList(characteristicsBackendSaveRequest.getCharacteristic()));

    }

    public boolean isUserInvalid() {
        final String accessToken = mDataServicesManager.getUCoreAccessProvider().getAccessToken();
        return !mDataServicesManager.getUCoreAccessProvider().isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    private void postError(int referenceId, final RetrofitError error) {
        DSLog.i("***SPO***", "Error In ConsentsMonitor - posterror");
        mEventing.post(new BackendResponse(referenceId, error));
    }

}