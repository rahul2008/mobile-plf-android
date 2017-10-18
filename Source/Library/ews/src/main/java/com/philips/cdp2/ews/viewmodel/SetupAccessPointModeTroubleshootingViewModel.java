package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

public class SetupAccessPointModeTroubleshootingViewModel {

    @NonNull
    private final Navigator navigator;

    @Inject
    public SetupAccessPointModeTroubleshootingViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
    }

    public void onDoneButtonClicked() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }

}
