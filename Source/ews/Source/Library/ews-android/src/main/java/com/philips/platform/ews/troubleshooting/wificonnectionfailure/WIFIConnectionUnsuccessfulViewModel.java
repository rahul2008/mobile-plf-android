/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class WIFIConnectionUnsuccessfulViewModel {

    @NonNull
    public final ObservableField<String> upperBodyText;

    @NonNull
    public final ObservableField<String> stepTwoText;

    @NonNull
    public final ObservableField<String> upperHelperText;

    @NonNull
    public final ObservableField<String> lowerHelperText;

    @NonNull
    private final Navigator navigator;

    @NonNull private final EWSTagger ewsTagger;
    @NonNull
    private final WiFiUtil wiFiUtil;

    @Inject
    public WIFIConnectionUnsuccessfulViewModel(@NonNull WiFiUtil wiFiUtil, @NonNull Navigator navigator, @NonNull final EWSTagger ewsTagger) {
        this.wiFiUtil = wiFiUtil;
        this.navigator = navigator;
        upperBodyText = new ObservableField<>();
        stepTwoText = new ObservableField<>();
        upperHelperText = new ObservableField<>();
        lowerHelperText = new ObservableField<>();
        this.ewsTagger = ewsTagger;
    }

    public void setUpperBodyText(@NonNull String upperBodyText) {
        this.upperBodyText.set(upperBodyText);
    }

    public void setStepTwoText(@NonNull String stepTwoText) {
        this.stepTwoText.set(stepTwoText);
    }

    public void setUpperHelperText(@NonNull String upperHelperText) {
        this.upperHelperText.set(upperHelperText);
    }

    public void setLowerHelperText(@NonNull String lowerHelperText) {
        this.lowerHelperText.set(lowerHelperText);
    }

    public void onTryAgainClicked() {
        wiFiUtil.forgetHotSpotNetwork(wiFiUtil.DEVICE_SSID);
        navigator.navigateToConnectingPhoneToHotspotWifiScreen();
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.CONNECTION_UNSUCCESSFUL);
    }
}
