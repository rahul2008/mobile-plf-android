package com.philips.platform.appframework.connectivity.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;

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
    @Test
    public void checkCanCreateApplianceForNode_True(){
        when(networkNode.getModelName())
                .thenReturn(BleReferenceAppliance.MODELNAME);
        assertTrue(new BleReferenceApplianceFactory(bleTransportContext).canCreateApplianceForNode(networkNode));

    }

    @Test
    public void createApplianceForNode_For_Not_Null(){
        when(networkNode.getModelName())
                .thenReturn(BleReferenceAppliance.MODELNAME);
        assertNotNull(new BleReferenceApplianceFactory(bleTransportContext).createApplianceForNode(networkNode));
    }

    @Test
    public void createApplianceForNode_For_Null(){
        when(networkNode.getModelName())
                .thenReturn("");
        assertNull(new BleReferenceApplianceFactory(bleTransportContext).createApplianceForNode(networkNode));
    }

    @Test
    public void getSupportedModelNames_Contains_Ble_Model_Name(){
        assertTrue(new BleReferenceApplianceFactory(bleTransportContext).getSupportedModelNames().contains(BleReferenceAppliance.MODELNAME));
    }
}
