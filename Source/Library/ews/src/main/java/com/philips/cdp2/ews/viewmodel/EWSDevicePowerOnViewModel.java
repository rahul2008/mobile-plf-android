/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.ChooseSetupStateFragment;
import com.philips.cdp2.ews.view.EWSPressPlayAndFollowSetupFragment;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class EWSDevicePowerOnViewModel {

    private ScreenFlowController screenFlowController;

    @Inject
    public EWSDevicePowerOnViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void onYesButtonClicked() {
        screenFlowController.showFragment(new EWSPressPlayAndFollowSetupFragment());
    }

    public void onNoButtonClicked() {
        screenFlowController.showFragment(new ChooseSetupStateFragment());
    }
}
