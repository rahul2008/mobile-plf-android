/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.hotspotconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.settingdeviceinfo.DeviceFriendlyNameFetcher;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.EWSActivity.DEVICE_CONNECTION_TIMEOUT;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSTagger.class, Log.class})
public class ConnectingWithDeviceViewModelTest {

    //TODO pls extend the tests in every viewModel in this PR

    private ConnectingWithDeviceViewModel subject;

    @Mock
    private WiFiConnectivityManager mockWiFiConnectivityManager;
    @Mock
    private DeviceFriendlyNameFetcher mockDeviceFriendlyNameFetcher;
    @Mock
    private WiFiUtil mockWiFiUtil;
    @Mock
    private Navigator mockNavigator;
    @Mock
    private Handler mockHandler;
    @Mock
    private ConnectingWithDeviceViewModel.ConnectingPhoneToHotSpotCallback mockFragmentCallback;
    @Mock
    private Intent mockIntent;
    @Mock
    private Context mockContext;
    @Mock
    private IntentFilter stubIntentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
    @Captor
    private ArgumentCaptor<BroadcastReceiver> broadcastReceiverArgumentCaptor;
    @Captor
    private ArgumentCaptor<Runnable> timeoutRunnableCaptor;
    @Mock
    private NetworkInfo mockNetworkInfo;
    @Mock
    private BaseContentConfiguration mockBaseContentConfiguration;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        mockStatic(Log.class);
        subject = new ConnectingWithDeviceViewModel(mockWiFiConnectivityManager, mockDeviceFriendlyNameFetcher,
                mockWiFiUtil, mockNavigator, mockHandler, mockBaseContentConfiguration);
        subject.setFragmentCallback(mockFragmentCallback);
    }


    @Test
    public void itShouldRegisterReceiverAndTryToConnectOnApplianceHotspotNetworkOnConnectToHomeSpot() {
        when(mockIntent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.getState()).thenReturn(NetworkInfo.State.CONNECTED);
        when(mockWiFiUtil.getCurrentWifiState()).thenReturn(WiFiUtil.DEVICE_HOTSPOT_WIFI);
        when(mockWiFiUtil.isHomeWiFiEnabled()).thenReturn(true);
        subject.connectToHotSpot();
        verify(mockFragmentCallback, times(1)).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        verify(mockHandler, times(1)).postDelayed(timeoutRunnableCaptor.capture(), anyLong());
        verify(mockWiFiConnectivityManager, times(1)).connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
        broadcastReceiverArgumentCaptor.getValue().onReceive(mockContext, mockIntent);
        verifyFriendlyNameFetching(1);
        verify(mockFragmentCallback, times(1)).unregisterReceiver(broadcastReceiverArgumentCaptor.capture());
    }

    @Test
    public void itShouldShowUnsuccessfulDialogOnConnectToHotSpotIfTimingOut() {
        when(mockIntent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)).thenReturn(mockNetworkInfo);
        when(mockNetworkInfo.getState()).thenReturn(NetworkInfo.State.CONNECTED);
        when(mockWiFiUtil.getCurrentWifiState()).thenReturn(WiFiUtil.DEVICE_HOTSPOT_WIFI);
        when(mockWiFiUtil.isHomeWiFiEnabled()).thenReturn(true);
        subject.connectToHotSpot();
        verify(mockFragmentCallback, times(1)).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        verify(mockHandler, times(1)).postDelayed(timeoutRunnableCaptor.capture(), anyLong());
        verify(mockWiFiConnectivityManager, times(1)).connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
        timeoutRunnableCaptor.getValue().run();
        verifyShowingUnsuccessfulDialog();
        verifyFriendlyNameFetching(0);
        verify(mockFragmentCallback, never()).unregisterReceiver(broadcastReceiverArgumentCaptor.capture());
    }

    @Test
    public void itShouldNotRegisterReceiverWhenCallbackNullAndTryToConnectOnApplianceHotspotNetworkOnConnectToHomeSpot() {
        subject.setFragmentCallback(null);
        when(mockWiFiUtil.isHomeWiFiEnabled()).thenReturn(true);
        subject.connectToHotSpot();
        verify(mockFragmentCallback, never()).registerReceiver(broadcastReceiverArgumentCaptor.capture(), any(IntentFilter.class));
        PowerMockito.verifyStatic();
        Log.e(anyString(), anyString());
        verify(mockHandler, times(1)).postDelayed(subject.timeOutAction, DEVICE_CONNECTION_TIMEOUT);
        verify(mockWiFiConnectivityManager, times(1)).connectToApplianceHotspotNetwork(WiFiUtil.DEVICE_SSID);
    }

    @Test
    public void itShouldShowUnsuccessfulDialogIfWifiIsOff() throws Exception{
        subject.connectToHotSpot();
        verify(mockFragmentCallback, times(1)).showTroubleshootHomeWifiDialog(mockBaseContentConfiguration);
    }

    @Test
    public void itShouldNavigateBackOnHandleCancelButtonClicked() {
        subject.handleCancelButtonClicked();
        verify(mockNavigator, times(1)).navigateBack();
    }

    @Test
    public void itShouldTagAndNavigateToResetConnectionTroubleShootingScreenOnHelpNeeded() {
        subject.onHelpNeeded();
        verifyStatic();
        EWSTagger.trackActionSendData(Tag.KEY.SPECIAL_EVENTS, Tag.ACTION.USER_NEEDS_HELP);
        verify(mockNavigator, times(1)).navigateToResetConnectionTroubleShootingScreen();
    }

    @Test
    public void itShouldNavigateToCompletingDeviceSetupScreenOnHelpNotNeeded() {
        subject.onHelpNotNeeded();
        verify(mockNavigator, times(1)).navigateToCompletingDeviceSetupScreen();
    }

    @Test
    public void itShouldSetFetcherCallbackAndFetchFriendlyNameOnPhoneConnectedToHotspotWifi() {
        subject.onPhoneConnectedToHotspotWifi();
        verifyFriendlyNameFetching(1);
    }

    @Test
    public void itShouldShowUnsuccessfulDialogOnConnectionAttemptTimedOut() {
        subject.onConnectionAttemptTimedOut();
        verify(mockFragmentCallback, times(1)).showTroubleshootHomeWifiDialog(mockBaseContentConfiguration);

    }

    @Test
    public void itShouldNotShowUnsuccessfulDialogOnConnectionAttemptTimedOutWhenCallbackNull() {
        subject.setFragmentCallback(null);
        subject.onConnectionAttemptTimedOut();
        verifyStatic(never());
        EWSTagger.trackPage(Page.PHONE_TO_DEVICE_CONNECTION_FAILED);
        verify(mockFragmentCallback, never()).showTroubleshootHomeWifiDialog(mockBaseContentConfiguration);
        verifyStatic();
        Log.e(anyString(), anyString());
    }

    @Test
    public void itShouldUnregisterReceiverAndSetCallackNullOnClear() {
        subject.clear();
        verify(mockFragmentCallback, times(1)).unregisterReceiver(broadcastReceiverArgumentCaptor.capture());
        assertNull(subject.getFragmentCallback());
    }

    @Test
    public void itShouldUnregisterReceiveOnUnregisterBroadcastReceiver() {
        subject.unregisterBroadcastReceiver();
        verify(mockFragmentCallback, times(1)).unregisterReceiver(broadcastReceiverArgumentCaptor.capture());
    }

    @Test
    public void itShouldNotUnregisterReceiveOnUnregisterBroadcastReceiverWhenCallbackNull() {
        subject.setFragmentCallback(null);
        subject.unregisterBroadcastReceiver();
        verify(mockFragmentCallback, never()).unregisterReceiver(broadcastReceiverArgumentCaptor.capture());
        verifyStatic();
        Log.e(anyString(), anyString());
    }

    @Test
    public void itShouldNavigateToConnectToDeviceWithPasswordScreenOnFriendlyNameFetchingSuccess() {
        String fetchedName = "friendlyName";
        subject.onFriendlyNameFetchingSuccess(fetchedName);
        verify(mockNavigator, times(1)).navigateToConnectToDeviceWithPasswordScreen(fetchedName);
    }

    @Test
    public void itShouldShowCancelDialogAndTagWhenCallbackIsNotNull() throws Exception {
        subject.showUnsuccessfulDialog();
        verifyShowingUnsuccessfulDialog();
    }

    @Test
    public void itShouldShowCancelDialogOnFriendlyNameFetchingFailed() throws Exception {
        subject.onFriendlyNameFetchingFailed();
        verifyShowingUnsuccessfulDialog();
    }

    @Test
    public void itShouldTrackPageOnTrackPageName() {
        subject.trackPageName();
        verifyStatic();
        EWSTagger.trackPage(Page.CONNECTING_WITH_DEVICE);
    }

    private void verifyShowingUnsuccessfulDialog() {
        verify(mockFragmentCallback, times(1)).showTroubleshootHomeWifiDialog(mockBaseContentConfiguration);
    }

    private void verifyFriendlyNameFetching(int times) {
        verify(mockDeviceFriendlyNameFetcher, times(times)).setNameFetcherCallback(subject);
        verify(mockDeviceFriendlyNameFetcher, times(times)).fetchFriendlyName();
    }
}