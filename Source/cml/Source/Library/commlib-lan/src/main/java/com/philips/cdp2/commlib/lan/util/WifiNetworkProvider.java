/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

public class WifiNetworkProvider {

    private final WifiManager wifiManager;

    public WifiNetworkProvider(final @NonNull Context context) {
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public WifiInfo getWifiInfo() {
        return wifiManager.getConnectionInfo();
    }
}
