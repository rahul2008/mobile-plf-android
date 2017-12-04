/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.communication.events;

import com.philips.cdp2.ews.annotations.NetworkType;
import com.philips.cdp2.ews.communication.wifi.NetworkConnectEvent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NetworkConnectEventTest {

    private NetworkConnectEvent subject;
    @NetworkType private int networkType = NetworkType.HOME_WIFI;
    private String networkSSID = "networkSSID";


    @Before
    public void setUp() throws Exception {
        subject = new NetworkConnectEvent(networkType, networkSSID);
    }

    @Test
    public void getNetworkType() throws Exception {
        assertEquals(subject.getNetworkType(), networkType);
    }

    @Test
    public void getNetworkSSID() throws Exception {
        assertEquals(subject.getNetworkSSID(), networkSSID);
    }

}