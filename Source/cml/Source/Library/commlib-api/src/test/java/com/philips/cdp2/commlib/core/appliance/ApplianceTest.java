/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplianceTest {

    @Mock
    private NetworkNode mockNetworkNode;

    @Mock
    private CommunicationStrategy mockCommunicationStrategy;

    @Mock
    private Handler mockHandler;

    @Mock
    private DICommPort<?> mockPort;

    private Appliance applianceUnderTest;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        enableMockedHandler(mockHandler);

        applianceUnderTest = new Appliance(mockNetworkNode, mockCommunicationStrategy) {
            @Override
            public String getDeviceType() {
                return null;
            }
        };
    }

    @Test
    public void whenPortGetsAddedThenPortListContainsTheAdditionalPort() {
        int numPorts = applianceUnderTest.getAllPorts().size();

        applianceUnderTest.addPort(mockPort);

        assertEquals(numPorts + 1, applianceUnderTest.getAllPorts().size());
        assertTrue(applianceUnderTest.getAllPorts().contains(mockPort));
    }
}
