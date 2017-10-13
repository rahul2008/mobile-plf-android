package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

/**
 * Created by salvatorelafiura on 11/10/2017.
 */

public class WIFIConnectionUnsuccessfulViewModel {

    @NonNull public final ObservableField<String> description;

    @NonNull private final Navigator navigator;

    @Inject
    public WIFIConnectionUnsuccessfulViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
        description = new ObservableField<>();
    }

    public void setDescription(@NonNull String description) {
        this.description.set(description);
    }

    public void onTryAgainClicked() {
        navigator.navigateToHomeNetworkConfirmationScreen();
    }
}
