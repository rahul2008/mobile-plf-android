/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.SessionDataPort;

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
