/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class EWSHomeWifiDisplayViewModel extends BaseObservable {

    @NonNull private final Navigator navigator;
    @NonNull private final WiFiUtil wiFiUtil;

    @NonNull private final StringProvider stringProvider;
    @NonNull public final ObservableField<String> title;
    @NonNull public final ObservableField<String> note;

    @Inject
    public EWSHomeWifiDisplayViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil,
                                       @NonNull BaseContentConfiguration baseConfig,
                                       @NonNull StringProvider stringProvider) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
        this.stringProvider = stringProvider;
        title = new ObservableField<>(getTitle());
        note = new ObservableField<>(getNote(baseConfig));
    }

    @Bindable
    public String getHomeWiFiSSID() {
        return wiFiUtil.getConnectedWiFiSSID();
    }

    public void refresh() {
        notifyPropertyChanged(BR.homeWiFiSSID);
        if (!wiFiUtil.isHomeWiFiEnabled()) {
            navigator.navigateToWifiTroubleShootingScreen();
        }
    }

    public void onNoButtonClicked() {
        navigator.navigateToWifiTroubleShootingScreen();
    }

    public void onYesButtonClicked() {
        navigator.navigateToDevicePoweredOnConfirmationScreen();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @NonNull
    String getTitle() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_currently_connected, getHomeWiFiSSID());
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @NonNull
    String getNote(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_confirm_connection_want_to_connect,
                baseConfig.getDeviceName(), getHomeWiFiSSID());
    }

}