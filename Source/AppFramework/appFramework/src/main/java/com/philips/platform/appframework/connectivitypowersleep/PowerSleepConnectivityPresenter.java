package com.philips.platform.appframework.connectivitypowersleep;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.models.Measurement;
import com.philips.platform.appframework.connectivity.models.MomentDetail;
import com.philips.platform.appframework.connectivity.models.UserMoment;
import com.philips.platform.appframework.connectivity.network.GetMomentRequest;
import com.philips.platform.appframework.connectivity.network.PostMomentRquest;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.utils.DataServicesConstants;

import java.net.URL;
import java.util.ArrayList;

public class PowerSleepConnectivityPresenter implements ConnectivityPowerSleepContract.UserActionsListener {
    public static final String TAG = PowerSleepConnectivityPresenter.class.getSimpleName();
    private ConnectivityPowerSleepContract.View connectivityViewListener;

    public PowerSleepConnectivityPresenter(final ConnectivityPowerSleepContract.View connectivityViewListener) {
        this.connectivityViewListener = connectivityViewListener;
    }

    @Override
    public void removeSessionPortListener(BleReferenceAppliance appliance) {
        if (appliance != null) {
            appliance.getSessionDataPort().removePortListener(diCommPortListener);
        }
    }

    @Override
    public void setUpApplicance(@NonNull BleReferenceAppliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create bleReferenceAppliance for provided NetworkNode.");
        }



        appliance.getSessionDataPort().addPortListener(diCommPortListener);

        appliance.getSensorDataPort().addPortListener(new DICommPortListener<GenericPort<SensorDataPortProperties>>() {

            @Override
            public void onPortUpdate(final GenericPort<SensorDataPortProperties> port) {
            }

            @Override
            public void onPortError(GenericPort<SensorDataPortProperties> diCommPort, final Error error, String s) {

            }
        });
    }

    DICommPortListener diCommPortListener = new DICommPortListener<SessionDataPort>() {
        @Override
        public void onPortUpdate(SessionDataPort diCommPort) {
            connectivityViewListener.updateSessionData(diCommPort.getPortProperties().getTotalSleepTime(), diCommPort.getPortProperties().getNumberOfInterruptions(), diCommPort.getPortProperties().getDeepSleepTime());

        }

        @Override
        public void onPortError(SessionDataPort diCommPort, Error error, @Nullable String s) {
            connectivityViewListener.showError(error, s);
        }
    };

}
