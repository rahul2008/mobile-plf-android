/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.os.Handler;

import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor.NetworkChangedCallback;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor.NetworkState;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.networknode.NetworkNode.PAIRED_STATUS;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.testutil.TestAppliance;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiscoveryManagerTest extends RobolectricTest {

    private static final String APPLIANCE_IP_1 = "198.168.1.145";
    private static final String APPLIANCE_IP_2 = "198.168.1.120";
    private static final String APPLIANCE_CPPID_1 = "1c5a6bfffe634357";
    private static final String APPLIANCE_CPPID_2 = "1c5a6bfffe64314e";

    private DiscoveryManager<TestAppliance> mDiscoveryManager;
    private DiscoveryEventListener mListener;
    private NetworkMonitor mMockedNetworkMonitor;
    private CppController mMockedCppController;
    private TestApplianceFactory mTestApplianceFactory;
    private DICommApplianceDatabase<TestAppliance> mMockedApplianceDatabase;

    @SuppressWarnings("unchecked")
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mMockedCppController = mock(CppController.class);
        mTestApplianceFactory = new TestApplianceFactory();
        mMockedApplianceDatabase = mock(DICommApplianceDatabase.class);
        mMockedNetworkMonitor = mock(NetworkMonitor.class);

        mDiscoveryManager = new DiscoveryManager<TestAppliance>(mTestApplianceFactory, mMockedApplianceDatabase, mMockedNetworkMonitor);
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
        mListener = mock(DiscoveryEventListener.class);

        mDiscoveryManager.addDiscoveryEventListener(mListener);
    }

    private void setAppliancesList(TestAppliance[] appliancesList) {
        if (appliancesList == null || appliancesList.length == 0) {
            fail("Performing test with null/empty appliancesList");
        }

        LinkedHashMap<String, TestAppliance> appliances = new LinkedHashMap<String, TestAppliance>();
        for (TestAppliance appliance : appliancesList) {
            appliances.put(appliance.getNetworkNode().getCppId(), appliance);
        }
        mDiscoveryManager.setAppliancesListForTesting(appliances);
    }

    // ***** START TESTS FOR START/STOP METHODS *****
    @Test
    public void testOnStartNoNetwork() {
        SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
        NetworkMonitor monitor = mock(NetworkMonitor.class);
        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(ssdpHelper);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);

        mDiscoveryManager.start();

        verify(ssdpHelper, never()).startDiscoveryAsync();
        verify(ssdpHelper, never()).stopDiscoveryAsync();
    }

    @Test
    public void testOnStartMobile() {
        SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
        NetworkMonitor monitor = mock(NetworkMonitor.class);

        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(ssdpHelper);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);

        mDiscoveryManager.start();
        verify(ssdpHelper, never()).startDiscoveryAsync();
        verify(ssdpHelper, never()).stopDiscoveryAsync();
    }

    @Test
    public void testOnStartWifi() {
        SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
        NetworkMonitor monitor = mock(NetworkMonitor.class);
        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(ssdpHelper);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);

        mDiscoveryManager.start();

        verify(ssdpHelper).startDiscoveryAsync();
        verify(ssdpHelper, never()).stopDiscoveryAsync();
    }

    @Test
    public void testOnStop() {
        SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(ssdpHelper);

        mDiscoveryManager.stop();

        verify(ssdpHelper, never()).startDiscoveryAsync();
        verify(ssdpHelper).stopDiscoveryAsync();
    }

// ***** STOP TESTS FOR START/STOP METHODS *****

    // ***** START TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****
    @Test
    public void testLostBackgroundAllAppliancesFound() {
        TestAppliance appliance1 = createLocalAppliance(false);
        TestAppliance appliance2 = createLocalAppliance2(false);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{APPLIANCE_CPPID_1, APPLIANCE_CPPID_2})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertLocal(appliance1);
        assertLocal(appliance2);
    }

    @Test
    public void testLostBackgroundNoAppliancesFound() {
        TestAppliance appliance1 = createLocalAppliance(false);
        TestAppliance appliance2 = createLocalAppliance2(false);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertDisconnected(appliance2);
    }

    @Test
    public void testLostBackgroundNoAppliancesFoundPaired() {
        TestAppliance appliance1 = createLocalAppliance(true);
        TestAppliance appliance2 = createLocalAppliance2(true);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertDisconnected(appliance2);
    }

    @Test
    public void testLostBackgroundNoAppliancesFoundPairedOnline() {
        TestAppliance appliance1 = createLocalAppliance(true);
        TestAppliance appliance2 = createLocalAppliance2(true);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertDisconnected(appliance2);
    }

    @Test
    public void testLostBackgroundOneApplianceFound() {
        TestAppliance appliance1 = createLocalAppliance(false);
        TestAppliance appliance2 = createLocalAppliance2(false);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{APPLIANCE_CPPID_2})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertLocal(appliance2);
    }

    @Test
    public void testLostBackgroundOneApplianceFoundPaired() {
        TestAppliance appliance1 = createLocalAppliance(true);
        TestAppliance appliance2 = createLocalAppliance2(true);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{APPLIANCE_CPPID_2})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertLocal(appliance2);
    }

    @Test
    public void testLostBackgroundOneApplianceFoundPairedOnline() {
        TestAppliance appliance1 = createLocalAppliance(true);
        TestAppliance appliance2 = createLocalAppliance2(true);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{APPLIANCE_CPPID_2})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertLocal(appliance2);
    }

    @Test
    public void testLostBackgroundOneApplianceFoundOffline() {
        TestAppliance appliance1 = createDisconnectedAppliance(true);
        TestAppliance appliance2 = createDisconnectedAppliance2(true);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{APPLIANCE_CPPID_2})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertDisconnected(appliance2);
    }

    @Test
    public void testLostBackgroundOneApplianceFoundRemote() {
        TestAppliance appliance1 = createDisconnectedAppliance(true);
        TestAppliance appliance2 = createRemoteAppliance2(true);
        setAppliancesList(new TestAppliance[]{appliance1, appliance2});

        SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
        when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[]{APPLIANCE_CPPID_2})));
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
        mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

        assertDisconnected(appliance1);
        assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
    }

// ***** STOP TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****

    // ***** START TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****
    private NetworkChangedCallback captureNetworkChangedCallback() {
        ArgumentCaptor<NetworkChangedCallback> captor = ArgumentCaptor.forClass(NetworkChangedCallback.class);
        verify(mMockedNetworkMonitor, times(1)).setListener(captor.capture());
        NetworkChangedCallback capturedNetworkChangedCallback = captor.getValue();
        return capturedNetworkChangedCallback;
    }

    @Test
    public void testDiscoveryManagerRegistersForNetworkMonitorCallbacks() {
        verify(mMockedNetworkMonitor, times(1)).setListener(any(NetworkChangedCallback.class));
    }

    @Test
    public void testDiscoveryTimerWifiNoNetwork() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkChangedCallback capturedNetworkChangedCallback = captureNetworkChangedCallback();
        Handler discoveryHandler = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
        discoveryHandler.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);

        capturedNetworkChangedCallback.onNetworkChanged(NetworkState.NONE, "");

        assertFalse(discoveryHandler.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertFalse(discoveryHandler.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
    }

    @Test
    public void testDiscoveryTimerWifiMobile() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkChangedCallback capturedNetworkChangedCallback = captureNetworkChangedCallback();
        Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
        discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);

        capturedNetworkChangedCallback.onNetworkChanged(NetworkState.MOBILE, "");

        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
    }

    @Test
    public void testDiscoveryTimerNoNetworkWifi() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkChangedCallback capturedNetworkChangedCallback = captureNetworkChangedCallback();
        Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();

        capturedNetworkChangedCallback.onNetworkChanged(NetworkState.WIFI_WITH_INTERNET, "JeroenMols");

        assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));

        discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
    }

    @Test
    public void testDiscoveryTimerStartNoNetwork() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkMonitor monitor = mock(NetworkMonitor.class);
        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);
        Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();

        mDiscoveryManager.start();

        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
    }

    @Test
    public void testDiscoveryTimerStartMobile() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkMonitor monitor = mock(NetworkMonitor.class);
        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);
        Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();

        mDiscoveryManager.start();

        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
    }

    @Test
    public void testDiscoveryTimerStartWifi() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkMonitor monitor = mock(NetworkMonitor.class);
        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);
        Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();

        mDiscoveryManager.start();

        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
    }

    @Test
    public void testDiscoveryTimerStop() {
        mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));

        NetworkMonitor monitor = mock(NetworkMonitor.class);
        when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
        mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);

        Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
        discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);
        discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE, 10000);
        mDiscoveryManager.start();
        assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
        assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));

        discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
        discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE);
    }
// ***** STOP TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****

    @Test
    public void testAddListener() {
        DiscoveryEventListener listener = mock(DiscoveryEventListener.class);
        mDiscoveryManager.addDiscoveryEventListener(listener);

        triggerOnDiscoveredDevicesListChanged();

        verify(listener, times(1)).onDiscoveredAppliancesListChanged();
    }

    @Test
    public void testAddRemoveListener() {
        DiscoveryEventListener listener = mock(DiscoveryEventListener.class);
        mDiscoveryManager.addDiscoveryEventListener(listener);
        mDiscoveryManager.removeDiscoverEventListener(listener);

        triggerOnDiscoveredDevicesListChanged();

        verify(listener, never()).onDiscoveredAppliancesListChanged();
    }

    @Test
    public void testRemoveNonAddedListener() {
        DiscoveryEventListener listener = mock(DiscoveryEventListener.class);
        mDiscoveryManager.removeDiscoverEventListener(listener);

        triggerOnDiscoveredDevicesListChanged();

        verify(listener, never()).onDiscoveredAppliancesListChanged();
    }

    @Test
    public void testRemoveNullListener() {
        mDiscoveryManager.removeDiscoverEventListener(null);

        triggerOnDiscoveredDevicesListChanged();
    }

    @Test
    public void testAddNullListener() {
        mDiscoveryManager.addDiscoveryEventListener(null);

        triggerOnDiscoveredDevicesListChanged();
    }

    @Test
    public void testListenerCannotBeAddedTwice() {
        DiscoveryEventListener listener = mock(DiscoveryEventListener.class);
        mDiscoveryManager.addDiscoveryEventListener(listener);
        mDiscoveryManager.addDiscoveryEventListener(listener);

        triggerOnDiscoveredDevicesListChanged();

        verify(listener, times(1)).onDiscoveredAppliancesListChanged();
    }

    private void triggerOnDiscoveredDevicesListChanged() {
        TestAppliance localAppliance = createLocalAppliance(false);
        setAppliancesList(new TestAppliance[]{localAppliance});
        NetworkChangedCallback networkChangedCallback = captureNetworkChangedCallback();
        networkChangedCallback.onNetworkChanged(NetworkState.NONE, null);
    }

    private TestAppliance createDisconnectedAppliance(boolean isPaired) {
        return createTestAppliance(APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED, isPaired);
    }

    private TestAppliance createDisconnectedAppliance2(boolean isPaired) {
        return createTestAppliance(APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED, isPaired);
    }

    private TestAppliance createLocalAppliance(boolean isPaired) {
        return createTestAppliance(APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY, isPaired);
    }

    private TestAppliance createLocalAppliance2(boolean isPaired) {
        return createTestAppliance(APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY, isPaired);
    }

    private TestAppliance createRemoteAppliance(boolean isPaired) {
        return createTestAppliance(APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY, isPaired);
    }

    private TestAppliance createRemoteAppliance2(boolean isPaired) {
        return createTestAppliance(APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY, isPaired);
    }

    private void assertDisconnected(TestAppliance appliance) {
        assertEquals(ConnectionState.DISCONNECTED, appliance.getNetworkNode().getConnectionState());
    }

    private void assertLocal(TestAppliance appliance) {
        assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance.getNetworkNode().getConnectionState());
    }

    private void assertRemote(TestAppliance appliance) {
        assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance.getNetworkNode().getConnectionState());
    }

    private TestAppliance createTestAppliance(String cppId, String ip, String name, long bootId, ConnectionState connectionState, boolean isPaired) {
        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(cppId);
        networkNode.setIpAddress(ip);
        networkNode.setName(name);
        networkNode.setConnectionState(connectionState);
        networkNode.setPairedState(isPaired ? PAIRED_STATUS.PAIRED : PAIRED_STATUS.NOT_PAIRED);

        return new TestAppliance(networkNode);
    }

    private class TestApplianceFactory extends DICommApplianceFactory<TestAppliance> {

        @Override
        public boolean canCreateApplianceForNode(NetworkNode networkNode) {
            return true;
        }

        @Override
        public TestAppliance createApplianceForNode(NetworkNode networkNode) {
            return new TestAppliance(networkNode);
        }
    }
}