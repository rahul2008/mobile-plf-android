/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class TroubleshootIncorrectPasswordViewModel {

    private ScreenFlowController screenFlowController;

    @Inject
    public TroubleshootIncorrectPasswordViewModel(@NonNull final ScreenFlowController screenFlowController) {
        this.screenFlowController = screenFlowController;
    }

    public void connectAgain() {
        screenFlowController.showFragment(new EWSHomeWifiDisplayFragment());
    }

    public void checkRouterSettings() {
        screenFlowController.showFragment(TroubleshootCheckRouterSettingsFragment.getInstance(TroubleshootCheckRouterSettingsFragment.ROUTER_ERROR_NO_NETWORK));
    }

    public void tagIncorrectPassword() {
        Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, Tag.VALUE.WRONG_PASSWORD_NOTIFICATION);
        map.put(Tag.KEY.CONNECTED_PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
        map.put(Tag.KEY.USER_ERROR, Tag.VALUE.WRONG_PASSWORD_ERROR);

        EWSTagger.trackAction(Tag.ACTION.CONNECTION_UNSUCCESSFUL, map);
    }
}