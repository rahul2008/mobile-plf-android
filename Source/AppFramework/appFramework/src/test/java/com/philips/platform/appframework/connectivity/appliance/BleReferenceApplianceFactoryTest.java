package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 16/01/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class BleReferenceApplianceFactoryTest {

    @Mock
    BleTransportContext bleTransportContext;

    @Mock
    NetworkNode networkNode;

    @Mock
    NetworkNode networkNodeModelNameNull;

    @Mock
    CommunicationStrategy communicationStrategy;

    BleReferenceApplianceFactory bleReferenceApplianceFactory;

    @Before
    public void setUp(){
        bleReferenceApplianceFactory=new BleReferenceApplianceFactory(bleTransportContext);
        when(networkNode.getDeviceType())
                .thenReturn(BleReferenceAppliance.MODELNAME);
        when(bleTransportContext.createCommunicationStrategyFor(networkNode)).thenReturn(communicationStrategy);
    }
    @Test
    public void checkCanCreateApplianceForNode_True(){
        assertTrue(bleReferenceApplianceFactory.canCreateApplianceForNode(networkNode));

    }

    @Test
    public void createApplianceForNode_For_Not_Null(){
        assertNotNull(bleReferenceApplianceFactory.createApplianceForNode(networkNode));
    }

    @Test
    public void createApplianceForNode_For_Null(){
        when(networkNodeModelNameNull.getDeviceType())
                .thenReturn("");
        assertNull(bleReferenceApplianceFactory.createApplianceForNode(networkNodeModelNameNull));
    }

    @Test
    public void getSupportedModelNames_Contains_Ble_Model_Name(){
        assertTrue(bleReferenceApplianceFactory.getSupportedModelNames().contains(BleReferenceAppliance.MODELNAME));
    }
}
