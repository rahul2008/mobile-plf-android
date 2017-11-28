/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.communication;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ConnectApplianceToHomeWiFiEvent;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.platform.appinfra.AppInfraInterface;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSDependencyProvider.class, EWSLogger.class, EWSTagger.class})
public class ApplianceAccessEventMonitorTest {

    private ApplianceAccessEventMonitor subject;

    @Mock private ApplianceAccessManager applianceAccessManagerMock;
    @Mock private DiscoveryHelper discoverManagerMock;
    @Mock private ApplianceSessionDetailsInfo sessionInfoMock;
    @Mock private EventBus eventBusMock;
    @Mock private Appliance applianceMock;

    private static String CPP = "cc1ad323";
    private static String DEVICE_MODEL = "deviceModel";
    private static String DEVICE_NAME = "deviceFriendlyName";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSLogger.class);
        PowerMockito.mockStatic(EWSTagger.class);

        AppInfraInterface mockAppInfraInterface = mock(AppInfraInterface.class);
        Map<String, String> mockMap = new HashMap<>();
        mockMap.put(EWSInterface.PRODUCT_NAME, DEVICE_NAME);
        EWSDependencyProvider.getInstance().initDependencies(mockAppInfraInterface, mockMap);

        when(sessionInfoMock.getCppId()).thenReturn(CPP);

        NetworkNode mockNetworkNode = mock(NetworkNode.class);
        when(mockNetworkNode.toString()).thenReturn("I am a network node!");
        when(mockNetworkNode.getDeviceType()).thenReturn(DEVICE_MODEL);
        when(applianceMock.getNetworkNode()).thenReturn(mockNetworkNode);

        subject = new ApplianceAccessEventMonitor(applianceAccessManagerMock, eventBusMock, sessionInfoMock,
                discoverManagerMock);
    }

    @Test
    public void itShouldConnectApplianceToHomeWiFiEventWhenRequested() throws Exception {
        ArgumentCaptor<String> ssidCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> pwdCaptor = ArgumentCaptor.forClass(String.class);

        String homeWiFiSSID = "BrightEyes2.4";
        String homeWiFiPassword = "BrightEyes123";
        final ConnectApplianceToHomeWiFiEvent event = new ConnectApplianceToHomeWiFiEvent(homeWiFiSSID, homeWiFiPassword);

        subject.connectApplianceToHomeWiFiEvent(event);

        verify(applianceAccessManagerMock).connectApplianceToHomeWiFiEvent(ssidCaptor.capture(), pwdCaptor.capture(),
                isNull(ApplianceAccessManager.SetPropertiesCallback.class));
        assertEquals(homeWiFiSSID, ssidCaptor.getValue());
        assertEquals(homeWiFiPassword, pwdCaptor.getValue());
    }

    @Test
    public void itShouldStartDiscoverApplianceWhenRequested() throws Exception {
        subject.discoverAppliance(new DiscoverApplianceEvent());

        verify(discoverManagerMock).startDiscovery(any(DiscoveryHelper.DiscoveryCallback.class));
    }

    @Test
    public void itShouldStopDiscoverWhenOnStopIsCalled() throws Exception {
        subject.onStop();
        verify(discoverManagerMock).stopDiscovery();
    }

    @Test
    public void itShouldCheckForApplianceInCallbackResultAndSendSuccessEvent() throws Exception {
        subject.onApplianceFound(applianceMock);

        verify(eventBusMock).post(isA(PairingSuccessEvent.class));
    }

    @Test
    public void itShouldSendProductConnectionTagsOnSuccess() throws Exception {
        subject.onApplianceFound(applianceMock);

        PowerMockito.verifyStatic();
        HashMap<String, String> tagVals = new HashMap<>();
        tagVals.put(Tag.KEY.MACHINE_ID, CPP);
        tagVals.put(Tag.KEY.PRODUCT_MODEL, DEVICE_MODEL);
        tagVals.put(Tag.KEY.PRODUCT_NAME, DEVICE_NAME);

        EWSTagger.trackAction(Tag.ACTION.CONNECTION_SUCCESS, tagVals);
    }

    @Test
    public void itShouldSendStopTimedActionOnSuccess() throws Exception {
        subject.onApplianceFound(applianceMock);

        PowerMockito.verifyStatic();
        EWSTagger.stopTimedAction(Tag.ACTION.TIME_TO_CONNECT);
    }
}