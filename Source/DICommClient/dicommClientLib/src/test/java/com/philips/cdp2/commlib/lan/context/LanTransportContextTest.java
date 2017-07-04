/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.context;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.Collections;

import static com.philips.cdp2.commlib.lan.context.LanTransportContext.acceptNewPinFor;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.findAppliancesWithMismatchedPinIn;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.rejectNewPinFor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

public class LanTransportContextTest extends RobolectricTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void whenRejectingStoredPinForAppliance_thenTheNetworkNodePinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");

        Appliance appliance = createTestAppliance(networkNode);
        rejectNewPinFor(appliance);

        assertNull(appliance.getNetworkNode().getPin());
    }

    @Test
    public void whenRejectingStoredPinForAppliance_thenTheNetworkNodeMismatchedPinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");

        Appliance appliance = createTestAppliance(networkNode);
        rejectNewPinFor(appliance);

        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @Test
    public void whenAcceptingStoredPinForAppliance_thenTheNetworkNodeMismatchedPinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");
        networkNode.setMismatchedPin("ABCDEF");

        Appliance appliance = createTestAppliance(networkNode);

        acceptNewPinFor(appliance);

        assertEquals("ABCDEF", appliance.getNetworkNode().getPin());
        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @Test
    public void whenFindingAppliancesWithMismatchedPinInEmptySet_ThenEmptySetIsReturned() throws Exception {
        Set result = findAppliancesWithMismatchedPinIn(Collections.emptySet());

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenFindingApplianceWithMismatchedPinInSetOfAppliancesWithNoMismatch_ThenEmptySetIsReturned() throws Exception {
        final NetworkNode networkNode = new NetworkNode();
        networkNode.setCppId("cpp");
        networkNode.setPin("1234567890");

        Set<Appliance> appliances = new HashSet<Appliance>() {{
            add(createTestAppliance(networkNode));
        }};

        Set result = findAppliancesWithMismatchedPinIn(appliances);

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenFindingApplianceWithMismatchedPinInSetOfAppliancesWithMismatch_ThenSetHasOneAppliance() throws Exception {
        final NetworkNode networkNode = new NetworkNode();
        networkNode.setCppId("cpp");
        networkNode.setPin("1234567890");
        networkNode.setMismatchedPin("0987654321");

        Set<Appliance> appliances = new HashSet<Appliance>() {{
            add(createTestAppliance(networkNode));
        }};

        Set result = findAppliancesWithMismatchedPinIn(appliances);

        assertEquals(1, result.size());
        assertEquals(result.toArray()[0], appliances.toArray()[0]);
    }

    @Test
    public void whenFindingApplianceWithMismatchedPinInSetOfAppliancesWithMismatchAndWithoutMismatch_ThenSetHasOneAppliance() throws Exception {
        final NetworkNode mismatchedNetworkNode = new NetworkNode();
        mismatchedNetworkNode.setCppId("cpp");
        mismatchedNetworkNode.setPin("1234567890");
        mismatchedNetworkNode.setMismatchedPin("0987654321");

        final NetworkNode matchingNetworkNode = new NetworkNode();
        matchingNetworkNode.setCppId("cpp2");
        matchingNetworkNode.setPin("1234567890");

        Set<Appliance> appliances = new HashSet<Appliance>() {{
            add(createTestAppliance(mismatchedNetworkNode));
            add(createTestAppliance(matchingNetworkNode));
        }};

        Set result = findAppliancesWithMismatchedPinIn(appliances);

        assertEquals(1, result.size());
        assertEquals(result.toArray()[0], createTestAppliance(mismatchedNetworkNode));
    }

    @NonNull
    private Appliance createTestAppliance(final NetworkNode networkNode) {
        return new Appliance(networkNode, communicationStrategyMock) {
            @Override
            public String getDeviceType() {
                return "TEST";
            }
        };
    }
}
