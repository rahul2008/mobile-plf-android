/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.context;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp2.commlib.lan.context.LanTransportContext.clearStoredCertificateFor;
import static junit.framework.Assert.assertNull;

public class LanTransportContextTest extends RobolectricTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();

    }

    @Test
    public void whenClearingStoredCertificateForAppliance_thenTheNetworkNodePinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");

        Appliance appliance = new Appliance(networkNode, communicationStrategyMock) {
            @Override
            public String getDeviceType() {
                return "TEST";
            }
        };

        clearStoredCertificateFor(appliance);
        assertNull(appliance.getNetworkNode().getPin());
    }
}
