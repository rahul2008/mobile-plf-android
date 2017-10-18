package com.philips.cdp2.ews.troubleshooting.resetdevice;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

public class ResetDeviceTroubleshootingViewModel {

    @NonNull private final Navigator navigator;

    @Inject
    public ResetDeviceTroubleshootingViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
    }

    public void onDoneButtonClicked() {
        navigator.navigateToDevicePoweredOnConfirmationScreen();
    }

}
