/**
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.troubleshooting.networknotlisted;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.util.StringProvider;

import javax.inject.Inject;

public class NetworkNotListedViewModel {

    @NonNull
    public final ObservableField<String> stepOneText;

    @NonNull
    public final ObservableField<String> stepTwoText;

    @NonNull
    private final Navigator navigator;

    @NonNull private final EWSTagger ewsTagger;

    @NonNull private final BaseContentConfiguration baseContentConfiguration;

    @NonNull private final StringProvider stringProvider;

    @Inject
    public NetworkNotListedViewModel(@NonNull Navigator navigator, @NonNull final BaseContentConfiguration baseConfig, @NonNull StringProvider stringProvider, @NonNull final EWSTagger ewsTagger) {
        this.navigator = navigator;
        this.baseContentConfiguration = baseConfig;
        this.stringProvider = stringProvider;
        stepOneText = new ObservableField<>(getStepOneText(baseContentConfiguration));
        stepTwoText = new ObservableField<>(getStepTwoText(baseContentConfiguration));
        this.ewsTagger = ewsTagger;
    }

    @VisibleForTesting
    @NonNull
    String getStepOneText(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_network_not_listed_instruction_1,
                baseConfig.getDeviceName());
    }

    @VisibleForTesting
    @NonNull
    String getStepTwoText(@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(R.string.label_ews_network_not_listed_instruction_2,
                baseConfig.getDeviceName());
    }

    public void onOkClicked() {
        navigator.navigateBack();
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.NETWORK_NOT_LISTED);
    }
}