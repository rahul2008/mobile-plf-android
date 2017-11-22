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
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Session;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPortProperties;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.Summary;
import com.philips.platform.appframework.connectivitypowersleep.error.InvalidPortPropertiesException;
import com.philips.platform.appframework.connectivitypowersleep.error.PortErrorException;
import com.philips.platform.baseapp.screens.utility.RALog;

class SessionProvider {

    private static final String TAG = "SleepSessionProvider";

    public interface Callback {
        void onSuccessForEmptyOrInvalidTimeSession();

        void onSuccess(@NonNull final Session data);

        void onError(@NonNull final Throwable error);
    }

    @NonNull final RefAppBleReferenceAppliance diCommAppliance;
    @NonNull final Callback sleepDataCallback;

    long sessionNumber;

    @Nullable
    Summary summary;

    private final PortDataCallback<SessionDataPortProperties> sessionDataCallback =
            new PortDataCallback<SessionDataPortProperties>() {
                @Override
                public void onDataReceived(@Nullable SessionDataPortProperties portProperties) {
                    RALog.d(TAG, "data for session"+sessionNumber);
                    if (portProperties != null && (portProperties.isEmptySession()|| !portProperties.isSessionTimeValid())) {
                        notifySuccessEmptySessionOrSessionWtihInvalidTime();
                    } else {
                        try {
                            processSessionData(portProperties);
                        } catch (InvalidPortPropertiesException e) {
                            notifyError(new IllegalStateException(e));
                        }
                    }
                }

                @Override
                public void onError(Error error) {
                    RALog.d(TAG, "data for session"+sessionNumber+"with error"+error);
                    notifyError(new PortErrorException(error));
                }
            };

    SessionProvider(@NonNull RefAppBleReferenceAppliance appliance, long sessionNumber,
                    @NonNull Callback sleepDataCallback) {
        this.diCommAppliance = appliance;
        this.sessionNumber = sessionNumber;
        this.sleepDataCallback = sleepDataCallback;
    }

    void fetchSession() {
        this.diCommAppliance.registerSessionDataCallback(sessionDataCallback);
        this.diCommAppliance.syncSessionData(sessionNumber);
    }

    void processSessionData(@Nullable SessionDataPortProperties sessionProperties)
            throws InvalidPortPropertiesException {
        RALog.i(TAG,"process data for session "+sessionNumber);

        summary = getSummary(sessionProperties);
        notifySuccess(getResult());
    }

    @NonNull
    protected Session getResult() {
        return new Session(summary);
    }

    @NonNull
    protected Summary getSummary(@Nullable SessionDataPortProperties sessionProperties) {
        return new Summary(sessionProperties.getDate(),sessionProperties.getDeepSleepTime(),sessionProperties.getTotalSleepTime());
    }

    void notifySuccess(@NonNull Session result) {
        this.diCommAppliance.unregisterSessionDataCallback();
        this.sleepDataCallback.onSuccess(result);
    }

    void notifyError(@NonNull Exception ex) {
        this.diCommAppliance.unregisterSessionDataCallback();
        this.sleepDataCallback.onError(ex);
    }

    void notifySuccessEmptySessionOrSessionWtihInvalidTime() {
        this.diCommAppliance.unregisterSessionDataCallback();
        this.sleepDataCallback.onSuccessForEmptyOrInvalidTimeSession();
    }
}
