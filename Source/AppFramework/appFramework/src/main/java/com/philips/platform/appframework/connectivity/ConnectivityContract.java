package com.philips.platform.appframework.connectivity;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

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

        void serviceDiscoveryError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s);

        void onDataCoreBasrUrlLoad(String baseUrl);
    }

    interface UserActionsListener {
        void postMoment(User user, String baseUrl,String momentValue);

        void getMoment(User user, String baseUrl,String momentId);

        void setUpApplicance(@NonNull BleReferenceAppliance appliance);
    }
}
