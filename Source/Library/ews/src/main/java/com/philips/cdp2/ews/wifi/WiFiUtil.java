/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.wifi;

import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.util.TextUtil;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings("WeakerAccess")
@Singleton
public class WiFiUtil {

    private static final String TAG = "WiFiUtil";
    public static final String DEVICE_SSID = "PHILIPS Setup";
    public static final String UNKNOWN_SSID = "<unknown ssid>";
    private WifiManager wifiManager;

    private String lastWifiSSid;

    public static final int HOME_WIFI = 1;
    public static final int WRONG_WIFI = 2;
    public static final int UNKNOWN_WIFI = 3;
    public static final int DEVICE_HOTSPOT_WIFI = 4;

    @IntDef({HOME_WIFI, WRONG_WIFI, DEVICE_HOTSPOT_WIFI, UNKNOWN_WIFI})
    public @interface WiFiState {
    }

    @Inject
    public WiFiUtil(@NonNull WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    @Nullable
    public String getHomeWiFiSSD() {
        return lastWifiSSid;
    }

    public String getConnectedWiFiSSID() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
            return getFormattedSSID(wifiInfo.getSSID());
        }
        return null;
    }

    public String getFormattedSSID(@NonNull final String SSID) {
        return SSID.replace("\"", "");
    }

    @Nullable
    public String getCurrentWiFiSSID() {
        lastWifiSSid = getConnectedWiFiSSID();
        return lastWifiSSid;
    }

    public boolean isHomeWiFiEnabled() {
        return wifiManager.isWifiEnabled() && isWifiConnectedToNetwork() && (!DEVICE_SSID
                .equals(getCurrentWiFiSSID()));
    }

    public boolean isWifiConnectedToNetwork() {
        return getConnectedWiFiSSID() != null && !TextUtil.isEmpty(getConnectedWiFiSSID());
    }

    public boolean isConnectedToPhilipsSetup() {
        return getCurrentWiFiSSID() != null && DEVICE_SSID.equals(getCurrentWiFiSSID());
    }

    public
    @WiFiState
    int getCurrentWifiState() {
        String currentWifi = getConnectedWiFiSSID();
        //EWSLogger.d(TAG, "Connected to:" + (currentWifi == null ? "Nothing" : currentWifi));

        if (lastWifiSSid == null || currentWifi == null || currentWifi
                .equalsIgnoreCase(UNKNOWN_SSID)) {
            return UNKNOWN_WIFI;
        } else if (currentWifi.contains(DEVICE_SSID)) {
            return DEVICE_HOTSPOT_WIFI;
        } else if (currentWifi.contains(lastWifiSSid)) {
            return HOME_WIFI;
        } else if (!lastWifiSSid.equals(currentWifi)
                && !lastWifiSSid.equals(DEVICE_SSID)) {
//            EWSLogger.d(TAG,
//                    "Connected to wrong wifi, Current wifi " + currentWifi + " Home wifi " +
//                            lastWifiSSid);
            return WRONG_WIFI;
        }
        return UNKNOWN_WIFI;
    }

    public void forgetHotSpotNetwork(String hotSpotWiFiSSID) {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            //EWSLogger.d(TAG, "Pre configured Wifi ssid " + config.SSID);
            String cleanSSID = config.SSID;
            cleanSSID = cleanSSID.replaceAll("^\"|\"$", "");
            if (cleanSSID.equals(hotSpotWiFiSSID)) {
                boolean success = wifiManager.removeNetwork(config.networkId);
                wifiManager.saveConfiguration();
                //EWSLogger.i(TAG, "Removing network " + success);
            }
        }
    }
}
