package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Handler;

import com.philips.cdp.dicommclient.port.common.WifiPortProperties;
import com.philips.cdp2.ews.hotspotconnection.ConnectingWithDeviceViewModel.ConnectingPhoneToHotSpotCallback;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.settingdeviceinfo.DeviceFriendlyNameFetcher;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

//Todo : required to rename it
public class ConnectingPhoneToHotspotWifiViewModelTest {

    @InjectMocks private ConnectingWithDeviceViewModel subject;

    @Mock private WiFiConnectivityManager mockWiFiConnectivityManager;
    @Mock private WiFiUtil mockWiFiUtil;
    @Mock private Navigator mockNavigator;
    @Mock private Handler mockHandler;
    @Mock private ConnectingPhoneToHotSpotCallback mockFragmentCallback;
    @Mock private DeviceFriendlyNameFetcher mockDeviceFriendlyNameFetcher;
    @Mock private Context mockContext;
    @Mock private Intent mockIntent;
    @Mock private NetworkInfo mockNetworkInfo;
    @Mock private WifiPortProperties mockWifiPortProperties;

    @Captor private ArgumentCaptor<BroadcastReceiver> receiverArgumentCaptor;
    @Captor private ArgumentCaptor<DeviceFriendlyNameFetcher.Callback> fetchDevicePortProperties;
    @Captor private ArgumentCaptor<Runnable> timeoutArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject.setFragmentCallback(mockFragmentCallback);
    }

    @Test
    public void itShouldRegisterBroadcastReceiverWhenConnectionToHotspot() throws Exception {
        subject.connectToHotSpot();

        verify(mockFragmentCallback)
                .registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void itShouldNotRegisterBroadcastReceiverWhenConnectionToHotspotWhenCallbackIsNull()
            throws Exception {
        subject.setFragmentCallback(null);

        subject.connectToHotSpot();

        verify(mockFragmentCallback, never())
                .registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void itShouldAddTimeoutRunnableWhenConnectingToHotspot() throws Exception {
        subject.connectToHotSpot();

        verify(mockHandler).postDelayed(any(Runnable.class), eq(TimeUnit.SECONDS.toMillis(30)));
    }

    @Test
    public void itShouldConnectToApplianceHotspotNetworkWhenConnectionToHotspot() throws Exception {
        subject.connectToHotSpot();

        verify(mockWiFiConnectivityManager).connectToApplianceHotspotNetwork("PHILIPS Setup");
    }

    @Test
    public void itShouldFetchDeviceFriendlyNameWhenConnectedToCorrectHotspot() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED,
                WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockDeviceFriendlyNameFetcher).fetchFriendlyName();
    }

    @Test
    public void itShouldNotFetchDeviceInfoWhenNetworkNotConnected() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.DISCONNECTED,
                WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockDeviceFriendlyNameFetcher, never())
                .fetchFriendlyName();
    }

    @Test
    public void itShouldNotFetchDeviceInfoWhenNetworkIsConnectedButToWrongWifiNetwork() throws
            Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.HOME_WIFI);

        verify(mockDeviceFriendlyNameFetcher, never())
                .fetchFriendlyName();
    }

    @Test
    public void itShouldUnregisterReceiverAndSetCallbackToNullWhenCleared() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED,
                WiFiUtil.DEVICE_HOTSPOT_WIFI);

        subject.clear();

        verify(mockFragmentCallback, atLeastOnce())
                .unregisterReceiver(any(BroadcastReceiver.class));
        assertNull(subject.getFragmentCallback());
    }

    @Test
    public void itShouldUnregisterReceiverWhenConnectedToCorrectHotSpot() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED,
                WiFiUtil.DEVICE_HOTSPOT_WIFI);

        verify(mockFragmentCallback).unregisterReceiver(any(BroadcastReceiver.class));
    }

    @Test
    public void itShouldNavigateToConnectToDeviceWithPasswordScreenWhenFetchingIsSuccessful() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);
        fetchFriendlyName();
        fetchDevicePortProperties.getValue().onFriendlyNameFetchingSuccess(anyString());

        verify(mockNavigator).navigateToConnectToDeviceWithPasswordScreen(anyString());
    }

    @Test
    public void itShouldNotNavigateToConnectToDeviceWithPasswordScreenWhenFetchingIsNotSuccessful() throws Exception {
        simulateConnectionToDeviceHotspot(NetworkInfo.State.CONNECTED, WiFiUtil.DEVICE_HOTSPOT_WIFI);
        fetchFriendlyName();
        fetchDevicePortProperties.getValue().onFriendlyNameFetchingFailed();

        verify(mockFragmentCallback).showTroubleshootHomeWifiDialog();
        verify(mockNavigator, never()).navigateToConnectToDeviceWithPasswordScreen(anyString());
    }

    @Test
    public void itShouldNavigateToUnsuccessfulDialogWhenTimesOut() throws Exception {
        subject.connectToHotSpot();
        verify(mockHandler).postDelayed(timeoutArgumentCaptor.capture(), anyInt());
        timeoutArgumentCaptor.getValue().run();

        verify(mockFragmentCallback).showTroubleshootHomeWifiDialog();
    }

    @Test
    public void itShouldNavigateBackWhenCancelIsPressed() throws Exception {
        subject.handleCancelButtonClicked();

        verify(mockNavigator).navigateBack();
    }

    private void fetchFriendlyName() {
        verify(mockDeviceFriendlyNameFetcher).setNameFetcherCallback(fetchDevicePortProperties.capture());
        verify(mockDeviceFriendlyNameFetcher).fetchFriendlyName();
    }

    private void mockNetworkChange(NetworkInfo.State networkState, @WiFiUtil.WiFiState int wifiState) {
        when(mockIntent.getParcelableExtra(anyString())).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.getState()).thenReturn(networkState);
        when(mockWiFiUtil.getCurrentWifiState()).thenReturn(wifiState);
    }

    private void simulateConnectionToDeviceHotspot(NetworkInfo.State networkState,
                                                   @WiFiUtil.WiFiState int wifiState) {
        mockNetworkChange(networkState, wifiState);
        subject.connectToHotSpot();
        verify(mockFragmentCallback)
                .registerReceiver(receiverArgumentCaptor.capture(), any(IntentFilter.class));
        receiverArgumentCaptor.getValue().onReceive(mockContext, mockIntent);
    }
}