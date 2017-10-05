package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

public class ResetConnectionTroubleshootingViewModel {

    @NonNull private final Navigator navigator;

    @Inject
    public ResetConnectionTroubleshootingViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
    }

    public void onYesButtonClicked() {
        navigator.navigateToResetDeviceTroubleShootingScreen();
    }

    public void onNoButtonClicked() {
        navigator.navigateToConnectToWrongPhoneTroubleShootingScreen();
    }
}
