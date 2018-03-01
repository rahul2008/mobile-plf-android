package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class RefAppBleReferenceApplianceTest {

    @Mock
    private NetworkNode networkNode;

    @Mock
    private CommunicationStrategy communicationStrategy;

    @Test
    public void getDeviceType_ReturnsTrue(){
        assertEquals("ReferenceNode", new RefAppBleReferenceAppliance(networkNode,communicationStrategy).getDeviceType());
    }

    @Test
    public void getDeviceMeasurement_NotNull(){
        assertNotNull(new RefAppBleReferenceAppliance(networkNode,communicationStrategy).getDeviceMeasurementPort());
    }
}
