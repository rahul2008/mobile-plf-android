/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;

import javax.inject.Inject;

public class EWSResetDeviceViewModel {

    private ScreenFlowController screenFlowController;

    @Inject
    public EWSResetDeviceViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void onContinueButtonClicked() {
        screenFlowController.showFragment(new EWSDevicePowerOnFragment());
    }
}