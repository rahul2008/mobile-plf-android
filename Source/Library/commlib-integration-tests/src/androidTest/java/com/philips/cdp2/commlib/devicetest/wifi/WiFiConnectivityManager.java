/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.devicetest.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;


import com.philips.cdp2.commlib.devicetest.util.TextUtil;

import java.util.List;


@SuppressWarnings("WeakerAccess")
public class WiFiConnectivityManager {

    private static final int DELAY_IN_EACH_NEW_SCAN = 500;
    private static int maxAttempts = 10;
    private final WifiManager wifiManager;
    private Wifi wifi;
    private WiFiUtil wiFiUtil;
    @VisibleForTesting
    Handler handler;

    public WiFiConnectivityManager(@NonNull final WifiManager wifiManager,
                                   @NonNull final Wifi wifi,
                                   @NonNull final WiFiUtil wiFiUtil) {
        this.wifiManager = wifiManager;
        this.wifi = wifi;
        this.wiFiUtil = wiFiUtil;
        handler = new Handler();
    }

    public void connectToHomeWiFiNetwork(@NonNull final String ssid) {
        connectToNetwork(ssid);
    }

    public void connectToApplianceHotspotNetwork(final String deviceHotspotSSID) {
        wiFiUtil.forgetHotSpotNetwork(deviceHotspotSSID);
        configureOpenNetwork();

        if (!TextUtil.isEmpty(wiFiUtil.getCurrentWiFiSSID())) {
            connectToNetwork("NOPE");
        }
    }

    private void configureOpenNetwork() {
        WifiConfiguration wfc = new WifiConfiguration();
        wfc.SSID = "\"" + "NOPE" + "\"";
        wfc.status = WifiConfiguration.Status.ENABLED;
        wfc.priority = 40;
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        wfc.allowedAuthAlgorithms.clear();
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        wifiManager.addNetwork(wfc);
        wifiManager.saveConfiguration();
    }


    private void connectToNetwork(@NonNull final String ssid) {
        tryToConnectToNetwork(ssid, 0);
    }

    private void tryToConnectToNetwork(@NonNull final String ssid, final int attempt) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ScanResult accessPoint = null;
                accessPoint = findNetworkAccessPoint(ssid);
                if (accessPoint != null) {
                } else {
                    if (attempt < maxAttempts) {
                        wifiManager.startScan();
                        tryToConnectToNetwork(ssid, attempt + 1);
                        return;
                    }
                }
                if (accessPoint == null) {
                    return;
                }
                wifi.connectToConfiguredNetwork(wifiManager, accessPoint);
            }
        }, DELAY_IN_EACH_NEW_SCAN);
    }

    private ScanResult findNetworkAccessPoint(@NonNull final String ssid) {
        List<ScanResult> foundNetworks = wifiManager.getScanResults();
        if (foundNetworks != null) {
            for (ScanResult foundNetwork : foundNetworks) {
                if (foundNetwork.SSID.equals(ssid)) {
                    return foundNetwork;
                }
            }
        }

        return null;
    }

    @VisibleForTesting
    public void setMaxScanAttempts(final int maxAttempts) {
        WiFiConnectivityManager.maxAttempts = maxAttempts;
    }
}
