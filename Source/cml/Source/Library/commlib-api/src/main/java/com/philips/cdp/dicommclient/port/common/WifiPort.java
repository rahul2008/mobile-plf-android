/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Port for getting/setting wifi and networking configuration on a WiFi connected {@link Appliance}.
 *
 * @publicApi
 */
public class WifiPort extends DICommPort<WifiPortProperties> {

    private static final String KEY_GATEWAY = "gateway";
    private static final String KEY_SUBNETMASK = "netmask";
    private static final String KEY_DHCP = "dhcp";
    private static final String KEY_IPADDRESS = "ipaddress";
    private static final String KEY_WIFIPASSWORD = "password";
    private static final String KEY_WIFISSID = "ssid";

    /**
     * Create a {@link WifiPort}.
     *
     * @param communicationStrategy The communication strategy for the port to use.
     * @see DICommPort#DICommPort(CommunicationStrategy)
     */
    public WifiPort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    private final String WIFIPORT_NAME = "wifi";
    private final int WIFIPORT_PRODUCTID = 0;

    @Override
    public void processResponse(String jsonResponse) {
        WifiPortProperties wifiPortProperties = parseResponse(jsonResponse);
        if (wifiPortProperties != null) {
            setPortProperties(wifiPortProperties);
            return;
        }
        DICommLog.e(DICommLog.WIFIPORT, "Wifi port properties should never be NULL");

    }

    @Override
    public String getDICommPortName() {
        return WIFIPORT_NAME;
    }

    @Override
    public int getDICommProductId() {
        return WIFIPORT_PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return false;
    }

    private WifiPortProperties parseResponse(String response) {
        if (response == null || response.isEmpty()) {
            return null;
        }

        WifiPortProperties wifiPortProperties = null;
        try {
            wifiPortProperties = gson.fromJson(response, WifiPortProperties.class);
        } catch (Exception e) {
            DICommLog.e(DICommLog.WIFIPORT, e.getMessage());
        }
        return wifiPortProperties;
    }

    /**
     * Convenience method to easily set WiFi related configuration.
     *
     * @param ssid     WiFi network SSID.
     * @param password WiFi network password.
     */
    public void setWifiNetworkDetails(String ssid, String password) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(KEY_WIFISSID, ssid);
        dataMap.put(KEY_WIFIPASSWORD, password);
        putProperties(dataMap);
    }

    /**
     * Convenience method to easily set WiFi and networking related configuration.
     *
     * @param ssid       WiFi network SSID.
     * @param password   WiFi network password.
     * @param ipAddress  Fixed IP address to use if dhcp is <code>false</code>.
     * @param dhcp       <code>true</code> if the {@link Appliance} should use DHCP.
     * @param subnetMask Subnet mask for the {@link Appliance}.
     * @param gateWay    IP address of the network gateway.
     */
    public void setWifiNetworkDetails(String ssid, String password, String ipAddress, boolean dhcp, String subnetMask, String gateWay) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put(KEY_WIFISSID, ssid);
        dataMap.put(KEY_WIFIPASSWORD, password);
        dataMap.put(KEY_IPADDRESS, ipAddress);
        dataMap.put(KEY_DHCP, dhcp);
        dataMap.put(KEY_SUBNETMASK, subnetMask);
        dataMap.put(KEY_GATEWAY, gateWay);
        putProperties(dataMap);
    }
}
