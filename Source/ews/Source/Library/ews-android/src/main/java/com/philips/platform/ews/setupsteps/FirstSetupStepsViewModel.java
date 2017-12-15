/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.setupsteps;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.util.StringProvider;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class FirstSetupStepsViewModel {

    @NonNull
    public final ObservableField<String> body;
    @NonNull
    public final ObservableField<String> title;
    @NonNull
    public final Drawable image;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final StringProvider stringProvider;
    @NonNull private final EWSTagger ewsTagger;

    @Inject
    public FirstSetupStepsViewModel(@NonNull final Navigator navigator,
                                    @NonNull final StringProvider stringProvider,
                                    @NonNull final BaseContentConfiguration baseConfiguration,
                                    @NonNull final HappyFlowContentConfiguration happyFlowContentConfiguration,
                                    @NonNull final EWSTagger ewsTagger) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        this.body = new ObservableField<>(getBody(baseConfiguration, happyFlowContentConfiguration));
        this.title = new ObservableField<>(getTitle(happyFlowContentConfiguration));
        this.image = getImage(happyFlowContentConfiguration);
        this.ewsTagger = ewsTagger;
    }

    @VisibleForTesting
    @NonNull
    String getTitle(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpScreenTitle());
    }

    @VisibleForTesting
    @NonNull
    String getBody(@NonNull BaseContentConfiguration baseConfig, @NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getString(happyFlowContentConfiguration.getSetUpScreenBody(), baseConfig.getDeviceName());
    }

    @NonNull
    @VisibleForTesting
    Drawable getImage(@NonNull HappyFlowContentConfiguration happyFlowContentConfiguration) {
        return stringProvider.getImageResource(happyFlowContentConfiguration.getSetUpScreenImage());
    }

    public void onYesButtonClicked() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.SETUP_STEP1);
    }
}
