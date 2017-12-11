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
    public final ObservableField<String> upperDescription, lowerDescription, stepFourText;

    @NonNull
    private final Navigator navigator;
    @NonNull
    private final EWSTagger ewsTagger;
    @Nullable
    private Bundle bundle;
    @NonNull
    private BaseContentConfiguration baseContentConfiguration;

    @Inject
    public WrongWifiNetworkViewModel(@NonNull Navigator navigator, @NonNull BaseContentConfiguration baseContentConfiguration,
                                     @NonNull final EWSTagger ewsTagger) {
        this.navigator = navigator;
        upperDescription = new ObservableField<>();
        lowerDescription = new ObservableField<>();
        stepFourText = new ObservableField<>();
        this.baseContentConfiguration = baseContentConfiguration;
        this.ewsTagger = ewsTagger;
    }

    void setUpperDescription(@NonNull String name) {
        upperDescription.set(name);
    }

    void setLowerDescription(@NonNull String name) {
        lowerDescription.set(name);
    }

    void setStepFourText(@NonNull String name) {
        stepFourText.set(name);
    }

    public void onButtonClick() {
        navigator.navigateToConnectingDeviceWithWifiScreen(bundle, true);
    }

    @Nullable
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    Bundle getBundle() {
        return bundle;
    }

    void setBundle(@Nullable Bundle bundle) {
        this.bundle = bundle;
    }

    int getAppName() {
        return baseContentConfiguration.getAppName();
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.WRONG_WIFI_NETWORK);
    }
}
