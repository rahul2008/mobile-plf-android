package com.philips.platform.ews.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BEApplianceFactoryTest {

    @InjectMocks private BEApplianceFactory subject;

    @Mock private LanTransportContext mockLanTransportContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldReturnTrueWhenDeviceIsSupported() throws Exception {
        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockNetworkNode.getDeviceType()).thenReturn(BEAppliance.DEVICE_TYPE);

        assertTrue(subject.canCreateApplianceForNode(mockNetworkNode));
    }

    @Test
    public void itShouldReturnFalseWhenDeviceIsNotSupported() throws Exception {
        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockNetworkNode.getDeviceType()).thenReturn("NotSupportedType");

        assertFalse(subject.canCreateApplianceForNode(mockNetworkNode));
    }

    @Test
    public void itShouldReturnCorrectSupprotedDeviceTypes() throws Exception {
        assertTrue(subject.getSupportedDeviceTypes().contains(BEAppliance.DEVICE_TYPE));
        assertTrue(subject.getSupportedDeviceTypes().contains(BEAppliance.PRODUCT_STUB));
        assertEquals(2, subject.getSupportedDeviceTypes().size());
    }

    @Test
    public void itShouldCreateCorrectTypeOfAppliance() throws Exception {
        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockNetworkNode.getDeviceType()).thenReturn(BEAppliance.DEVICE_TYPE);

        CommunicationStrategy mockStrategy = mock(CommunicationStrategy.class);
        when(mockLanTransportContext.createCommunicationStrategyFor(any(NetworkNode.class)))
                .thenReturn(mockStrategy);

        assertTrue(subject.createApplianceForNode(mockNetworkNode) instanceof BEAppliance);
    }

    @Test
    public void itShouldReturnNullWhenApplianceForNodeCannotBeCreated() throws Exception{
        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockNetworkNode.getDeviceType()).thenReturn("NotSupportedType");
        assertNull(subject.createApplianceForNode(mockNetworkNode));
    }
}