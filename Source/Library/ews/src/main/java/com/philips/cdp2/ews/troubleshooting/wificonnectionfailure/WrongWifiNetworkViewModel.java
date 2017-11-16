/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;

import javax.inject.Inject;

public class WrongWifiNetworkViewModel {

    @NonNull
    public final ObservableField<String> upperDescription, lowerDescription;

    @NonNull
    private final Navigator navigator;

    @Nullable
    public Bundle bundle;

    @NonNull
    private BaseContentConfiguration baseContentConfiguration;

    @Inject
    public WrongWifiNetworkViewModel(@NonNull Navigator navigator, @NonNull BaseContentConfiguration baseContentConfiguration) {
        this.navigator = navigator;
        upperDescription = new ObservableField<>();
        lowerDescription= new ObservableField<>();
        this.baseContentConfiguration = baseContentConfiguration;
    }

    void setUpperDescription(@NonNull String name) {
        upperDescription.set(name);
    }

    void setLowerDescription(@NonNull String name) {
        lowerDescription.set(name);
    }

    public void onButtonClick() {
        navigator.navigateToConnectingDeviceWithWifiScreen(bundle);
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    Bundle getBundle() {
        return bundle;
    }

    public int getAppName() {
        return baseContentConfiguration.getAppName();
    }

    void trackPageName() {
        EWSTagger.trackPage(Page.WRONG_WIFI_NETWORK);
    }
}
