/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.troubleshooting.connecttowrongphone;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.TroubleShootContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.util.StringProvider;

import javax.inject.Inject;

public class ConnectToWrongPhoneTroubleshootingViewModel {

    @NonNull
    public final ObservableField<String> title;
    @NonNull
    public final ObservableField<String> description;
    @NonNull
    public final ObservableField<String> questions;
    @NonNull
    public final Drawable connectWrongImage;
    @NonNull
    private final Navigator navigator;
    @NonNull
    private final StringProvider stringProvider;

    @NonNull private final EWSTagger ewsTagger;

    @Inject
    public ConnectToWrongPhoneTroubleshootingViewModel(@NonNull Navigator navigator,
                                                       @NonNull StringProvider stringProvider,
                                                       @NonNull BaseContentConfiguration contentConfiguration,
                                                       @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                                                       @NonNull final EWSTagger ewsTagger) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        this.title = new ObservableField<>(getTitle(troubleShootContentConfiguration, contentConfiguration));
        this.description = new ObservableField<>(getBody(troubleShootContentConfiguration, contentConfiguration));
        this.questions = new ObservableField<>(getQuestions(troubleShootContentConfiguration, contentConfiguration));
        this.connectWrongImage = getWrongPhoneImage(troubleShootContentConfiguration);
        this.ewsTagger = ewsTagger;

    }

    @NonNull
    Drawable getWrongPhoneImage(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        return stringProvider.getImageResource(troubleShootContentConfiguration.getConnectWrongPhoneImage());
    }

    void onYesButtonClicked() {
        navigator.navigateSetupAccessPointModeScreen();
    }

    void onNoButtonClicked() {
        navigator.navigateToResetConnectionTroubleShootingScreen();
    }

    @VisibleForTesting
    private String getTitle(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getConnectWrongPhoneTitle(),
                baseConfig.getDeviceName());
    }

    @NonNull
    @VisibleForTesting
    private String getBody(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                   @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getConnectWrongPhoneBody(),
                baseConfig.getDeviceName());
    }

    @NonNull
    @VisibleForTesting
    private String getQuestions(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                        @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getConnectWrongPhoneQuestion(),
                baseConfig.getDeviceName());
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.CONNECT_TO_WRONG_PHONE);
    }


}
