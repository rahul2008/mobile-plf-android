/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.communication.events;

import com.philips.cdp2.ews.communication.appliance.ConnectApplianceToHomeWiFiEvent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConnectApplianceToHomeWiFiEventTest {

    private ConnectApplianceToHomeWiFiEvent subject;
    private String homeWifiSSID = "guest";
    private String password = "password";

    @Before
    public void setUp() throws Exception {
        subject = new ConnectApplianceToHomeWiFiEvent(homeWifiSSID, password);
    }

    @Test
    public void itShouldVerifyHomeWifiPasswordIsEqual() throws Exception {
        assertEquals(subject.getHomeWiFiPassword(), password);
    }

    @Test
    public void itShouldVerifyHomeWifiSSIDIsEqual() throws Exception {
        assertEquals(subject.getHomeWiFiSSID(), homeWifiSSID);
    }

}