/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class FirstSetupStepsViewModel {

    @NonNull private final Navigator navigator;

    @Inject
    public FirstSetupStepsViewModel(@NonNull final Navigator navigator) {
        this.navigator = navigator;
    }

    public void onYesButtonClicked() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }
}
