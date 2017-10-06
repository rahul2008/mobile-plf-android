/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import com.philips.cdp.dicommclient.discovery.DiscoveryEventListener;
import com.philips.cdp.dicommclient.discovery.DiscoveryManager;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ConnectApplianceToHomeWiFiEvent;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.FetchDevicePortPropertiesEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSDependencyProvider.class, EWSLogger.class, EWSTagger.class})
public class ApplianceAccessEventMonitorTest {

    @Mock
    private ApplianceAccessManager applianceAccessManagerMock;

    private ApplianceAccessEventMonitor applianceMonitor;
    @Mock
    private DiscoveryManager discoverManagerMock;
    @Mock
    private ApplianceSessionDetailsInfo sessionInfoMock;
    @Mock
    private EventBus eventBusMock;
    @Mock
    private EWSDependencyProvider ewsDependencyProviderMock;

    private static String CPP = "cc1ad323";
    private static String DEVICE_MODEL = "deviceModel";
    private static String DEVICE_NAME = "deviceName";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        PowerMockito.mockStatic(EWSTagger.class);
        applianceMonitor = new ApplianceAccessEventMonitor(applianceAccessManagerMock, eventBusMock, sessionInfoMock,
                discoverManagerMock);
    }

    @Test
    public void shouldFetchApplianceDevicePortDetailsWhenRequested() throws Exception {
        applianceMonitor.fetchDevicePortProperties(new FetchDevicePortPropertiesEvent());

        verify(applianceAccessManagerMock).fetchDevicePortProperties(isNull(ApplianceAccessManager.FetchCallback.class));
    }

    @Test
    public void shouldConnectApplianceToHomeWiFiEventWhenRequested() throws Exception {
        ArgumentCaptor<String> ssidCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> pwdCaptor = ArgumentCaptor.forClass(String.class);

        String homeWiFiSSID = "BrightEyes2.4";
        String homeWiFiPassword = "BrightEyes123";
        final ConnectApplianceToHomeWiFiEvent event = new ConnectApplianceToHomeWiFiEvent(homeWiFiSSID, homeWiFiPassword);

        applianceMonitor.connectApplianceToHomeWiFiEvent(event);

        verify(applianceAccessManagerMock).connectApplianceToHomeWiFiEvent(ssidCaptor.capture(), pwdCaptor.capture());
        assertEquals(homeWiFiSSID, ssidCaptor.getValue());
        assertEquals(homeWiFiPassword, pwdCaptor.getValue());
    }

    @Test
    public void shouldStartDiscoverApplianceWhenRequested() throws Exception {
        applianceMonitor.discoverAppliance(new DiscoverApplianceEvent());

        verify(discoverManagerMock).start();
    }

    @Test
    public void shouldStopDiscoverWhenOnStopIsCalled() throws Exception {
        applianceMonitor.onStop();
        verify(discoverManagerMock).stop();
    }

    @Test
    public void shouldCheckForApplianceInCallbackResultAndSendSuccessEvent() throws Exception {
        final ArgumentCaptor<DiscoveryEventListener> listenerArgumentCaptor = ArgumentCaptor.forClass(DiscoveryEventListener.class);

        simulateDeviceFoundWithConnectionState(ConnectionState.CONNECTED_LOCALLY);

        applianceMonitor.discoverAppliance(new DiscoverApplianceEvent());
        verify(discoverManagerMock).addDiscoveryEventListener(listenerArgumentCaptor.capture());

        listenerArgumentCaptor.getValue().onDiscoveredAppliancesListChanged();

        verify(eventBusMock).post(isA(PairingSuccessEvent.class));
    }

    @Test
    public void shouldCheckForApplianceInCallbackAndNoEventIfNotConnectedState() throws Exception {
        final ArgumentCaptor<DiscoveryEventListener> listenerArgumentCaptor = ArgumentCaptor.forClass(DiscoveryEventListener.class);

        simulateDeviceFoundWithConnectionState(ConnectionState.DISCONNECTED);

        applianceMonitor.discoverAppliance(new DiscoverApplianceEvent());
        verify(discoverManagerMock).addDiscoveryEventListener(listenerArgumentCaptor.capture());

        listenerArgumentCaptor.getValue().onDiscoveredAppliancesListChanged();

        verify(eventBusMock, Mockito.never()).post(isA(PairingSuccessEvent.class));
    }

    @Test
    public void shouldSendProductConnectionTagsOnSuccess() throws Exception {
        final ArgumentCaptor<DiscoveryEventListener> listenerArgumentCaptor = ArgumentCaptor.forClass(DiscoveryEventListener.class);

        simulateDeviceFoundWithConnectionState(ConnectionState.CONNECTED_LOCALLY);

        applianceMonitor.discoverAppliance(new DiscoverApplianceEvent());
        verify(discoverManagerMock).addDiscoveryEventListener(listenerArgumentCaptor.capture());

        listenerArgumentCaptor.getValue().onDiscoveredAppliancesListChanged();

        PowerMockito.verifyStatic();
        HashMap<String, String> tagVals = new HashMap<>();
        tagVals.put(Tag.KEY.MACHINE_ID, CPP);
        tagVals.put(Tag.KEY.PRODUCT_MODEL, DEVICE_MODEL);
        tagVals.put(Tag.KEY.PRODUCT_NAME, DEVICE_NAME);

        EWSTagger.trackAction(Tag.ACTION.CONNECTION_SUCCESS, tagVals);
    }

    @Test
    public void shouldSendStopTimedActionOnSuccess() throws Exception {
        final ArgumentCaptor<DiscoveryEventListener> listenerArgumentCaptor = ArgumentCaptor.forClass(DiscoveryEventListener.class);

        simulateDeviceFoundWithConnectionState(ConnectionState.CONNECTED_LOCALLY);

        applianceMonitor.discoverAppliance(new DiscoverApplianceEvent());
        verify(discoverManagerMock).addDiscoveryEventListener(listenerArgumentCaptor.capture());

        listenerArgumentCaptor.getValue().onDiscoveredAppliancesListChanged();

        PowerMockito.verifyStatic();

        EWSTagger.stopTimedAction(Tag.ACTION.TIME_TO_CONNECT);
    }

    @Test
    public void shouldNotSendProductConnectionTagsOnDeviceNotConnectedReceived() throws Exception {
        final ArgumentCaptor<DiscoveryEventListener> listenerArgumentCaptor = ArgumentCaptor.forClass(DiscoveryEventListener.class);

        simulateDeviceFoundWithConnectionState(ConnectionState.DISCONNECTED);

        applianceMonitor.discoverAppliance(new DiscoverApplianceEvent());
        verify(discoverManagerMock).addDiscoveryEventListener(listenerArgumentCaptor.capture());

        listenerArgumentCaptor.getValue().onDiscoveredAppliancesListChanged();

        PowerMockito.verifyStatic(never());

        HashMap<String, String> tagVals = new HashMap<>();
        tagVals.put(Tag.KEY.MACHINE_ID, CPP);
        tagVals.put(Tag.KEY.PRODUCT_MODEL, DEVICE_MODEL);
        tagVals.put(Tag.KEY.PRODUCT_NAME, DEVICE_NAME);

        EWSTagger.trackAction(Tag.ACTION.CONNECTION_SUCCESS, tagVals);
        EWSTagger.stopTimedAction(Tag.ACTION.TIME_TO_CONNECT);
    }

    private void simulateDeviceFoundWithConnectionState(ConnectionState connectionState) {
        final Appliance applianceMock = mock(Appliance.class);
        final NetworkNode networkNodeMock = mock(NetworkNode.class);

        PowerMockito.mockStatic(EWSDependencyProvider.class);
        final EWSDependencyProvider dependencyProviderMock = mock(EWSDependencyProvider.class);
        when(EWSDependencyProvider.getInstance()).thenReturn(dependencyProviderMock);
        when(dependencyProviderMock.getLoggerInterface()).thenReturn(mock(LoggingInterface.class));
        when(dependencyProviderMock.getProductName()).thenReturn(DEVICE_NAME);

        when(discoverManagerMock.getApplianceByCppId(CPP)).thenReturn(applianceMock);
        when(applianceMock.getNetworkNode()).thenReturn(networkNodeMock);
        when(networkNodeMock.getConnectionState()).thenReturn(connectionState);
        when(sessionInfoMock.getCppId()).thenReturn(CPP);
        when(networkNodeMock.getDeviceType()).thenReturn(DEVICE_MODEL);
    }
}