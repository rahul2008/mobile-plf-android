package com.philips.platform.ews.base;

import android.support.annotation.NonNull;

import com.philips.platform.ews.navigation.Navigator;

import javax.inject.Inject;

public class BaseTroubleShootingViewModel {

    @NonNull private final Navigator navigator;

    @Inject
    public BaseTroubleShootingViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
    }

    public void onCancelButtonClicked() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }

}
