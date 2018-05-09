/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.appliance;

import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager.ApplianceListener;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy.DiscoveryListener;
import com.philips.cdp2.commlib.core.store.ApplianceDatabase;
import com.philips.cdp2.commlib.core.store.NetworkNodeDatabase;
import com.philips.cdp2.commlib.core.util.Availability.AvailabilityListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
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
    private ApplianceFactory applianceFactoryMock;

    @Mock
    private Handler handlerMock;

    @Mock
    private DiscoveryStrategy strategyMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private Appliance applianceMock;

    @Mock
    private NetworkNodeDatabase networkNodeDatabaseMock;

    @Mock
    private ApplianceListener applianceListenerMock;

    @Mock
    private ApplianceDatabase applianceDatabaseMock;

    private AvailabilityListener<Appliance> applianceMockAvailabilityListener;
    private Map<DiscoveryStrategy, Set<DiscoveryListener>> discovery = new ConcurrentHashMap<>();
    private ApplianceManager managerUnderTest;
    private PropertyChangeListener networkNodeChangeListener;

    private void createDiscoveryStrategies() {
        discovery.put(mock(DiscoveryStrategy.class), new HashSet<DiscoveryListener>());
        discovery.put(mock(DiscoveryStrategy.class), new HashSet<DiscoveryListener>());
        discovery.put(mock(DiscoveryStrategy.class), new HashSet<DiscoveryListener>());
    }

    private void setupDiscoveryListeners() {
        for (final Map.Entry<DiscoveryStrategy, Set<DiscoveryListener>> entry : discovery.entrySet()) {
            doAnswer(new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    entry.getValue().add((DiscoveryListener) invocation.getArgument(0));
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
        disableLogging();

        when(networkNodeMock.getCppId()).thenReturn(CPPID);
        when(applianceFactoryMock.canCreateApplianceForNode(networkNodeMock)).thenReturn(true);
        when(applianceFactoryMock.createApplianceForNode(networkNodeMock)).thenReturn(applianceMock);
        when(applianceMock.getNetworkNode()).thenReturn(networkNodeMock);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable r = invocation.getArgument(0);
                r.run();
                return null;
            }
        }).when(handlerMock).post(isA(Runnable.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                applianceMockAvailabilityListener = invocation.getArgument(0);
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

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                networkNodeChangeListener = invocation.getArgument(0);
                return null;
            }
        }).when(networkNodeMock).addPropertyChangeListener(isA(PropertyChangeListener.class));

        createDiscoveryStrategies();
        setupDiscoveryListeners();

        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock, networkNodeDatabaseMock, applianceDatabaseMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreatedWithoutStrategies_thenErrorIsThrown() {
        Set<DiscoveryStrategy> discoveryStrategies = new HashSet<>();
        managerUnderTest = new ApplianceManager(discoveryStrategies, applianceFactoryMock, networkNodeDatabaseMock, applianceDatabaseMock);
    }

    private DiscoveryListener firstDiscoveryListener() {
        return discovery.values().iterator().next().iterator().next();
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceIsCreated() {

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceFactoryMock).createApplianceForNode(networkNodeMock);
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceListenerIsCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceListenerMock).onApplianceFound(applianceMock);
    }

    @Test
    public void whenApplianceListenerIsRemovedAndStrategyDiscoveringNode_thenApplianceListenerIsNotCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);
        managerUnderTest.removeApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceListenerMock, never()).onApplianceFound(applianceMock);
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceIsInSetOfAvailableAppliances() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        assertThat(managerUnderTest.getAvailableAppliances()).contains(applianceMock);
    }

    @Test
    public void whenStrategyDiscoversNode_thenApplianceCanBeFoundUsingCppId() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        assertThat(managerUnderTest.findApplianceByCppId(CPPID)).isSameAs(applianceMock);
    }

    @Test
    public void whenStrategyLosesNode_thenApplianceListenerIsCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        verify(applianceListenerMock).onApplianceLost(applianceMock);
    }

    @Test
    public void whenStrategyLosesNode_thenApplianceIsNoLongerInSetOfAvailableAppliances() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        assertThat(managerUnderTest.getAvailableAppliances()).doesNotContain(applianceMock);
    }

    @Test
    public void whenStrategyLosesNode_thenApplianceCanStillBeFoundUsingCppId() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        assertThat(managerUnderTest.findApplianceByCppId(CPPID)).isNotNull();
    }

    @Test
    public void whenStrategyLosesNodeButApplianceIsStillAvailable_thenApplianceListenerIsNotCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);
        when(applianceMock.isAvailable()).thenReturn(true);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeLost(networkNodeMock);

        verify(applianceListenerMock, never()).onApplianceLost(applianceMock);
    }

    @Test
    public void givenNodeDiscovered_whenStrategyFindsNodeAgain_thenNodesAreMerged() {
        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        NetworkNode foundNode = mock(NetworkNode.class);
        when(foundNode.getCppId()).thenReturn(CPPID);
        firstDiscoveryListener().onNetworkNodeDiscovered(foundNode);

        verify(networkNodeMock).updateWithValuesFrom(foundNode);
    }

    @Test
    public void givenNodeDiscovered_whenStrategyFindsNodeAgain_thenApplianceListenerIsCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);
        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        NetworkNode foundNode = mock(NetworkNode.class);
        when(foundNode.getCppId()).thenReturn(CPPID);
        firstDiscoveryListener().onNetworkNodeDiscovered(foundNode);

        verify(applianceListenerMock).onApplianceUpdated(applianceMock);
    }

    @Test
    public void whenOtherStrategyFindsAppliance_thenApplianceListenerIsCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);
        NetworkNode updatedNetworkNode = mock(NetworkNode.class);
        when(updatedNetworkNode.getCppId()).thenReturn(CPPID);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        firstDiscoveryListener().onNetworkNodeDiscovered(updatedNetworkNode);

        verify(applianceListenerMock).onApplianceUpdated(applianceMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalse_thenApplianceListenerCalled() {
        managerUnderTest.addApplianceListener(applianceListenerMock);

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);

        verify(applianceListenerMock).onApplianceLost(applianceMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalseAndIsDiscoveredAgain_thenNoNewApplianceIsCreated() {

        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);
        applianceMockAvailabilityListener.onAvailabilityChanged(applianceMock);
        firstDiscoveryListener().onNetworkNodeDiscovered(networkNodeMock);

        verify(applianceFactoryMock, times(1)).createApplianceForNode(networkNodeMock);
    }

    @Test
    public void whenApplianceAvailabilityChangesToFalseAndIsDiscoveredAgain_thenNotificationsAreSent() {
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

    @Test
    public void whenCreated_thenLoadsAppliancesFromDB() {

        verify(networkNodeDatabaseMock).getAll();
    }

    @Test
    public void whenLoadingFromDB_thenCreatesAppliance() {
        final List<NetworkNode> nodeList = new CopyOnWriteArrayList<>();
        nodeList.add(networkNodeMock);
        when(networkNodeDatabaseMock.getAll()).thenReturn(nodeList);

        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock, networkNodeDatabaseMock, applianceDatabaseMock);

        assertThat(managerUnderTest.getAvailableAppliances()).isNotEmpty();
    }

    @Test
    public void whenStoringAppliance_thenStoresNode() {

        managerUnderTest.storeAppliance(applianceMock);

        verify(networkNodeDatabaseMock).save(networkNodeMock);
    }

    @Test
    public void whenStoringAppliance_thenStoresAppliance() {

        managerUnderTest.storeAppliance(applianceMock);

        verify(applianceDatabaseMock).save(applianceMock);
    }

    @Test
    public void whenForgettingAppliance_thenDeletesNode() {

        managerUnderTest.forgetStoredAppliance(applianceMock);

        verify(networkNodeDatabaseMock).delete(networkNodeMock);
    }

    @Test
    public void whenForgettingStoredAppliance_thenDeletesAppliance() {
        when(networkNodeDatabaseMock.delete(networkNodeMock)).thenReturn(1);

        managerUnderTest.forgetStoredAppliance(applianceMock);

        verify(applianceDatabaseMock).delete(applianceMock);
    }

    @Test
    public void whenForgettingUnStoredAppliance_thenDoesntDeletesAppliance() {
        when(networkNodeDatabaseMock.delete(networkNodeMock)).thenReturn(1);

        managerUnderTest.forgetStoredAppliance(applianceMock);

        verify(applianceDatabaseMock).delete(applianceMock);
    }

    @Test
    public void whenNetworkNodeChanges_thenNetworkNodeIsStored() {
        final List<NetworkNode> nodeList = new CopyOnWriteArrayList<>();
        nodeList.add(networkNodeMock);
        when(networkNodeDatabaseMock.getAll()).thenReturn(nodeList);
        managerUnderTest = new ApplianceManager(discovery.keySet(), applianceFactoryMock, networkNodeDatabaseMock, applianceDatabaseMock);

        networkNodeChangeListener.propertyChange(null);

        verify(networkNodeDatabaseMock).save(networkNodeMock);
    }
}
