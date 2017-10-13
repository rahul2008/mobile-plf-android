/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

public class WrongWifiNetworkViewModel {

    @NonNull public final ObservableField<String> description;

    @NonNull private final Navigator navigator;

    @Nullable public Bundle bundle;

    @Inject
    public WrongWifiNetworkViewModel(@NonNull Navigator navigator) {
        this.navigator = navigator;
        description = new ObservableField<>();
    }

    void setDescription(@NonNull String name) {
        description.set(name);
    }

    public void onButtonClick() {
        navigator.navigateToConnectingDeviceWithWifiScreen(bundle);
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
