/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.common.util.Tagger;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordFragment;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;
import com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class TroubleshootWrongWiFiViewModel {

    private final WiFiUtil wiFiUtil;
    private final EventBus eventBus;
    private final ScreenFlowController screenFlowController;
    private boolean isVisible;
    private boolean pairingSuccess;

    @Inject
    public TroubleshootWrongWiFiViewModel(@NonNull ScreenFlowController screenFlowController,
                                          @NonNull @Named("ews.event.bus") final EventBus eventBus,
                                          @NonNull final WiFiUtil wiFiUtil) {
        this.wiFiUtil = wiFiUtil;
        this.eventBus = eventBus;
        this.screenFlowController = screenFlowController;
        eventBus.register(this);
    }

    public String getHomeWifi() {
        return wiFiUtil.getHomeWiFiSSD();
    }

    public void tagWrongWifi() {
        Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.CONNECTED_PRODUCT_NAME, EWSDependencyProvider.getInstance().getProductName());
        map.put(Tag.KEY.USER_ERROR, Tag.VALUE.WRONG_WIFI);

        Tagger.trackAction(Tag.ACTION.SEND_DATA, map);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void showPairingSuccessEvent(@NonNull final PairingSuccessEvent event) {
        pairingSuccess = true;
        screenFlowController.showFragment(new EWSWiFiPairedFragment());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void showWifiConnectionScreen(@NonNull final DiscoverApplianceEvent event) {
        if(isVisible && !pairingSuccess) {
            screenFlowController.showFragment(new ConnectWithPasswordFragment());
        }
    }

    public void unregister() {
        eventBus.unregister(this);
    }

    public void launchRouterTroubleshooting() {
        screenFlowController.showFragment(TroubleshootCheckRouterSettingsFragment.getInstance(
                TroubleshootCheckRouterSettingsFragment.ROUTER_ERROR_DIFFERENT_NETWORK));
    }

    public void start() {
        isVisible = true;
    }

    public void stop() {
        isVisible = false;
    }
}
