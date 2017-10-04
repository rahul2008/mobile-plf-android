/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.ScreenFlowController;

import javax.inject.Inject;

public class EWSWiFIPairedViewModel {

    private ScreenFlowController screenFlowController;

    @Inject
    public EWSWiFIPairedViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void onStartClicked() {
        screenFlowController.finish();
        EWSCallbackNotifier.getInstance().onSuccess();
    }
}