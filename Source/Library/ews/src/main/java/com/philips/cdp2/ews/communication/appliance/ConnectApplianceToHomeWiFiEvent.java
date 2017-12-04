/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication.appliance;

import android.support.annotation.Keep;

@Keep
public class ConnectApplianceToHomeWiFiEvent {
    private final String homeWiFiSSID;
    private final String homeWiFiPassword;

    public ConnectApplianceToHomeWiFiEvent(final String homeWiFiSSID, final String homeWiFiPassword) {
        this.homeWiFiSSID = homeWiFiSSID;
        this.homeWiFiPassword = homeWiFiPassword;
    }

    public String getHomeWiFiPassword() {
        return homeWiFiPassword;
    }

    public String getHomeWiFiSSID() {
        return homeWiFiSSID;
    }
}