/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;

import javax.inject.Inject;

public class WIFIConnectionUnsuccessfulViewModel {

    @NonNull
    public final ObservableField<String> upperBody;

    @NonNull
    public final ObservableField<String> step2;

    @NonNull
    public final ObservableField<String> upperHelper;

    @NonNull
    public final ObservableField<String> lowerHelper;

    @NonNull
    private final Navigator navigator;

    @Inject
    public WIFIConnectionUnsuccessfulViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
        upperBody = new ObservableField<>();
        step2 = new ObservableField<>();
        upperHelper = new ObservableField<>();
        lowerHelper = new ObservableField<>();
    }

    public void setUpperBody(@NonNull String upperBody) {
        this.upperBody.set(upperBody);
    }

    public void setStep2(@NonNull String step2) {
        this.step2.set(step2);
    }

    public void setUpperHelper(@NonNull String upperHelper) {
        this.upperHelper.set(upperHelper);
    }

    public void setLowerHelper(@NonNull String lowerHelper) {
        this.lowerHelper.set(lowerHelper);
    }

    public void onTryAgainClicked() {
        navigator.navigateToHomeNetworkConfirmationScreen();
    }

    void trackPageName() {
        EWSTagger.trackPage(Page.CONNECTION_UNSUCCESSFUL);
    }
}
