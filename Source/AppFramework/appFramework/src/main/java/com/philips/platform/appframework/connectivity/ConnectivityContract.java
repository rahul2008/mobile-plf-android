package com.philips.platform.appframework.connectivity;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ConnectivityContract {

    interface View {

        void onProcessMomentError(String errorText);

        void onProcessMomentSuccess(String momentValue);

        void onProcessMomentProgress(String message);

        void updateDeviceMeasurementValue(String measurementValue);

        void onDeviceMeasurementError(Error error,String s);

        void updateConnectionStateText(String text);

    }

    interface UserActionsListener {
        void processMoment(String momentValue);

        void setUpApplicance(@NonNull RefAppBleReferenceAppliance appliance);
    }
}
