/*
 * (C) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.lan;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.lan.NetworkMonitor.NetworkState;
import com.philips.cdp2.commlib.util.UnthreadExecutor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;

import static android.net.wifi.SupplicantState.DISCONNECTED;
import static com.philips.cdp2.commlib.lan.NetworkMonitor.NetworkState.MOBILE;
import static com.philips.cdp2.commlib.lan.NetworkMonitor.NetworkState.NONE;
import static com.philips.cdp2.commlib.lan.NetworkMonitor.NetworkState.WIFI_WITH_INTERNET;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkMonitorTest {

    private NetworkMonitor monitorUnderTest;

    @Mock
    Context mockContext;

    @Mock
    IntentFilter mockIntentFilter;

    @Mock
    ConnectivityManager mockConnectivityManager;

    @Mock
    WifiManager mockWifiManager;

    @Mock
    Intent mockIntent;

    @Mock
    NetworkMonitor.NetworkChangedListener mockNetworkChangedListener;

    @Captor
    ArgumentCaptor<BroadcastReceiver> broadcastReceiverArgumentCaptor;

    @Spy
    UnthreadExecutor executor = new UnthreadExecutor();

    @Mock
    NetworkInfo mockNetworkInfo;

    @Mock
    WifiInfo mockWifiInfo;

    private static final String TEST_SSID = "TEST_SSID";

    @SuppressLint("WifiManagerPotentialLeak")
    @Before
    public void setup() {
        initMocks(this);
        DICommLog.disableLogging();

        when(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager);
        when(mockContext.getSystemService(Context.WIFI_SERVICE)).thenReturn(mockWifiManager);
        when(mockContext.getApplicationContext()).thenReturn(mockContext);

        when(mockIntent.getAction()).thenReturn("TEST_ACTION");

        monitorUnderTest = new NetworkMonitor(mockContext, executor) {
            @NonNull
            @Override
            protected IntentFilter createIntentFilter() {
                return mockIntentFilter;
            }
        };

        monitorUnderTest.addListener(mockNetworkChangedListener);
    }

    @Test
    public void registersReceiver() {
        monitorUnderTest.startNetworkChangedReceiver();

        verify(mockContext).registerReceiver(any(BroadcastReceiver.class), any(IntentFilter.class));
    }

    @Test
    public void unregisterReceiver() {
        monitorUnderTest.startNetworkChangedReceiver();
        monitorUnderTest.stopNetworkChangedReceiver();

        verify(mockContext).unregisterReceiver(any(BroadcastReceiver.class));
    }

    @Test
    public void unregisterReceiverWithError() {
        monitorUnderTest.startNetworkChangedReceiver();
        doThrow(new IllegalArgumentException()).when(mockContext).unregisterReceiver(any(BroadcastReceiver.class));

        monitorUnderTest.stopNetworkChangedReceiver();
    }

    @Test
    public void networkChangeWifi() {
        whenConnectedWifi(TEST_SSID);

        monitorUnderTest.startNetworkChangedReceiver();

        verify(mockNetworkChangedListener).onNetworkChanged(WIFI_WITH_INTERNET, TEST_SSID);
    }

    @Test
    public void networkChangeMobile() {
        whenConnectedMobile();

        monitorUnderTest.startNetworkChangedReceiver();

        verify(mockNetworkChangedListener).onNetworkChanged(MOBILE, null);
    }

    @Test
    public void networkChangeNewSSID() {
        InOrder inOrder = inOrder(mockNetworkChangedListener);
        whenConnectedWifi(TEST_SSID);
        whenMonitorStarted();

        whenConnectedWifi(null);
        notifyNetworkMonitorStateChanged();

        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(WIFI_WITH_INTERNET, TEST_SSID);
        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(NONE, null);
        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(WIFI_WITH_INTERNET, null);
    }

    @Test
    public void networkChangeMobileToWifi() {
        InOrder inOrder = inOrder(mockNetworkChangedListener);
        whenConnectedMobile();
        whenMonitorStarted();

        whenConnectedWifi(TEST_SSID);
        notifyNetworkMonitorStateChanged();

        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(MOBILE, null);
        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(NONE, null);
        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(WIFI_WITH_INTERNET, TEST_SSID);
    }

    @Test
    public void networkChangeWifiToMobile() {
        InOrder inOrder = inOrder(mockNetworkChangedListener);
        whenConnectedWifi(TEST_SSID);
        whenMonitorStarted();

        whenConnectedMobile();
        notifyNetworkMonitorStateChanged();

        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(WIFI_WITH_INTERNET, TEST_SSID);
        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(NONE, null);
        inOrder.verify(mockNetworkChangedListener).onNetworkChanged(MOBILE, null);
    }

    @Test
    public void getSsidNone() {
        final String lastKnownNetworkSsid = monitorUnderTest.getLastKnownNetworkSsid();

        assertNull(lastKnownNetworkSsid);
    }

    @Test
    public void getSsidTest() {
        whenConnectedWifi(TEST_SSID);
        whenMonitorStarted();

        final String lastKnownNetworkSsid = monitorUnderTest.getLastKnownNetworkSsid();

        assertEquals(TEST_SSID, lastKnownNetworkSsid);
    }

    @Test
    public void getStateNone() {
        final NetworkState lastKnownNetworkState = monitorUnderTest.getLastKnownNetworkState();

        assertEquals(NONE, lastKnownNetworkState);
    }

    @Test
    public void getStateMobile() {
        whenConnectedMobile();
        whenMonitorStarted();

        final NetworkState lastKnownNetworkState = monitorUnderTest.getLastKnownNetworkState();

        assertEquals(MOBILE, lastKnownNetworkState);
    }

    private void notifyNetworkMonitorStateChanged() {
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, mockIntent);
    }

    private void whenConnected(final int connectionType) {
        when(mockConnectivityManager.getActiveNetworkInfo()).thenReturn(mockNetworkInfo);
        when(mockWifiManager.getConnectionInfo()).thenReturn(mockWifiInfo);
        when(mockNetworkInfo.isConnectedOrConnecting()).thenReturn(true);
        when(mockNetworkInfo.getType()).thenReturn(connectionType);
    }

    private void whenConnectedMobile() {
        whenConnected(ConnectivityManager.TYPE_MOBILE);
        when(mockWifiInfo.getSupplicantState()).thenReturn(DISCONNECTED);
    }

    private void whenConnectedWifi(String ssid) {
        whenConnected(ConnectivityManager.TYPE_WIFI);
        when(mockWifiInfo.getSSID()).thenReturn(ssid);
    }

    private void whenMonitorStarted() {
        monitorUnderTest.startNetworkChangedReceiver();
        verify(mockContext).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
    }
}
