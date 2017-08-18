/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.philips.cdp2.commlib.core.util.HandlerProvider.enableMockedHandler;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplianceManagerTest {

    public static final String CPPID = "CPPID";
    @Mock
    private ApplianceFactory<Appliance> applianceFactoryMock;

    @Mock
    private Handler handlerMock;

    @Mock
    private DiscoveryStrategy strategyMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private Appliance applianceMock;

    @Mock
    private ApplianceManager.ApplianceListener<Appliance> applianceListenerMock;

    private AvailabilityListener<Appliance> applianceMockAvailabilityListener;
    private Map<DiscoveryStrategy, Set<DiscoveryListener>> discovery = new ConcurrentHashMap<>();
    private ApplianceManager managerUnderTest;

    private void createDisoveryStrategies() {
        discovery.put(mock(DiscoveryStrategy.class), new HashSet<DiscoveryListener>());
        discovery.put(mock(DiscoveryStrategy.class), new HashSet<DiscoveryListener>());
        discovery.put(mock(DiscoveryStrategy.class), new HashSet<DiscoveryListener>());
    }

    private void setupDiscoveryListeners() {
        for (final Map.Entry<DiscoveryStrategy, Set<DiscoveryListener>> entry : discovery.entrySet()) {
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    entry.getValue().add(invocation.getArgumentAt(0, DiscoveryListener.class));
                    return null;
                }
            }).when(entry.getKey()).addDiscoveryListener(isA(DiscoveryListener.class));
        }
    }

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        initMocks(this);
        enableMockedHandler(handlerMock);

        when(networkNodeMock.getCppId()).thenReturn(CPPID);
        when(applianceFactoryMock.canCreateApplianceForNode(networkNodeMock)).thenReturn(true);
        when(applianceFactoryMock.createApplianceForNode(networkNodeMock)).thenReturn(applianceMock);
        when(applianceMock.getNetworkNode()).thenReturn(networkNodeMock);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, Runnable.class).run();
                return null;
            }
        }).when(handlerMock).post(isA(Runnable.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                applianceMockAvailabilityListener = invocation.getArgumentAt(0, AvailabilityListener.class);
                return null;
            }
        }).when(applianceMock).addAvailabilityListener(isA(AvailabilityListener.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                applianceMockAvailabilityListener = null;
                return null;
            }
        }).when(applianceMock).removeAvailabilityListener(isA(AvailabilityListener.class));

        createDisoveryStrategies();
        setupDiscoveryListeners();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreatedWithoutStrategies_thenErrorIsThrown() {
        Set<DiscoveryStrategy> discoveryStrategies = new HashSet<>();
        managerUnderTest = new ApplianceManager(discoveryStrategies, applianceFactoryMock);
    }


    @Test
    public void whenCreatedWithStrategies_thenNoErrorIsThrown() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
    }

    private DiscoveryListener firstDiscoveryListener() {
        return discovery.values().iterator().next().iterator().next();
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceIsCreated() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceFactoryMock).createApplianceForNode(networkNodeMock);
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceListenerIsCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceListenerMock).onApplianceFound(applianceMock);
    }

    @Test
    public void whenApplianceListenerIsRemovedAndStrategyDiscoveringNode_thenApplianceListenerIsNotCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);
        managerUnderTest.removeApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceListenerMock, never()).onApplianceFound(applianceMock);
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceIsInSetOfAvailableAppliances() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        assertThat(managerUnderTest.getAvailableAppliances()).contains(applianceMock);
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceCanBeFoundUsingCppId() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        assertThat(managerUnderTest.findApplianceByCppId(CPPID)).isSameAs(applianceMock);
    }

    @Test
    public void whenStrategyLosesNode_thenApplianceListenerIsCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        verify(applianceListenerMock).onApplianceLost(applianceMock);
    }

    @Test
    public void whenStrategyLosesNode_thenApplianceIsNoLongerInSetOfAvailableAppliances() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        assertThat(managerUnderTest.getAvailableAppliances()).doesNotContain(applianceMock);
    }

    @Test
    public void whenStrategyLosesNode_thenApplianceCanNoLongerBeFoundUsingCppId() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        assertThat(managerUnderTest.findApplianceByCppId(CPPID)).isNull();
    }

    @Test
    public void whenStrategyLosesNodeButApplianceIsStillAvailable_thenApplianceListenerIsNotCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);
        when(applianceMock.isAvailable()).thenReturn(true);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        verify(applianceListenerMock, never()).onApplianceLost(applianceMock);
    }

    @Test
    public void whenStrategyUpdatesExistingNode_thenNodesAreMerged() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        NetworkNode updatedNetworkNode = mock(NetworkNode.class);
        when(updatedNetworkNode.getCppId()).thenReturn(CPPID);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeUpdated(updatedNetworkNode);

        verify(networkNodeMock).updateWithValuesFrom(updatedNetworkNode);
    }

    @Test
    public void whenStrategyUpdatesExistingNode_thenApplianceListenerIsCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);
        NetworkNode updatedNetworkNode = mock(NetworkNode.class);
        when(updatedNetworkNode.getCppId()).thenReturn(CPPID);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeUpdated(updatedNetworkNode);

        verify(applianceListenerMock).onApplianceUpdated(applianceMock);
    }

    @Test
    public void whenOtherStrategyFindsAppliance_thenApplianceListenerIsCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);
        NetworkNode updatedNetworkNode = mock(NetworkNode.class);
        when(updatedNetworkNode.getCppId()).thenReturn(CPPID);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeDiscovered(updatedNetworkNode);

        verify(applianceListenerMock).onApplianceUpdated(applianceMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalse_thenApplianceListenerCalled() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);

        verify(applianceListenerMock).onApplianceLost(applianceMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalseAndIsDiscoveredAgain_thenNoNewApplianceIsCreated() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);
        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceFactoryMock, times(1)).createApplianceForNode(networkNodeMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalseAndIsDiscoveredAgain_thenNotificationsAreSent() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);
        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        final InOrder inOrder = inOrder(applianceListenerMock);
        inOrder.verify(applianceListenerMock).onApplianceFound(applianceMock);
        inOrder.verify(applianceListenerMock).onApplianceLost(applianceMock);
        inOrder.verify(applianceListenerMock).onApplianceFound(applianceMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalseAndTrueAgain_thenNotificationsAreSent() {
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock);
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);
        when(applianceMock.isAvailable()).thenReturn(true);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);

        final InOrder inOrder = inOrder(applianceListenerMock);
        inOrder.verify(applianceListenerMock).onApplianceFound(applianceMock);
        inOrder.verify(applianceListenerMock).onApplianceLost(applianceMock);
        inOrder.verify(applianceListenerMock).onApplianceFound(applianceMock);
    }


}