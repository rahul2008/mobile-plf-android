/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.confirmwifi;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.BR;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class ConfirmWifiNetworkViewModel extends BaseObservable {

    interface ViewCallback {
        void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration, @NonNull EWSTagger ewsTagger);
    }

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

    @NonNull private final EWSTagger ewsTagger;

    @Inject
    public ConfirmWifiNetworkViewModel(@NonNull final Navigator navigator,
                                       @NonNull final WiFiUtil wiFiUtil,
                                       @NonNull BaseContentConfiguration baseConfig,
                                       @NonNull StringProvider stringProvider,
                                       @NonNull final EWSTagger ewsTagger) {
        this.navigator = navigator;
        this.wiFiUtil = wiFiUtil;
        this.stringProvider = stringProvider;
        this.baseContentConfiguration = baseConfig;
        this.ewsTagger = ewsTagger;
    }

    protected void setViewCallback(@Nullable ViewCallback viewCallback) {
        this.viewCallback = viewCallback;
    }

    @Bindable
    public String getHomeWiFiSSID() {
        if (TextUtil.isEmpty(wiFiUtil.getConnectedWiFiSSID()) ||
                WiFiUtil.UNKNOWN_SSID.equalsIgnoreCase(wiFiUtil.getConnectedWiFiSSID())) {
            return "";
        }
        return wiFiUtil.getConnectedWiFiSSID();
    }

    void refresh() {
        notifyPropertyChanged(BR.homeWiFiSSID);
        notifyPropertyChanged(BR.title);
        notifyPropertyChanged(BR.note);
        if (viewCallback != null && !wiFiUtil.isHomeWiFiEnabled()) {
            tapToChangeWifi();
            viewCallback.showTroubleshootHomeWifiDialog(baseContentConfiguration, ewsTagger);
        }
    }

    public void onNoButtonClicked() {
        if (viewCallback != null) {
            tapToChangeWifi();
            viewCallback.showTroubleshootHomeWifiDialog(baseContentConfiguration, ewsTagger);
        }
    }

    public void onYesButtonClicked() {
        if (wiFiUtil.isHomeWiFiEnabled()) {
            tapToConnect();
            navigator.navigateToDevicePoweredOnConfirmationScreen();
        } else {
            tapToChangeWifi();
            if (viewCallback != null) {
                viewCallback.showTroubleshootHomeWifiDialog(baseContentConfiguration, ewsTagger);
            }
        }
    }

    private void tapToConnect() {
        ewsTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.CONFIRM_NETWORK);
    }

    private void tapToChangeWifi() {
        ewsTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.CHANGE_NETWORK);
    }

    @Bindable
    @NonNull
    public String getTitle() {
        return getHomeWiFiSSID();
    }

    @Bindable
    @NonNull
    public String getNote() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_want_to_connect,
                baseContentConfiguration.getDeviceName(), getHomeWiFiSSID());
    }

    @Bindable
    @NonNull
    public String getHelperTitle() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_tip_upper,
                baseContentConfiguration.getDeviceName());
    }

    @Bindable
    @NonNull
    public String getHelperDescription() {
        return stringProvider.getString(R.string.label_ews_confirm_connection_tip_lower,
                baseContentConfiguration.getDeviceName());
    }

    public void trackPageName() {
        ewsTagger.trackPage(Page.CONFIRM_WIFI_NETWORK);
    }

}