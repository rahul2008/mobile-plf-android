package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import junit.framework.Assert;

import org.junit.Before;
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

    DeviceMeasurementPort deviceMeasurementPort;
    @Before
    public void setUp(){
        deviceMeasurementPort=new DeviceMeasurementPort(communicationStrategy);
    }

    @Test
    public void getDicommPortName(){
        Assert.assertEquals("devicemeasurement",deviceMeasurementPort.getDICommPortName());
    }

    @Test
    public void getDicommProductID(){
        Assert.assertEquals(1,deviceMeasurementPort.getDICommProductId());
    }

    @Test
    public void supportSubcriptions_Returns_False(){
        Assert.assertFalse(deviceMeasurementPort.supportsSubscription());
    }
}
