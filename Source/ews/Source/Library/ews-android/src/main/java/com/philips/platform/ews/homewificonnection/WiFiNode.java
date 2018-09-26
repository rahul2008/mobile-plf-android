package com.philips.platform.ews.homewificonnection;

import android.support.annotation.NonNull;

public class WiFiNode {
    public final String SSID;
    public final int RSSI;

    public WiFiNode(@NonNull final String SSID, final int RSSI) {
        this.SSID = SSID;
        this.RSSI = RSSI;
    }

    @Override
    public String toString() {
        return "WiFiNode{" + "SSID='" + SSID + '\'' + ", RSSI=" + RSSI + '}';
    }
}
