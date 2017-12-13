package com.philips.platform.ews.homewificonnection;

import android.support.annotation.NonNull;

class StartConnectionModel {
    @NonNull private final String homeWiFiSSID;
    @NonNull private final String homeWiFiPassword;
    @NonNull private final String deviceName;
    @NonNull private final String deviceFriendlyName;

    StartConnectionModel(@NonNull String homeWiFiSSID,
                         @NonNull String homeWiFiPassword, @NonNull String deviceName,
                         @NonNull String deviceFriendlyName) {
        this.homeWiFiSSID = homeWiFiSSID;
        this.homeWiFiPassword = homeWiFiPassword;
        this.deviceName = deviceName;
        this.deviceFriendlyName = deviceFriendlyName;
    }

    @NonNull
    String getHomeWiFiSSID() {
        return homeWiFiSSID;
    }

    @NonNull
    String getHomeWiFiPassword() {
        return homeWiFiPassword;
    }

    @NonNull
    String getDeviceName() {
        return deviceName;
    }

    @NonNull
    String getDeviceFriendlyName() {
        return deviceFriendlyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartConnectionModel that = (StartConnectionModel) o;

        if (!homeWiFiSSID.equals(that.homeWiFiSSID)) return false;
        if (!homeWiFiPassword.equals(that.homeWiFiPassword)) return false;
        if (!deviceName.equals(that.deviceName)) return false;
        return deviceFriendlyName.equals(that.deviceFriendlyName);
    }

    @Override
    public int hashCode() {
        int result = homeWiFiSSID.hashCode();
        result = 31 * result + homeWiFiPassword.hashCode();
        result = 31 * result + deviceName.hashCode();
        result = 31 * result + deviceFriendlyName.hashCode();
        return result;
    }
}
