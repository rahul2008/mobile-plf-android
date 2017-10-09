package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager;
import com.philips.cdp2.ews.appliance.ApplianceAccessManager.FetchCallback;
import com.philips.cdp2.ews.hotspotconnection.ConnectingPhoneToHotspotWifiViewModel
        .ConnectingPhoneToHotSpotCallback;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectingPhoneToHotspotWifiViewModelTest {

    @InjectMocks private ConnectingPhoneToHotspotWifiViewModel subject;

    @Mock private WiFiConnectivityManager mockWiFiConnectivityManager;
    @Mock private ApplianceAccessManager mockApplianceAccessManager;
    @Mock private WiFiUtil mockWiFiUtil;
    @Mock private Navigator mockNavigator;
    @Mock private ConnectingPhoneToHotSpotCallback mockFragmentCallback;

    @Mock private Context mockContext;
    @Mock private Intent mockIntent;
    @Mock private NetworkInfo mockNetworkInfo;
    @Mock private WifiPortProperties mockWifiPortProperties;

    @Captor private ArgumentCaptor<BroadcastReceiver> receiverArgumentCaptor;
    @Captor private ArgumentCaptor<FetchCallback> fetchCallbackArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject.setFragmentCallback(mockFragmentCallback);
    }

    @Test
    public void itShouldRegisterBroadcastReceiverWhenConnectionToHotspot() throws Exception {
        subject.connectToHotSpot();

        verify(mockFragmentCallback).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void itShouldNotRegisterBroadcastReceiverWhenConnectionToHotspotWhenCallbackIsNull() throws Exception {
        subject.setFragmentCallback(null);

        subject.connectToHotSpot();

        verify(mockFragmentCallback, never()).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void itShouldConnectToApplianceHotspotNetworkWhenConnectionToHotspot() throws Exception {
        subject.connectToHotSpot();

        verify(mockWiFiConnectivityManager).connectToApplianceHotspotNetwork("PHILIPS Setup");
    }

    @Test
    public void itShouldFetchDeviceInfoWhenConnectedToCorrectHotspot() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockApplianceAccessManager).fetchDevicePortProperties(any(FetchCallback.class));
    }

    @Test
    public void itShouldNotFetchDeviceInfoWhenNetworkNotConnected() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.DISCONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockApplianceAccessManager, never()).fetchDevicePortProperties(any(FetchCallback.class));
    }

    @Test
    public void itShouldNotFetchDeviceInfoWhenNetworkIsConnectedButToWrongWifiNetwork() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.HOME_WIFI);

        verify(mockApplianceAccessManager, never()).fetchDevicePortProperties(any(FetchCallback.class));
    }

    @Test
    public void itShouldUnregisterReceiverAndSetCallbackToNullWhenCleared() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);

        subject.clear();

        verify(mockFragmentCallback).unregisterReceiver(any(BroadcastReceiver.class));
        assertNull(subject.getFragmentCallback());
    }

    @Test
    public void itShouldNavigateToConnectToDeviceWithPasswordScreenWhenFetchingIsSuccessful() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockApplianceAccessManager).fetchDevicePortProperties(fetchCallbackArgumentCaptor.capture());
        fetchCallbackArgumentCaptor.getValue().onDeviceInfoReceived(mockWifiPortProperties);

        verify(mockNavigator).navigateToConnectToDeviceWithPasswordScreen();
    }

    @Test
    public void itShouldNotNavigateToConnectToDeviceWithPasswordScreenWhenFetchingIsNotSuccessful() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockApplianceAccessManager).fetchDevicePortProperties(fetchCallbackArgumentCaptor.capture());
        fetchCallbackArgumentCaptor.getValue().onFailedToFetchDeviceInfo();

        // TODO
        verify(mockNavigator, never()).navigateToConnectToDeviceWithPasswordScreen();
    }

    private void mockNetworkChange(NetworkInfo.State networkState, @WiFiUtil.WiFiState int wifiState) {
        when(mockIntent.getParcelableExtra(anyString())).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.getState()).thenReturn(networkState);
        when(mockWiFiUtil.getCurrentWifiState()).thenReturn(wifiState);
    }

    private void simulateConnectionToDeviceHotspot(NetworkInfo.State networkState, @WiFiUtil.WiFiState int wifiState) {
        mockNetworkChange(networkState, wifiState);
        subject.connectToHotSpot();
        verify(mockFragmentCallback).registerReceiver(receiverArgumentCaptor.capture(), any(IntentFilter.class));
        receiverArgumentCaptor.getValue().onReceive(mockContext, mockIntent);
    }
}