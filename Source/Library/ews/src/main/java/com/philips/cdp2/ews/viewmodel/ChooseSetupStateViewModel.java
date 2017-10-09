/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.BlinkingAccessPointFragment;
import com.philips.cdp2.ews.view.EWSResetDeviceFragment;

import javax.inject.Inject;

public class ChooseSetupStateViewModel {

    private ScreenFlowController screenFlowController;

    @Inject
    ChooseSetupStateViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void onFreshSetupIsChosen() {
        screenFlowController.showFragment(new EWSResetDeviceFragment());
    }

    public void onExistingSetupIsChosen() {
        screenFlowController.showFragment(new BlinkingAccessPointFragment());
    }
}
