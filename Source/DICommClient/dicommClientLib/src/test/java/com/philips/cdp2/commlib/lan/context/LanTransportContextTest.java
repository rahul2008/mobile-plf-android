/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.context;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp2.commlib.lan.context.LanTransportContext.acceptNewPinFor;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.rejectNewPinFor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class LanTransportContextTest extends RobolectricTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void whenRejectingStoredPinForAppliance_thenTheNetworkNodePinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");

        Appliance appliance = createTestAppliance(networkNode);
        rejectNewPinFor(appliance);

        assertNull(appliance.getNetworkNode().getPin());
    }

    @Test
    public void whenRejectingStoredPinForAppliance_thenTheNetworkNodeMismatchedPinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");

        Appliance appliance = createTestAppliance(networkNode);
        rejectNewPinFor(appliance);

        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @Test
    public void whenAcceptingStoredPinForAppliance_thenTheNetworkNodeMismatchedPinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");
        networkNode.setMismatchedPin("ABCDEF");

        Appliance appliance = createTestAppliance(networkNode);

        acceptNewPinFor(appliance);

        assertEquals("ABCDEF", appliance.getNetworkNode().getPin());
        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @NonNull
    private Appliance createTestAppliance(final NetworkNode networkNode) {
        return new Appliance(networkNode, communicationStrategyMock) {
            @Override
            public String getDeviceType() {
                return "TEST";
            }
        };
    }
}
