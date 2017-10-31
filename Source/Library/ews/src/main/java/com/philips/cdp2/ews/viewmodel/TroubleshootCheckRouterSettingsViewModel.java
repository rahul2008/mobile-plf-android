/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.view.EWSProductSupportFragment;
import com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment;

import javax.inject.Inject;

public class TroubleshootCheckRouterSettingsViewModel {

    private ScreenFlowController screenFlowController;
    public @TroubleshootCheckRouterSettingsFragment.RouterScreenType int screenType;

    @Inject
    public TroubleshootCheckRouterSettingsViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void tagWifiRouterSettings() {
        EWSTagger.trackActionSendData(Tag.KEY.TECHNICAL_ERROR, Tag.VALUE.WIFI_ROUTER_ERROR);
    }

    public void contactUS() {
        screenFlowController.showFragment(new EWSProductSupportFragment());
    }

    public void setScreenType(@TroubleshootCheckRouterSettingsFragment.RouterScreenType int screenType) {
        this.screenType = screenType;
    }

}