package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.cloud.context.CloudTransportContext;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.connectivity.demouapp.RefAppApplianceFactory;

import org.junit.After;
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
    LanTransportContext lanTransportContext;

    @Mock
    CloudTransportContext cloudTransportContext;

    @Mock
    NetworkNode networkNode;

    @Mock
    NetworkNode networkNodeModelNameNull;

    @Mock
    CommunicationStrategy communicationStrategy;

    RefAppApplianceFactory bleReferenceApplianceFactory;

    @Before
    public void setUp(){
        bleReferenceApplianceFactory=new RefAppApplianceFactory(bleTransportContext, lanTransportContext, cloudTransportContext);
        bleReferenceApplianceFactory.setDeviceType(ConnectivityDeviceType.REFERENCE_NODE);
        when(networkNode.getDeviceType())
                .thenReturn(RefAppBleReferenceAppliance.MODELNAME);
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
    public void getSupportedDeviceTypes_Contains_Ble_Model_Name(){
        assertTrue(bleReferenceApplianceFactory.getSupportedDeviceTypes().contains(RefAppBleReferenceAppliance.MODELNAME));
    }

    @Test
    public void canCreateApplianceForNodeForPowerSleepTest() {
        bleReferenceApplianceFactory=new RefAppApplianceFactory(bleTransportContext, lanTransportContext, cloudTransportContext);
        bleReferenceApplianceFactory.setDeviceType(ConnectivityDeviceType.POWER_SLEEP);
        when(networkNode.getModelId())
                .thenReturn(RefAppBleReferenceAppliance.MODEL_NAME_HH1600);
        assertTrue(bleReferenceApplianceFactory.canCreateApplianceForNode(networkNode));
    }

    @After
    public void tearDown() {
        bleReferenceApplianceFactory = null;
        communicationStrategy = null;
        networkNodeModelNameNull = null;
        networkNode = null;
        bleTransportContext = null;
    }
}
