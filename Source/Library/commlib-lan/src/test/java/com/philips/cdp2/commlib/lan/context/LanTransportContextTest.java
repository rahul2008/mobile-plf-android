/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.context;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashSet;
import java.util.Set;

import static android.net.ConnectivityManager.TYPE_WIFI;
import static android.net.ConnectivityManager.TYPE_WIMAX;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.acceptNewPinFor;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.acceptPinFor;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.findAppliancesWithMismatchedPinIn;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.readPin;
import static com.philips.cdp2.commlib.lan.context.LanTransportContext.rejectNewPinFor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanTransportContextTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private Handler handlerMock;

    @Mock
    private Context contextMock;

    @Mock
    ConnectivityManager connectivityManagerMock;

    @Mock
    private NetworkInfo activeNetworkInfoMock;

    @Mock
    private DiscoveryStrategy lanDiscoveryStrategyMock;

    @Mock
    RuntimeConfiguration runtimeConfigurationMock;

    private LanTransportContext lanTransportContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        HandlerProvider.enableMockedHandler(handlerMock);
        DICommLog.disableLogging();

        when(runtimeConfigurationMock.getContext()).thenReturn(contextMock);
        when(contextMock.getApplicationContext()).thenReturn(contextMock);
    }

    @Test
    public void whenRejectingNewPinForAppliance_thenThePinShouldRemainUntouched_andTheMismatchedPinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");
        networkNode.setMismatchedPin("ABCDEF");

        Appliance appliance = createTestAppliance(networkNode);
        rejectNewPinFor(appliance);

        assertEquals("1234567890", appliance.getNetworkNode().getPin());
        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @Test
    public void whenAcceptingNewPinForAppliance_thenThePinShouldBeTheNewPin_andTheMismatchedPinShouldBeNull() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");
        networkNode.setMismatchedPin("ABCDEF");

        Appliance appliance = createTestAppliance(networkNode);

        acceptNewPinFor(appliance);

        assertEquals("ABCDEF", appliance.getNetworkNode().getPin());
        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @Test
    public void whenAcceptingAPinForAppliance_thenThePinShouldBeTheNewPin_andTheMismatchedPinShouldBeNull() {
        final String newPin = "1234567890";

        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("9876543210");
        networkNode.setMismatchedPin("ABCDEF");

        Appliance appliance = createTestAppliance(networkNode);

        acceptPinFor(appliance, newPin);

        assertEquals(newPin, appliance.getNetworkNode().getPin());
        assertNull(appliance.getNetworkNode().getMismatchedPin());
    }

    @Test
    public void whenReadingThePinFromAnAppliance_thenThePinOfItsNetworkNodeShouldBeReturned() {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setPin("1234567890");

        Appliance appliance = createTestAppliance(networkNode);

        assertEquals("1234567890", readPin(appliance));
    }

    @Test
    public void whenFindingAppliancesWithMismatchedPinInEmptySet_ThenEmptySetIsReturned() {
        Set result = findAppliancesWithMismatchedPinIn(new HashSet());

        assertTrue(result.isEmpty());
    }

    @Test
    public void whenFindingApplianceWithMismatchedPinInSetOfAppliancesWithNoMismatch_ThenEmptySetIsReturned() {
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
    public void whenFindingApplianceWithMismatchedPinInSetOfAppliancesWithMismatch_ThenSetHasOneAppliance() {
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
    public void whenFindingApplianceWithMismatchedPinInSetOfAppliancesWithMismatchAndWithoutMismatch_ThenSetHasOneAppliance() {
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

    @Test
    public void whenConnectedToWifi_thenAvailableIsTrue() {
        doAnswer(new Answer<Intent>() {
            @Override
            public Intent answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, BroadcastReceiver.class).onReceive(contextMock, null);
                return null;
            }
        }).when(contextMock).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
        when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);
        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(activeNetworkInfoMock);
        when(activeNetworkInfoMock.getType()).thenReturn(TYPE_WIFI);
        when(activeNetworkInfoMock.isConnected()).thenReturn(true);

        lanTransportContext = new LanTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            DiscoveryStrategy createLanDiscoveryStrategy() {
                return lanDiscoveryStrategyMock;
            }
        };

        assertThat(lanTransportContext.isAvailable()).isTrue();
    }

    @Test
    public void whenConnectedToOther_thenAvailableIsFalse() {
        doAnswer(new Answer<Intent>() {
            @Override
            public Intent answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, BroadcastReceiver.class).onReceive(contextMock, null);
                return null;
            }
        }).when(contextMock).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
        when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);
        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(activeNetworkInfoMock);
        when(activeNetworkInfoMock.getType()).thenReturn(TYPE_WIMAX);
        when(activeNetworkInfoMock.isConnected()).thenReturn(true);

        lanTransportContext = new LanTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            DiscoveryStrategy createLanDiscoveryStrategy() {
                return lanDiscoveryStrategyMock;
            }
        };

        assertThat(lanTransportContext.isAvailable()).isFalse();
    }

    @Test
    public void whenNotConnectedToWifi_thenAvailableIsFalse() {
        doAnswer(new Answer<Intent>() {
            @Override
            public Intent answer(InvocationOnMock invocation) throws Throwable {
                invocation.getArgumentAt(0, BroadcastReceiver.class).onReceive(contextMock, null);
                return null;
            }
        }).when(contextMock).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
        when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManagerMock);
        when(connectivityManagerMock.getActiveNetworkInfo()).thenReturn(activeNetworkInfoMock);
        when(activeNetworkInfoMock.getType()).thenReturn(TYPE_WIFI);
        when(activeNetworkInfoMock.isConnected()).thenReturn(false);

        lanTransportContext = new LanTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            DiscoveryStrategy createLanDiscoveryStrategy() {
                return lanDiscoveryStrategyMock;
            }
        };

        assertThat(lanTransportContext.isAvailable()).isFalse();
    }

    @Test
    public void whenCreatingTransportContext_thenDiscoveryStrategyIsCreated() {

        lanTransportContext = new LanTransportContext(runtimeConfigurationMock) {
            @NonNull
            @Override
            DiscoveryStrategy createLanDiscoveryStrategy() {
                return lanDiscoveryStrategyMock;
            }
        };

        assertThat(lanTransportContext.getDiscoveryStrategy()).isNotNull();
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
