/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class BEApplianceTest {

    private BEAppliance subject;

    @Mock
    NetworkNode mockNetworkNode;

    @Mock
    CommunicationStrategy mockCommunicationStrategy;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new BEAppliance(mockNetworkNode, mockCommunicationStrategy);
    }

    @Test
    public void getDeviceType() throws Exception {
        subject.getDeviceType();
        assertEquals("Wake-up Light", subject.getDeviceType());
    }

}