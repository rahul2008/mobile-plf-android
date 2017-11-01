/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class ConfirmWifiNetworkViewModel extends BaseObservable {

    @NonNull
    public final ObservableField<String> title;
    @NonNull
    public final ObservableField<String> note;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final StringProvider stringProvider;
    @Nullable
    private ViewCallback viewCallback;
    @NonNull
    private BaseContentConfiguration baseContentConfiguration;

    @Inject
    public ConfirmWifiNetworkViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil,
                                       @NonNull BaseContentConfiguration baseConfig,
                                       @NonNull StringProvider stringProvider) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
        this.stringProvider = stringProvider;
        this.baseContentConfiguration = baseConfig;
        title = new ObservableField<>(getTitle());
        note = new ObservableField<>(getNote(baseConfig));
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

    @VisibleForTesting
    @NonNull
    String getTitle() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_currently_connected, getHomeWiFiSSID());
    }

    @VisibleForTesting
    @NonNull
    String getNote(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_confirm_connection_tip,  baseConfig.getDeviceName());
    }

    public interface ViewCallback {
        void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration);
    }

}