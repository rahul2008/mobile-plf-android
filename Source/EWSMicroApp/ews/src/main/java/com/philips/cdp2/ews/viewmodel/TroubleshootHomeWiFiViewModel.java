/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.communication.events.FoundHomeNetworkEvent;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Named;

@SuppressWarnings("WeakerAccess")
public class TroubleshootHomeWiFiViewModel {

    private ScreenFlowController screenFlowController;
    private EventBus eventBus;
    private WiFiUtil wiFiUtil;
    private boolean foundNetwork;

    @Inject
    public TroubleshootHomeWiFiViewModel(@NonNull final ScreenFlowController screenFlowController,
                                         @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                         @NonNull final WiFiUtil wiFiUtil) {
        this.screenFlowController = screenFlowController;
        this.eventBus = eventBus;
        this.wiFiUtil = wiFiUtil;
        this.eventBus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHomeNetworkFound(@SuppressWarnings("UnusedParameters") @NonNull final FoundHomeNetworkEvent event) {
        this.foundNetwork = true;
        showHomeWifiDisplayScreen();
    }

    public void checkHomeWiFiNetwork() {
        if (wiFiUtil.isHomeWiFiEnabled() && foundNetwork) {
            showHomeWifiDisplayScreen();
        }
    }

    private void showHomeWifiDisplayScreen() {
        screenFlowController.popBackStack();
        screenFlowController.showFragment(new EWSHomeWifiDisplayFragment());
    }

    public void unregister() {
        this.eventBus.unregister(this);
    }
}