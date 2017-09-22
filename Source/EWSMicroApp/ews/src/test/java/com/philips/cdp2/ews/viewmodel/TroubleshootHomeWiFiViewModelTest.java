/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.communication.events.FoundHomeNetworkEvent;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.view.EWSHomeWifiDisplayFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleshootHomeWiFiViewModelTest {

    @Mock
    FoundHomeNetworkEvent foundHomeNetworkEventMock;

    @Mock
    private EventBus eventBusMock;

    @Mock
    private ScreenFlowController screenFlowControllerMock;

    @Mock
    private WiFiUtil wifiUtilMock;

    private TroubleshootHomeWiFiViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new TroubleshootHomeWiFiViewModel(screenFlowControllerMock, eventBusMock, wifiUtilMock);
    }

    @Test
    public void shouldSubscribeWithEventBusToReceiveHomeNetworkWiFiChangeOnCreate() throws Exception {
        verify(eventBusMock).register(viewModel);
    }

    @Test
    public void shouldShowHomeWiFiScreenWhenFoundHomeNetworkEventIsReceived() throws Exception {
        sendFoundHomeNetworkEvent();

        verify(screenFlowControllerMock).showFragment(isA(EWSHomeWifiDisplayFragment.class));
    }

    @Test
    public void shouldUnregisterWithEventBusWhenFoundHomeNetworkEventIsReceived() throws Exception {
        viewModel.unregister();

        verify(eventBusMock).unregister(viewModel);
    }

    @Test
    public void shouldRemoveCurrentFragmentFromFragmentBackStackBeforeShowingNextScreen() throws Exception {
        sendFoundHomeNetworkEvent();

        verify(screenFlowControllerMock).popBackStack();
    }

    @Test
    public void shouldShowHomeWiFiScreenIfNetworkIsFoundWhenAsked() throws Exception {
        sendFoundHomeNetworkEvent();
        when(wifiUtilMock.isHomeWiFiEnabled()).thenReturn(true);

        viewModel.checkHomeWiFiNetwork();

        verify(screenFlowControllerMock, atLeastOnce()).showFragment(isA(EWSHomeWifiDisplayFragment.class));
    }

    @Test
    public void shouldNotShowHomeWiFiScreenIfNetworkIsNotFound() throws Exception {
        viewModel.checkHomeWiFiNetwork();

        verify(screenFlowControllerMock, never()).showFragment(isA(EWSHomeWifiDisplayFragment.class));
    }

    private void sendFoundHomeNetworkEvent() {
        viewModel.onHomeNetworkFound(foundHomeNetworkEventMock);
    }
}