package com.philips.platform.ews.troubleshooting.resetconnection;

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

public class ResetConnectionTroubleshootingViewModel {

    @NonNull private final Navigator navigator;
    @NonNull public final ObservableField<String> title;
    @NonNull public final ObservableField<String> description;
    @NonNull private StringProvider stringProvider;
    @NonNull public final Drawable resetConnectionImage;
    @NonNull private final EWSTagger ewsTagger;

    @Inject
    public ResetConnectionTroubleshootingViewModel(@NonNull Navigator navigator, @NonNull StringProvider stringProvider, @NonNull BaseContentConfiguration contentConfiguration,
                                                   @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration, @NonNull final EWSTagger ewsTagger) {
        this.navigator = navigator;
        this.stringProvider = stringProvider;
        this.title = new ObservableField<>(getTitle(troubleShootContentConfiguration, contentConfiguration));
        this.description = new ObservableField<>(getNote(troubleShootContentConfiguration,contentConfiguration));
        this.resetConnectionImage = getResetConnectionImage(troubleShootContentConfiguration);
        this.ewsTagger = ewsTagger;
    }

    void onYesButtonClicked() {
        navigator.navigateToResetDeviceTroubleShootingScreen();
    }

    void onNoButtonClicked() {
        navigator.navigateToConnectToWrongPhoneTroubleShootingScreen();
    }

    @NonNull
    @VisibleForTesting
    Drawable getResetConnectionImage(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        return stringProvider.getImageResource(troubleShootContentConfiguration.getResetConnectionImage());
    }

    @VisibleForTesting
    @NonNull
    String getTitle(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,
                    @NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getResetConnectionTitle(),
                baseConfig.getDeviceName());
    }

    @VisibleForTesting
    @NonNull
    String getNote(@NonNull TroubleShootContentConfiguration troubleShootContentConfiguration,@NonNull BaseContentConfiguration baseConfig) {
        return stringProvider.getString(troubleShootContentConfiguration.getResetConnectionBody(),
                baseConfig.getDeviceName());
    }

    void trackPageName() {
        ewsTagger.trackPage(Page.RESET_CONNECTION);
    }
}
