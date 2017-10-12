package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

/**
 * Created by salvatorelafiura on 11/10/2017.
 */

public class WIFIConnectionUnsuccessfulViewModel {

    @NonNull
    private final WiFiUtil wiFiUtil;
    @NonNull
    private final Navigator navigator;
    @NonNull
    public ObservableField<String> description = new ObservableField<>();

    @Inject
    public WIFIConnectionUnsuccessfulViewModel(@NonNull WiFiUtil wiFiUtil,@NonNull Navigator navigator) {
        this.wiFiUtil = wiFiUtil;
        this.navigator = navigator;
    }

    public void setDescription(@NonNull String description) {
        this.description.set(description);
    }

    public void onTryAgainClicked() {
        if (!wiFiUtil.isWifiConnectedToNetwork() || wiFiUtil.isConnectedToPhilipsSetup()){
            //TODO lynn is verifying this flow
        } else{
            navigator.navigateToHomeNetworkConfirmationScreen();
        }
    }
}
