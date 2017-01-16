package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by philips on 16/01/17.
 */

public class DeviceMeasurementPortTest {
    @Mock
    NetworkNode networkNode;

    @Mock
    CommunicationStrategy communicationStrategy;

    @Test
    public void isResponseForThisPort_Returns_True(){
        Assert.assertTrue(new DeviceMeasurementPort(networkNode,communicationStrategy).isResponseForThisPort("{\"measurementvalue\":50}"));
    }

    @Test
    public void getDicommPortName(){
        Assert.assertEquals("devicemeasurement",new DeviceMeasurementPort(networkNode,communicationStrategy).getDICommPortName());
    }

    @Test
    public void getDicommProductID(){
        Assert.assertEquals(1,new DeviceMeasurementPort(networkNode,communicationStrategy).getDICommProductId());
    }

    @Test
    public void supportSubcriptions_Returns_False(){
        Assert.assertFalse(new DeviceMeasurementPort(networkNode,communicationStrategy).supportsSubscription());
    }
}
