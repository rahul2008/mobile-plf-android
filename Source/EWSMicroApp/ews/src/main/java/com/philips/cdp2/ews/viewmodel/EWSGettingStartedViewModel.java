/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.TroubleshootHomeWiFiFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class EWSGettingStartedViewModel {

    private ScreenFlowController screenFlowController;
    private WiFiUtil wiFiUtil;
    private int hierarchyLevel;

    @Inject
    public EWSGettingStartedViewModel(@NonNull final ScreenFlowController screenFlowController,
                                      @NonNull final WiFiUtil wiFiUtil) {
        this.screenFlowController = screenFlowController;
        this.wiFiUtil = wiFiUtil;
    }

    public void onGettingStartedButtonClicked() {
        if (wiFiUtil.isHomeWiFiEnabled()) {
            screenFlowController.showFragment(new EWSHomeWifiDisplayFragment());
        } else {
            screenFlowController.showFragment(TroubleshootHomeWiFiFragment.getInstance(hierarchyLevel + 1));
        }
    }

    public void setHierarchyLevel(final int hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public void onBackPressed() {
        screenFlowController.finish();
        EWSCallbackNotifier.getInstance().onBackPressed();
    }
}