/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.TroubleshootIncorrectPasswordFragment;
import com.philips.cdp2.ews.view.TroubleshootWrongWiFiFragment;

import javax.inject.Inject;

public class TroubleshootConnectionUnsuccessfulViewModel {

    @NonNull
    private ScreenFlowController screenFlowController;

    @Inject
    public TroubleshootConnectionUnsuccessfulViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void showConnectionErrorDifferentNetwork() {
        screenFlowController.showFragment(new TroubleshootWrongWiFiFragment());
    }

    public void showConnectionErrorDeviceNotFound() {
        screenFlowController.showFragment(new TroubleshootIncorrectPasswordFragment());
    }
}
