/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.common.callbacks.DialogHelper;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class EWSHomeWifiDisplayViewModel extends BaseObservable {

    @NonNull private final Navigator navigator;
    @NonNull private final WiFiUtil wiFiUtil;

    @Nullable private DialogHelper dialogHelper;

    @Inject
    public EWSHomeWifiDisplayViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
    }

    public void setDialogHelper(@Nullable DialogHelper dialogHelper) {
        this.dialogHelper = dialogHelper;
    }

    @Bindable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getConnectedWiFiSSID();
    }

    public void refresh() {
        notifyPropertyChanged(BR.homeWiFiSSID);
        if (dialogHelper != null && !wiFiUtil.isHomeWiFiEnabled()) {
//            navigator.navigateToWifiTroubleShootingScreen();
            dialogHelper.showTroubleshootHomeWifi();
        }
    }

    public void onNoButtonClicked() {
//        navigator.navigateToWifiTroubleShootingScreen();
        if (dialogHelper != null) {
            dialogHelper.showTroubleshootHomeWifi();
        }
    }

    public void onYesButtonClicked() {
        navigator.navigateToDevicePoweredOnConfirmationScreen();
    }
}