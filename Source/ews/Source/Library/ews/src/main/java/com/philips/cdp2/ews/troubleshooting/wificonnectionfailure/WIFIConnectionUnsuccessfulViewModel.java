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

    @Inject
    public WIFIConnectionUnsuccessfulViewModel(@NonNull Navigator navigator, @NonNull final EWSTagger ewsTagger) {
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
        navigator.navigateToHomeNetworkConfirmationScreen();
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.CONNECTION_UNSUCCESSFUL);
    }
}
