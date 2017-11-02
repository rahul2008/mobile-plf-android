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
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class ConfirmWifiNetworkViewModel extends BaseObservable {

    public interface ViewCallback {
        void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration);
    }

    @NonNull private final Navigator navigator;
    @NonNull private final WiFiUtil wiFiUtil;

    @NonNull private final StringProvider stringProvider;

    @Nullable private ViewCallback viewCallback;
    @NonNull private BaseContentConfiguration baseContentConfiguration;

    @Inject
    public ConfirmWifiNetworkViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil,
                                       @NonNull BaseContentConfiguration baseConfig,
                                       @NonNull StringProvider stringProvider) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
        this.stringProvider = stringProvider;
        this.baseContentConfiguration = baseConfig;
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
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.note);
        if (viewCallback != null && !wiFiUtil.isHomeWiFiEnabled()) {
            viewCallback.showTroubleshootHomeWifiDialog(baseContentConfiguration);
        }
    }

    public void onNoButtonClicked() {
        if (viewCallback != null) {
            viewCallback.showTroubleshootHomeWifiDialog(baseContentConfiguration);
        }
    }

    public void onYesButtonClicked() {
        navigator.navigateToDevicePoweredOnConfirmationScreen();
    }

    @Bindable
    @NonNull
    public String getTitle() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_currently_connected, getHomeWiFiSSID());
    }

    @Bindable
    @NonNull
    public String getNote() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_want_to_connect,
                baseContentConfiguration.getDeviceName(), getHomeWiFiSSID());
    }

}