/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class EWSGettingStartedViewModel {

    @NonNull private final Navigator navigator;
    @NonNull private final WiFiUtil wiFiUtil;

    @Inject
    public EWSGettingStartedViewModel(@NonNull final Navigator navigator,
                                      @NonNull final WiFiUtil wiFiUtil) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
    }

    public void onGettingStartedButtonClicked() {
        if (wiFiUtil.isHomeWiFiEnabled()) {
            navigator.navigateToHomeNetworkConfirmationScreen();
        } else {
            navigator.navigateToWifiTroubleShootingScreen();
        }
    }

    public void onBackPressed() {
//        screenFlowController.finish();
        EWSCallbackNotifier.getInstance().onBackPressed();
    }
}