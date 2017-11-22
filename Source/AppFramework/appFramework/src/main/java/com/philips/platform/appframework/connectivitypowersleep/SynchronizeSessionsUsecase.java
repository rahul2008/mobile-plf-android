/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.PortDataCallback;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionInfoPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionsOldestToNewest;
import com.philips.platform.appframework.connectivitypowersleep.error.FailedSyncSessionsException;
import com.philips.platform.baseapp.screens.utility.RALog;

import org.joda.time.DateTime;

import javax.inject.Inject;

public class SynchronizeSessionsUsecase {

    private static final String TAG = "FetchSessionsUsecase";

    interface Callback {
        void onSynchronizeSucceed(@NonNull final SessionsOldestToNewest sleepDataList);

        void onNoNewSessionsAvailable();

        void onError(@NonNull final Exception error);
    }

    AllSessionsProvider.Callback allSessionProviderCallback = new AllSessionsProvider.Callback() {
        @Override
        public void onResult(@NonNull SessionsOldestToNewest result) {
            RALog.i(TAG, "onResult:"+result.size());

            if (callback != null) {
//                secureStorage.storeLong(SecureStorageUtility.LAST_SYNC_DATE, new Date().getTime());
                callback.onSynchronizeSucceed(result);
            }
        }

        @Override
        public void onError(@NonNull Throwable error) {
            RALog.i(TAG, "onError:"+error.getMessage());

            if (callback != null) {
                callback.onError(new FailedSyncSessionsException());
            }
        }

        @Override
        public void onDeviceContainsNoSessions() {
            RALog.d(TAG, "onDeviceContainsNoSessions() -> fetchAllSleepMoments first");

            if (callback != null) {
                callback.onNoNewSessionsAvailable();
            }
        }
    };

    private final PortDataCallback<SessionInfoPortProperties> sessionInfoCallback =
            new PortDataCallback<SessionInfoPortProperties>() {
                @Override
                public void onDataReceived(SessionInfoPortProperties portProperties) {
                    if (callback != null && portProperties == null) {
                        callback.onError(new FailedSyncSessionsException("SessionInfoPortProperties == null"));
                    } else {

                        final Long latestSessionId = portProperties.getNewestSession();
                        final Long oldestSessionId = portProperties.getOldestSession();
                        final Boolean isEmpty = portProperties.isEmpty();

                        RALog.d(TAG, "Session info oldest"+oldestSessionId+"newest"+latestSessionId+"isEmpty"+isEmpty);

                        final AllSessionsProvider allSessionsProvider = allSessionsProviderFactory.createAllSessionProvider(appliance);

                        if (latestSessionId != null && oldestSessionId != null && isEmpty != null) {
                            allSessionsProvider.fetchAllSessionData(latestSessionId, oldestSessionId, isEmpty, latestMomentDateTime, allSessionProviderCallback);
                        } else if (callback != null) {
                            callback.onError(new FailedSyncSessionsException("latest or oldest sessionId == null"));
                        }
                    }

                    if (appliance != null) {
                        appliance.unregisterSessionInfoCallback();
                    }
                }

                @Override
                public void onError(Error error) {
                    RALog.d(TAG, "Session info failed");
                    if (callback != null) {
                        callback.onError(new FailedSyncSessionsException(
                                error != null ? error.getErrorMessage() : "Error retrieving session info!"));
                    }

                    if (appliance != null) {
                        appliance.unregisterSessionInfoCallback();
                    }
                }
            };


    @NonNull final AllSessionsProviderFactory allSessionsProviderFactory;

    @Nullable
    RefAppBleReferenceAppliance appliance;
    @Nullable Callback callback;
    @NonNull private DateTime latestMomentDateTime;

    @Inject
    SynchronizeSessionsUsecase(@NonNull AllSessionsProviderFactory allSessionsProviderFactory) {
        this.allSessionsProviderFactory = allSessionsProviderFactory;
    }

    void execute(@NonNull RefAppBleReferenceAppliance appliance, @NonNull Callback callback, @NonNull DateTime latestMomentDateTime) {
        this.callback = callback;
        this.appliance = appliance;
        this.latestMomentDateTime = latestMomentDateTime;

        appliance.registerSessionInfoCallback(sessionInfoCallback);
        appliance.syncSessionInfo();
    }
}
