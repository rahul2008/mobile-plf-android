/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.setupsteps;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class FirstSetupStepsViewModel {

    @NonNull private final Navigator navigator;

    @NonNull private final StringProvider stringProvider;

    @NonNull
    public final ObservableField<String> body;

    @Inject
    public FirstSetupStepsViewModel(@NonNull final Navigator navigator,
                                    @NonNull final StringProvider stringProvider,
                                    @NonNull final BaseContentConfiguration baseConfiguration) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        this.body = new ObservableField<>(getBody(baseConfiguration));
    }

    public void onYesButtonClicked() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }

    @VisibleForTesting
    @NonNull
    public String getBody(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_plug_in_body_default,
                baseConfig.getDeviceName());
    }
}
