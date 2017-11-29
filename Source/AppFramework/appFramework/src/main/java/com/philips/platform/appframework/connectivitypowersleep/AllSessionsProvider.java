/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.baseapp.screens.utility.RALog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

class AllSessionsProvider implements SessionProvider.Callback {

    private static final String TAG = "AllSessionsProvider";

    @NonNull private final RefAppBleReferenceAppliance appliance;
    @NonNull private final SessionProviderFactory sessionProviderFactory;
    @NonNull private RetryHelper retryHelper;
    @NonNull private List<Session> newSessionsData;
    @NonNull private DateTime latestMomentDateTime;
    @Nullable private Callback sessionsDataCallback;
    private long currentSessionNumber;
    private long oldestSessionNumber;

    interface Callback {
        void onResult(@NonNull final SessionsOldestToNewest result);

        void onError(@NonNull Throwable error);

        void onDeviceContainsNoSessions();
    }

    AllSessionsProvider(@NonNull RefAppBleReferenceAppliance appliance, @NonNull SessionProviderFactory sessionProviderFactory,RetryHelper retryHelper) {
        this.appliance = appliance;
        this.sessionProviderFactory = sessionProviderFactory;
        this.retryHelper = retryHelper;
        this.newSessionsData = new ArrayList<>();
    }

    void fetchAllSessionData(long newest, long oldest, boolean isEmpty, @NonNull DateTime lastestMomentDateTime, @NonNull AllSessionsProvider.Callback callback) {
        RALog.d(TAG, "fetchAllSessionData " + newest + " " + oldest);
        sessionsDataCallback = callback;
        currentSessionNumber = newest;
        oldestSessionNumber = oldest;
        this.latestMomentDateTime = lastestMomentDateTime;
        newSessionsData.clear();

        if (isEmpty) {
            callback.onDeviceContainsNoSessions();
        } else {
            fetchSession(newest, false);
        }
    }

    private void fetchSession(long session, boolean isOlderThanLastDbSession) {
        if ((session < oldestSessionNumber) || isOlderThanLastDbSession) {
            RALog.d(TAG, "onFetchingSucceed size: " + newSessionsData.size());

            if (sessionsDataCallback != null) {
                sessionsDataCallback.onResult(new SessionsOldestToNewest(newSessionsData));
            }
        } else {
            RALog.d(TAG, "fetchSession " + session);

            SessionProvider nextSessionProvider = sessionProviderFactory.createBleSessionProvider(appliance, session, this);
            nextSessionProvider.fetchSession();
        }
    }

    private boolean isOlderThanLastDbSession(Summary lastRetrievedSession) {
        return lastRetrievedSession != null && lastRetrievedSession.getDate().getTime() <= latestMomentDateTime.getMillis();
    }

    @Override
    public void onSuccessForEmptyOrInvalidTimeSession() {
        RALog.d(TAG, "onSuccessForEmptyOrInvalidTimeSession");
        fetchNextSession(false);
    }

    @Override
    public void onSuccess(@NonNull Session sleepData) {
        Summary summary = sleepData.getSummary();
        boolean isOlderThanLastDbSession = isOlderThanLastDbSession(summary);
        if (!isOlderThanLastDbSession) {
            newSessionsData.add(sleepData);
        }

        RALog.d(TAG, "onFetchingSucceed");
        fetchNextSession(isOlderThanLastDbSession);
    }

    private void fetchNextSession(boolean isOlderThanLastDbSession) {
        currentSessionNumber--;
        retryHelper.reset();
        fetchSession(currentSessionNumber, isOlderThanLastDbSession);
    }

    @Override
    public void onError(@NonNull Throwable error) {
        RALog.d(TAG, "onError ");

        if (retryHelper.canRetry()) {
            RALog.d(TAG, "retrying.....");
            fetchSession(currentSessionNumber, false);
        } else {
            retryHelper.reset();
            if (sessionsDataCallback != null) {
                sessionsDataCallback.onError(error);
            }
        }
    }
}
