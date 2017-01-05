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

import java.util.Collections;

import javax.inject.Inject;

import retrofit.RetrofitError;

public class UserCharacteristicsMonitor extends EventMonitor {

    private UserCharacteristicsSender mUserCharacteristicsSender;
    private UserCharacteristicsFetcher mUserCharacteristicsFetcher;

    @Inject
    UCoreAccessProvider uCoreAccessProvider;
    @Inject
    Eventing mEventing;

    @Inject
    public UserCharacteristicsMonitor(
            UserCharacteristicsSender sender,
            UserCharacteristicsFetcher fetcher) {

        DataServicesManager.mAppComponent.injectUserCharacteristicsMonitor(this);
        mUserCharacteristicsSender = sender;
        mUserCharacteristicsFetcher = fetcher;
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

    private boolean isUserInvalid() {
        final String accessToken = uCoreAccessProvider.getAccessToken();
        return !uCoreAccessProvider.isLoggedIn() || accessToken == null || accessToken.isEmpty();
    }

    private RetrofitError getNonLoggedInError() {
        return RetrofitError.unexpectedError("", new IllegalStateException("you're not logged in"));
    }

    private void postError(int referenceId, final RetrofitError error) {
        DSLog.i("***SPO***", "Error In ConsentsMonitor - posterror");
        mEventing.post(new BackendResponse(referenceId, error));
    }

}