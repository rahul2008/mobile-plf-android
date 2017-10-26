/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.common.util.Tagger;
import com.philips.cdp2.ews.communication.events.DiscoverApplianceEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.ScreenFlowController;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.settingdeviceinfo.ConnectWithPasswordFragment;
import com.philips.cdp2.ews.view.EWSWiFiPairedFragment;
import com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

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

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Tagger.class, EWSDependencyProvider.class})
public class TroubleshootWrongWiFiViewModelTest {

    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private EventBus eventBusMock;
    @Mock
    private ScreenFlowController screenFlowControllerMock;
    @Mock
    private EWSDependencyProvider ewsDependencyProviderMock;

    private TroubleshootWrongWiFiViewModel viewModel;


    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(Tagger.class);
        PowerMockito.mockStatic(EWSDependencyProvider.class);

        when(EWSDependencyProvider.getInstance()).thenReturn(ewsDependencyProviderMock);
        when(ewsDependencyProviderMock.getProductName()).thenReturn("product leet");

        viewModel = new TroubleshootWrongWiFiViewModel(screenFlowControllerMock, eventBusMock, wifiUtilMock);
    }

    @Test
    public void shouldCallWifiUtilOnGetHomeWifi() {
        viewModel.getHomeWifi();

        verify(wifiUtilMock).getHomeWiFiSSD();
    }

    @Test
    public void shouldCheckThatTaggerHasActionSendDataAndRightAttributes() {
        ArgumentCaptor<HashMap> mapArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        viewModel.tagWrongWifi();

        PowerMockito.verifyStatic();
        Tagger.trackAction(eq(Tag.ACTION.SEND_DATA), mapArgumentCaptor.capture());
        HashMap map = mapArgumentCaptor.getValue();

        assertEquals(map.size(), 2);
        assertEquals(EWSDependencyProvider.getInstance().getProductName(), map.get(Tag.KEY.CONNECTED_PRODUCT_NAME));
        assertEquals(Tag.VALUE.WRONG_WIFI, map.get(Tag.KEY.USER_ERROR));
    }

    @Test
    public void shouldCallEventBusUnRegisteronUnregister() {
        viewModel.unregister();

        verify(eventBusMock).unregister(viewModel);
    }

    @Test
    public void shouldShowPairingSuccessOnEventReceived() {
        viewModel.showPairingSuccessEvent(new PairingSuccessEvent());

        verify(screenFlowControllerMock).showFragment(any(EWSWiFiPairedFragment.class));
    }

    @Test
    public void shouldLaunchWifiConnectFragmentWhenApplianceDiscoveredAndFragmentVisible() {
        viewModel.start();
        viewModel.showWifiConnectionScreen(new DiscoverApplianceEvent());

        verify(screenFlowControllerMock).showFragment(any(ConnectWithPasswordFragment.class));
    }

    @Test
    public void shouldLaunchWifiConnectFragmentWhenApplianceDiscoveredAndFragmentInVisible() {
        viewModel.stop();
        viewModel.showWifiConnectionScreen(new DiscoverApplianceEvent());

        verify(screenFlowControllerMock, Mockito.never()).showFragment(any(ConnectWithPasswordFragment.class));
    }

    @Test
    public void shouldLaunchRouterTroubleShootingPageWhenAsked() {
        viewModel.launchRouterTroubleshooting();

        verify(screenFlowControllerMock).showFragment(any(TroubleshootCheckRouterSettingsFragment.class));
    }
}