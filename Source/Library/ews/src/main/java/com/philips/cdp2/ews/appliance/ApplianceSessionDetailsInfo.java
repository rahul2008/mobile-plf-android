/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.appliance;

import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.port.common.WifiPortProperties;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings({"WeakerAccess"})
@Singleton
public class ApplianceSessionDetailsInfo {

    private DevicePortProperties devicePortProperties;
    private WifiPortProperties wifiPortProperties;

    @Inject
    public ApplianceSessionDetailsInfo() {
    }

    public DevicePortProperties getDevicePortProperties() {
        return devicePortProperties;
    }

    public void setDevicePortProperties(DevicePortProperties devicePortProperties) {
        this.devicePortProperties = devicePortProperties;
    }

    public WifiPortProperties getWifiPortProperties() {
        return wifiPortProperties;
    }

    public void setWifiPortProperties(WifiPortProperties wifiPortProperties) {
        this.wifiPortProperties = wifiPortProperties;
    }

    public String getDeviceName() {
        return "Wakeup Light";
    }

    public boolean hasSessionProperties() {
        // TODO :: Currently not fetching device port properties, add the condition once its fixed
        return wifiPortProperties != null;
    }

    public String getCppId() {
        if(wifiPortProperties == null) {
            throw new IllegalStateException("Trying to get cpp id, without wifi properties being set");
        }
        return this.wifiPortProperties.getCppid();
    }
}
