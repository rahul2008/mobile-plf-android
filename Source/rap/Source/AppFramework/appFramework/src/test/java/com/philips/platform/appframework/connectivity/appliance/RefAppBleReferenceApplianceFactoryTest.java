package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.cloud.context.CloudTransportContext;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.demouapp.RefAppApplianceFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class RefAppBleReferenceApplianceFactoryTest {

    @Mock
    private BleTransportContext bleTransportContext;

    @Mock
    private CloudTransportContext cloudTransportContext;

    @Mock
    private LanTransportContext lanTransportContext;

    @Mock
    private NetworkNode networkNode;

    @Mock
    private NetworkNode networkNodeModelNameNull;

    @Mock
    private CommunicationStrategy communicationStrategy;

    private RefAppApplianceFactory bleReferenceApplianceFactory;

    @Before
    public void setUp(){
        bleReferenceApplianceFactory = new RefAppApplianceFactory(bleTransportContext, lanTransportContext, cloudTransportContext);
        when(networkNode.getDeviceType()).thenReturn(BleReferenceAppliance.DEVICETYPE);
        when(bleTransportContext.createCommunicationStrategyFor(networkNode)).thenReturn(communicationStrategy);
        when(networkNode.isValid()).thenReturn(true);
    }

    @Test
    public void checkCanCreateApplianceForNode_True(){
        assertTrue(bleReferenceApplianceFactory.canCreateApplianceForNode(networkNode));

    }

    @Test
    public void createApplianceForNode_For_Not_Null(){
        when(cloudTransportContext.createCommunicationStrategyFor(networkNode)).thenReturn(communicationStrategy);
        when(lanTransportContext.createCommunicationStrategyFor(networkNode)).thenReturn(communicationStrategy);
        when(networkNode.getModelId()).thenReturn("PS1234");
        when(networkNode.getName()).thenReturn("DiCommBLEReference");
        when(networkNode.isValid()).thenReturn(true);
        when(networkNode.getDeviceType()).thenReturn(BleReferenceAppliance.DEVICETYPE);
        assertNotNull(bleReferenceApplianceFactory.createApplianceForNode(networkNode));
    }

    @Test
    public void createApplianceForNode_For_Null(){
        when(networkNodeModelNameNull.getDeviceType()).thenReturn("");
        assertNull(bleReferenceApplianceFactory.createApplianceForNode(networkNodeModelNameNull));
    }

    @Test
    public void getSupportedDeviceTypes_isEmpty(){
        assertTrue(bleReferenceApplianceFactory.getSupportedDeviceTypes().isEmpty());
    }
}
