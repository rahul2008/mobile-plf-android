package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import javax.inject.Inject;

/**
 * Created by salvatorelafiura on 11/10/2017.
 */

public class WrongWifiNetworkViewModel {
    @NonNull
    public ObservableField<String> networkName = new ObservableField<>();

    @Inject
    public WrongWifiNetworkViewModel() {
    }

    public void setNetworkName(@NonNull String name){
        networkName.set(name);
    }
}
