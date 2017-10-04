package com.philips.cdp2.ews.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
public class EWSGenericApplianceTest {

    private EWSGenericAppliance appliance;

    @Before
    public void setUp() throws Exception {
        appliance = new EWSGenericAppliance(mock(NetworkNode.class), mock(CommunicationStrategy.class));
    }

    @Before
    public void shouldCheckApplianceIsNotNull() throws Exception {
        assertNotNull(appliance);
    }

    @Test
    public void shouldCheckDeviceTypeIsNullForTempAppliance() throws Exception {
        assertNull(appliance.getDeviceType());
    }
}