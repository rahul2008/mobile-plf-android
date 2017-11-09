package com.philips.cdp2.ews.troubleshooting.setupaccesspointmode;

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

public class SetupAccessPointModeTroubleshootingViewModel {

    @NonNull
    private final Navigator navigator;



    @NonNull public final ObservableField<String> title;
    @NonNull public final ObservableField<String> description;
    @NonNull private StringProvider stringProvider;
    @NonNull public final Drawable setupAccessPointImage;

    @Inject
    public SetupAccessPointModeTroubleshootingViewModel(@NonNull Navigator navigator, @NonNull StringProvider stringProvider, @NonNull BaseContentConfiguration contentConfiguration,
                                                   @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        this.title = new ObservableField<>(getTitle(troubleShootContentConfiguration, contentConfiguration));
        this.description = new ObservableField<>(getNote(troubleShootContentConfiguration, contentConfiguration));
        this.setupAccessPointImage = getSetupAccessPointImage(troubleShootContentConfiguration);
    }

    public void onDoneButtonClicked() {
        navigator.navigateToCompletingDeviceSetupScreen();
    }

    @VisibleForTesting
    @NonNull
    Drawable getSetupAccessPointImage(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        return stringProvider.getImageResource(troubleShootContentConfiguration.getSetUpAccessPointImage());
    }

    public void onYesButtonClicked() {
        navigator.navigateToResetDeviceTroubleShootingScreen();
    }

    public void onNoButtonClicked() {
        navigator.navigateToConnectToWrongPhoneTroubleShootingScreen();
    }

    @VisibleForTesting
    @NonNull
    String getTitle(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getSetUpAccessPointTitle(),
                baseConfig.getDeviceName());
    }

    @VisibleForTesting
    @NonNull
    String getNote(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getSetUpAccessPointBody(),
                baseConfig.getDeviceName());
    }

    void trackPageName() {
        EWSTagger.trackPage(Page.SETUP_ACCESS_POINT_MODE);
    }
}

