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
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class EWSHomeWifiDisplayViewModel extends BaseObservable {

    public interface ViewCallback {
        void showTroubleshootHomeWifiDialog();
    }

    @NonNull private final Navigator navigator;
    @NonNull private final WiFiUtil wiFiUtil;

    @Nullable private ViewCallback viewCallback;

    @Inject
    public EWSHomeWifiDisplayViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
    }

    public void setViewCallback(@Nullable ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
    }

    @Bindable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getConnectedWiFiSSID();
    }

    public void refresh() {
        notifyPropertyChanged(BR.homeWiFiSSID);
        if (viewCallback != null && !wiFiUtil.isHomeWiFiEnabled()) {
            viewCallback.showTroubleshootHomeWifiDialog();
        }
    }

    public void onNoButtonClicked() {
        if (viewCallback != null) {
            viewCallback.showTroubleshootHomeWifiDialog();
        }
    }

    public void onYesButtonClicked() {
        navigator.navigateToDevicePoweredOnConfirmationScreen();
    }
}