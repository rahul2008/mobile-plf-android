/*
 * (C) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port.common;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp.dicommclient.networknode.WiFiNode;
import com.philips.cdp2.commlib.core.port.PortProperties;

import java.util.List;

public class WiFiNetworksPortProperties implements PortProperties {

    private static final String KEY_WIFI_NETWORKS_LIST = "strongestssids";

    @SerializedName(KEY_WIFI_NETWORKS_LIST)
    private List<WiFiNode> wifiNetworkList;

    public List<WiFiNode> getWifiNetworkList() {
        return wifiNetworkList;
    }
}
