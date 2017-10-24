package com.philips.cdp2.ews.troubleshooting.connecttowrongphone;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.troubleshooting.TroubleShootBaseViewModel;

import javax.inject.Inject;

public class ConnectToWrongPhoneTroubleshootingViewModel extends TroubleShootBaseViewModel{

    @NonNull private final Navigator navigator;

    public void onYesButtonClicked() {
        navigator.navigateSetupAccessPointModeScreen();
    }

    public void onNoButtonClicked() {
        navigator.navigateToResetConnectionTroubleShootingScreen();
    }

    @Inject
    public ConnectToWrongPhoneTroubleshootingViewModel(@NonNull Navigator navigator, @NonNull StringProvider stringProvider, @NonNull BaseContentConfiguration contentConfiguration,
                                                   @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration) {
        super(stringProvider, contentConfiguration, troubleShootContentConfiguration);
        this.navigator = navigator;
    }

}
