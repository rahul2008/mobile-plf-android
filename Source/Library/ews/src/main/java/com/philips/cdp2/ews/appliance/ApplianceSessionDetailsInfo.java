/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.appliance;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings({"WeakerAccess"})
@Singleton
public class ApplianceSessionDetailsInfo {

    private WifiPortProperties wifiPortProperties;

    private String appliancePin;

    @Inject
    public ApplianceSessionDetailsInfo() {
    }

    public WifiPortProperties getWifiPortProperties() {
        return wifiPortProperties;
    }

    public void setWifiPortProperties(WifiPortProperties wifiPortProperties) {
        this.wifiPortProperties = wifiPortProperties;
    }

    public boolean hasSessionProperties() {
        return wifiPortProperties != null;
    }

    public String getCppId() {
        if(wifiPortProperties == null) {
            throw new IllegalStateException("Trying to get cpp id, without wifi properties being set");
        }
        return this.wifiPortProperties.getCppid();
    }

    public String getAppliancePin() {
        return appliancePin;
    }

    public void setAppliancePin(String appliancePin) {
        this.appliancePin = appliancePin;
    }
}
