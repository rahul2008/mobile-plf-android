package com.philips.platform.ews.homewificonnection;


import com.philips.cdp.dicommclient.networknode.WiFiNode;
import com.philips.platform.ews.appliance.ApplianceAccessManager;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SelectWiFiViewModelTest {

    private SelectWiFiViewModel viewModel;
    @Mock
    private Navigator mockNavigator;
    @Mock
    private ApplianceAccessManager mockApplianceAccessManager;
    @Mock
    private WiFiUtil mockWiFiUtil;
    @Mock
    private SelectWiFiAdapter mockAdapter;
    @Mock
    private EWSTagger mockEWSTagger;
    @Mock
    private EWSLogger mockEWSLogger;
    @Captor
    private ArgumentCaptor<ApplianceAccessManager.FetchWiFiNetworksCallBack> fetchWiFiNetworksCaptor;
    @Mock
    private List<WiFiNode> mockWiFiNodeList;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new SelectWiFiViewModel(mockNavigator, mockApplianceAccessManager, mockWiFiUtil, mockAdapter, mockEWSTagger, mockEWSLogger);
    }

    @Test
    public void itShouldSetWifiNodeSelectListenerWhenCreated() {
        assertNotNull(viewModel.getAdapter());

        verify(mockAdapter).setOnWifiNodeSelectListener(viewModel);
    }

    @Test
    public void itShouldSendEventToApplianceAccessManagerFetchWiFiNetworksFromDeviceWhenCalled() {
        viewModel.fetchWifiNodes();

        verify(mockApplianceAccessManager).fetchWiFiNetworks(any(ApplianceAccessManager.FetchWiFiNetworksCallBack.class));
    }

    @Test
    public void itShouldShowProgressBarWhileFetchingNetworksIsInProgress() {
        viewModel.fetchWifiNodes();

        assertTrue(viewModel.isRefreshing.get());
    }

    @Test
    public void itShouldDisableContinueButtonWhileFetchingNetworksIsInProgress() {
        viewModel.fetchWifiNodes();

        assertFalse(viewModel.enableContinueButton.get());
    }

    @Test
    public void itShouldUpdateListViewWhenWiFiListIsReceived() {
        simulateWiFiListFetch();

        verify(mockAdapter).setWifiList(mockWiFiNodeList);
    }

    @Test
    public void itShouldNotShowProgressbarOnceWiFiNetworkListIsReceived() {
        simulateWiFiListFetch();

        assertFalse(viewModel.isRefreshing.get());
    }

    @Test
    public void itShouldEnableContinueButtonOnlyWhenUserSelectedSSIDFromTheList() {
        viewModel.onWifiNodeSelected("SSID");

        assertTrue(viewModel.enableContinueButton.get());
    }

    @Test
    public void itShouldUpdateWiFiUtilLastWifiSSidWithSelectedSSIDOnlyWhenContinueButtonClicked() {
        String selectedSSID = "SSID";
        viewModel.onWifiNodeSelected(selectedSSID);
        viewModel.onContinueButtonClicked();

        verify(mockWiFiUtil).setHomeWiFiSSID(selectedSSID);
    }

    @Test
    public void itShouldShowConnectToDeviceWithPasswordScreenWhenContinueButtonIsClicked() throws Exception {
        viewModel.onContinueButtonClicked();

        verify(mockNavigator).navigateToConnectToDeviceWithPasswordScreen(null);
    }

    @Test
    public void itShouldShowNetworkNotListedTroubleshootingScreenWhenNetworkNotListedButtonIsClicked() throws Exception {
        viewModel.onNetworkNotListedButtonClicked();

        verify(mockNavigator).navigateToNetworkNotListedTroubleshootingScreen();
    }

    @Test
    public void itShouldRemoveOnWifiNodeSelectListenerWhenCleanUpIsCalled() {
        viewModel.cleanUp();

        verify(mockAdapter).removeOnWifiNodeSelectListener();
    }

    @Test
    public void itShouldVerifyTrackPageName() throws Exception {
        viewModel.trackPageName();

        verify(mockEWSTagger).trackPage("selectWifiNetwork");
    }

    private void simulateWiFiListFetch() {
        viewModel.fetchWifiNodes();
        verify(mockApplianceAccessManager).fetchWiFiNetworks(fetchWiFiNetworksCaptor.capture());
        fetchWiFiNetworksCaptor.getValue().onWiFiNetworksReceived(mockWiFiNodeList);
    }
}