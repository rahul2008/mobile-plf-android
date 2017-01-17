package com.philips.platform.appframework.connectivity;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ConnectivityContract {

    interface View {
        void updateUIOnPostMomentSuccess(String momentId);

        void updateUIOnPostMomentError(VolleyError volleyError);

        void updateUIOnGetMomentSuccess(String momentValue);

        void updateUIOnGetMomentError(VolleyError volleyError);

        void updateDeviceMeasurementValue(String measurementValue);

        void onDeviceMeasurementError(Error error,String s);

        void updateConnectionStateText(String text);
    }

    interface UserActionsListener {
        void postMoment(User user, String momentValue);

        void getMoment(User user, String momentId);

        void setUpApplicance(@NonNull BleReferenceAppliance appliance);
    }
}
