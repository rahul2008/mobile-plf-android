package com.philips.cdp2.ews.troubleshooting.connecttowrongphone;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

public class ConnectToWrongPhoneTroubleshootingViewModel {

    @NonNull private final Navigator navigator;

    @Inject
    public ConnectToWrongPhoneTroubleshootingViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
    }

    public void onYesButtonClicked() {
        navigator.navigateSetupAccessPointModeScreen();
    }

    public void onNoButtonClicked() {
        navigator.navigateToResetConnectionTroubleShootingScreen();
    }

}
