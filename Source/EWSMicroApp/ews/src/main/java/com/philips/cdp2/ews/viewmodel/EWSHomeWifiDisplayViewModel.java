/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class EWSHomeWifiDisplayViewModel extends BaseObservable {

    @NonNull private final Navigator navigator;
    @NonNull private final WiFiUtil wiFiUtil;
    private int hierarchyLevel;

    @Inject
    public EWSHomeWifiDisplayViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
    }

    @Bindable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getConnectedWiFiSSID();
    }

    public void updateHomeWiFiSSID() {
        notifyPropertyChanged(BR.homeWiFiSSID);
    }


    public void onNoButtonClicked() {
//        screenFlowController.showFragment(TroubleshootHomeWiFiFragment.getInstance(hierarchyLevel + 1));
    }
    public void onYesButtonClicked() {
//        screenFlowController.showFragment(new EWSDevicePowerOnFragment());
        navigator.navigateToDevicePoweredOnConfirmationScreen();
    }

    public void setHierarchyLevel(final int hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }
}