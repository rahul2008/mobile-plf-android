/*
 * Wifi Connecter
 * <p>
 * Copyright (c) 20101 Kevin Yuan (farproc@gmail.com)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 **/
package com.philips.cdp2.commlib.devicetest.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;

import com.philips.cdp2.commlib.devicetest.util.TextUtil;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class Wifi {

    private static final int MAX_PRIORITY = 99999;
    public final ConfigurationSecurities ConfigSec = new ConfigurationSecurities();

    public Wifi() {
    }

    public static String convertToQuotedString(String string) {
        if (TextUtil.isEmpty(string)) {
            return "";
        }

        final int lastPos = string.length() - 1;
        if (lastPos > 0 && (string.charAt(0) == '"' && string.charAt(lastPos) == '"')) {
            return string;
        }

        return "\"" + string + "\"";
    }

    public boolean connectToConfiguredNetwork(final WifiManager wifiMgr, WifiConfiguration config, boolean reassociate) {
        final String security = ConfigSec.getWifiConfigurationSecurity(config);
        if (config == null) return false;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            int oldPri = config.priority;
            // Make it the highest priority.
            int newPri = getMaxPriority(wifiMgr) + 100;
            if (newPri >= MAX_PRIORITY) {
                newPri = shiftPriorityAndSave(wifiMgr);
                config = getWifiConfiguration(wifiMgr, config, security);
                if (config == null) {
                    return false;
                }
            }

            // Set highest priority to this configured network
            config.priority = newPri;
            int networkId = wifiMgr.updateNetwork(config);
            if (networkId == -1) {
                return false;
            }

            // Do not disable others
            if (!wifiMgr.enableNetwork(networkId, false)) {
                config.priority = oldPri;
                return false;
            }

            if (!wifiMgr.saveConfiguration()) {
                config.priority = oldPri;
                return false;
            }
        }

        // We have to retrieve the WifiConfiguration after save.
        config = getWifiConfiguration(wifiMgr, config, security);
        if (config == null) {
            return false;
        }

        // Diconnect from current network, enable target network and connect to it
        boolean reconnectResult = false;
        boolean disconnectResult = wifiMgr.disconnect();
        if (disconnectResult) {
            boolean enableResult = wifiMgr.enableNetwork(config.networkId, true);
            if (enableResult) {
                if (reassociate) {
                    reconnectResult = wifiMgr.reassociate();
                } else {
                    reconnectResult = wifiMgr.reconnect();
                }
            }
        }

        return reconnectResult;
    }

    private void sortByPriority(final List<WifiConfiguration> configurations) {
        java.util.Collections.sort(configurations, new Comparator<WifiConfiguration>() {

            @Override
            public int compare(WifiConfiguration object1,
                               WifiConfiguration object2) {
                return object1.priority - object2.priority;
            }
        });
    }

    private int shiftPriorityAndSave(final WifiManager wifiMgr) {
        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        sortByPriority(configurations);
        final int size = configurations.size();
        for (int i = 0; i < size; i++) {
            final WifiConfiguration config = configurations.get(i);
            config.priority = i;
            wifiMgr.updateNetwork(config);
        }
        wifiMgr.saveConfiguration();
        return size;
    }

    private int getMaxPriority(final WifiManager wifiManager) {
        final List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        int pri = 0;
        for (final WifiConfiguration config : configurations) {
            if (config.priority > pri) {
                pri = config.priority;
            }
        }
        return pri;
    }

    private WifiConfiguration getWifiConfiguration(
            final WifiManager wifiMgr, final ScanResult hotspot, String hotspotSecurity) {
        final String ssid = convertToQuotedString(hotspot.SSID);
        if (ssid.length() == 0) {
            return null;
        }

        final String bssid = hotspot.BSSID;
        if (bssid == null) {
            return null;
        }

        if (hotspotSecurity == null) {
            hotspotSecurity = ConfigSec.getScanResultSecurity(hotspot);
        }

        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();
        if (configurations == null) {
            return null;
        }

        for (final WifiConfiguration config : configurations) {
            if (config.SSID == null || !ssid.equals(config.SSID)) {
                continue;
            }
            if (config.BSSID == null || !bssid.equals(config.BSSID)) {
                final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
                if (hotspotSecurity.equals(configSecurity)) {
                    return config;
                }
            }
        }
        return null;
    }

    public WifiConfiguration getWifiConfiguration(
            final WifiManager wifiMgr, final WifiConfiguration configToFind, String security) {
        final String ssid = configToFind.SSID;
        if (ssid.length() == 0) {
            return null;
        }

        final String bssid = configToFind.BSSID;

        if (security == null) {
            security = ConfigSec.getWifiConfigurationSecurity(configToFind);
        }

        final List<WifiConfiguration> configurations = wifiMgr.getConfiguredNetworks();

        for (final WifiConfiguration config : configurations) {
            if (config.SSID == null || !ssid.equals(config.SSID)) {
                continue;
            }
            if (config.BSSID == null || bssid == null || bssid.equals(config.BSSID)) {
                final String configSecurity = ConfigSec.getWifiConfigurationSecurity(config);
                if (security.equals(configSecurity)) {
                    return config;
                }
            }
        }
        return null;
    }

    public void connectToConfiguredNetwork(@NonNull final WifiManager wifiManager, @NonNull final ScanResult accessPoint) {
        // Fix for WEP networks
        wifiManager.startScan();

        final String security = ConfigSec.getScanResultSecurity(accessPoint);
        final WifiConfiguration config = getWifiConfiguration(wifiManager, accessPoint, security);

        connectToConfiguredNetwork(wifiManager, config, false);
    }
}
