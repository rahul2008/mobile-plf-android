package com.philips.cdp2.ews.troubleshooting.connecttowrongphone;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.StringProvider;

import javax.inject.Inject;

public class ConnectToWrongPhoneTroubleshootingViewModel {

    @NonNull private final Navigator navigator;
    @NonNull public final ObservableField<String> title;
    @NonNull public final ObservableField<String> description;
    @NonNull public final ObservableField<String> questions;
    @NonNull private StringProvider stringProvider;
    @NonNull public final Drawable connectWrongImage;

    @Inject
    public ConnectToWrongPhoneTroubleshootingViewModel(@NonNull Navigator navigator, @NonNull StringProvider stringProvider, @NonNull BaseContentConfiguration contentConfiguration,
                                                   @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        this.title = new ObservableField<>(getTitle(troubleShootContentConfiguration, contentConfiguration));
        this.description = new ObservableField<>(getBody(troubleShootContentConfiguration, contentConfiguration));
        this.questions = new ObservableField<>(getQuestions(troubleShootContentConfiguration, contentConfiguration));
        this.connectWrongImage = getWrongPhoneImage(troubleShootContentConfiguration);
    }

    @VisibleForTesting
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
    @NonNull
    String getTitle(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getConnectWrongPhoneTitle(),
                baseConfig.getDeviceName());
    }


    @VisibleForTesting
    @NonNull
    String getBody(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getConnectWrongPhoneBody(),
                baseConfig.getDeviceName());
    }

    @VisibleForTesting
    @NonNull
    String getQuestions(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                   @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getConnectWrongPhoneQuestion(),
                baseConfig.getDeviceName());
    }

    void trackPageName() {
        EWSTagger.trackPage(Page.CONNECT_TO_WRONG_PHONE);
    }


}
