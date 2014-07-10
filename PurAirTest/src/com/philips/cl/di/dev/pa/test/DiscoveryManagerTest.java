package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import android.os.Handler;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.cpp.CppDiscoveryHelper;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.SsdpServiceHelper;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;

public class DiscoveryManagerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP_1 = "198.168.1.145";
	private static final String PURIFIER_IP_2 = "198.168.1.120";
	private static final String PURIFIER_EUI64_1 = "1c5a6bfffe634357";
	private static final String PURIFIER_EUI64_2 = "1c5a6bfffe64314e";
	
	private DiscoveryManager mDiscMan;
	private DiscoveryEventListener mListener;
	private NetworkMonitor mNetwork;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		mDiscMan = DiscoveryManager.getInstance();
		mListener = mock(DiscoveryEventListener.class);
		mDiscMan.setDummyDiscoveryEventListenerForTesting(mListener);
		mNetwork = mock(NetworkMonitor.class);
		mDiscMan.setDummyNetworkMonitorForTesting(mNetwork);
		
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// Clean up resources
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		super.tearDown();
	}
	
	// TODO add unit tests for SSDP events
	// TODO add unit tests for Network events
	
	private void setPurifierList(PurAirDevice[] devicesList) {
		if (devicesList == null || devicesList.length == 0) {
			fail("Performing test with null/empty devicesList");
		}
		
		LinkedHashMap<String, PurAirDevice> devices = new LinkedHashMap<String, PurAirDevice>();
		for (PurAirDevice device : devicesList) {
			devices.put(device.getEui64(), device);
		}
		mDiscMan.setPurifierListForTesting(devices);
	}
	

// ***** START TESTS FOR START/STOP METHODS ***** 
	public void testOnStartNoNetwork() {
		SubscriptionHandler mSubHandler = mock(SubscriptionHandler.class);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(mSubHandler);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
		CppDiscoveryHelper cppHelper = mock(CppDiscoveryHelper.class);
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		
		DiscoveryManager manager = DiscoveryManager.getInstance();
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		manager.setDummySsdpServiceHelperForTesting(ssdpHelper);
		manager.setDummyCppDiscoveryHelperForTesting(cppHelper);
		manager.setDummyNetworkMonitorForTesting(monitor);
		
		manager.start(mock(DiscoveryEventListener.class));
		verify(ssdpHelper, never()).startDiscoveryAsync();
		verify(ssdpHelper, never()).stopDiscoveryAsync();
		verify(cppHelper).startDiscoveryViaCpp();
		verify(cppHelper, never()).stopDiscoveryViaCpp();
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
	}
	
	public void testOnStartMobile() {
		SubscriptionHandler mSubHandler = mock(SubscriptionHandler.class);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(mSubHandler);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
		CppDiscoveryHelper cppHelper = mock(CppDiscoveryHelper.class);
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		
		DiscoveryManager manager = DiscoveryManager.getInstance();
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		manager.setDummySsdpServiceHelperForTesting(ssdpHelper);
		manager.setDummyCppDiscoveryHelperForTesting(cppHelper);
		manager.setDummyNetworkMonitorForTesting(monitor);
		
		manager.start(mock(DiscoveryEventListener.class));
		verify(ssdpHelper, never()).startDiscoveryAsync();
		verify(ssdpHelper, never()).stopDiscoveryAsync();
		verify(cppHelper).startDiscoveryViaCpp();
		verify(cppHelper, never()).stopDiscoveryViaCpp();
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
	}
	
	public void testOnStartWifi() {
		SubscriptionHandler mSubHandler = mock(SubscriptionHandler.class);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(mSubHandler);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
		CppDiscoveryHelper cppHelper = mock(CppDiscoveryHelper.class);
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		
		DiscoveryManager manager = DiscoveryManager.getInstance();
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		manager.setDummySsdpServiceHelperForTesting(ssdpHelper);
		manager.setDummyCppDiscoveryHelperForTesting(cppHelper);
		manager.setDummyNetworkMonitorForTesting(monitor);
		
		manager.start(mock(DiscoveryEventListener.class));
		verify(ssdpHelper).startDiscoveryAsync();
		verify(ssdpHelper, never()).stopDiscoveryAsync();
		verify(cppHelper).startDiscoveryViaCpp();
		verify(cppHelper, never()).stopDiscoveryViaCpp();
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
	}

	public void testOnStop() {
		SubscriptionHandler mSubHandler = mock(SubscriptionHandler.class);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(mSubHandler);
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SsdpServiceHelper ssdpHelper = mock(SsdpServiceHelper.class);
		CppDiscoveryHelper cppHelper = mock(CppDiscoveryHelper.class);
		
		DiscoveryManager manager = DiscoveryManager.getInstance();
		manager.setDummySsdpServiceHelperForTesting(ssdpHelper);
		manager.setDummyCppDiscoveryHelperForTesting(cppHelper);
		
		manager.stop();
		verify(ssdpHelper, never()).startDiscoveryAsync();
		verify(ssdpHelper).stopDiscoveryAsync();
		verify(cppHelper, never()).startDiscoveryViaCpp();
		verify(cppHelper).stopDiscoveryViaCpp();
		
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
	}

// ***** STOP TESTS FOR START/STOP METHODS ***** 

// ***** START TESTS TO UPDATE NETWORKSTATE WHEN CPP EVENT RECEIVED ***** 
	public void testCppConnectNotPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectNotPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectNotPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectNotPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectNotPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectNotPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedRemoteWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedRemoteMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppConnectPairedRemoteNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectNotPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectNotPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectNotPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectNotPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectNotPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectNotPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedRemoteWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedRemoteMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppDisconnectPairedRemoteNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppSingleConnectPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppSingleConnectPairedDisconnectedWifi2() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppSingleDisconnectPairedRemoteWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppInvalidEventReceived() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "I'm an invalid event";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppEventReceivedDifferentPurifier() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + "eui64notexist" + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectNotPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectNotPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReaConnectNotPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectNotPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectNotPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectNotPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedRemoteWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedRemoteMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqConnectPairedRemoteNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectNotPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectNotPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectNotPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectNotPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectNotPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectNotPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedDisconnectedMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedDisconnectedNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedLocallyWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedLocallyMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedLocallyNone() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedRemoteWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqDisconnectPairedRemoteMobile() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppAllConnectPairedDisconnectedWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppAllDisconnectPairedRemoteWifi() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqInvalidEventReceived() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "I'm an invalid event";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppReqEventReceivedDifferentPurifier() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + "eui64notexist" + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
		verify(mListener).onDiscoveredDevicesListChanged();
	}
	
	public void testCppSignonEventReceivedDisconnected() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOnViaCpp();
		
		assertFalse(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
	}
	
	public void testCppSignonEventReceivedRemoteLocal() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOnViaCpp();
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertFalse(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
	public void testCppSignoffEventReceivedRemote() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOffViaCpp();
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
	}
	
	public void testCppSignoffEventReceivedDisconnectedLocal() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOffViaCpp();
		
		assertTrue(purifier1.isOnlineViaCpp());
		assertTrue(purifier2.isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
		verify(mListener, never()).onDiscoveredDevicesListChanged();
	}
	
// ***** STOP TESTS TO UPDATE NETWORKSTATE WHEN CPP EVENT RECEIVED ***** 

// ***** START TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****
	public void testLostBackgroundAllDevicesFound() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_1, PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
	}

	public void testLostBackgroundNoDevicesFound() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
	}

	public void testLostBackgroundNoDevicesFoundPaired() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
	}

	public void testLostBackgroundNoDevicesFoundPairedOnline() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
	}
	
	public void testLostBackgroundOneDeviceFound() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
	}
	
	public void testLostBackgroundOneDeviceFoundPaired() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(false);
		purifier2.setOnlineViaCpp(false);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
	}
	
	public void testLostBackgroundOneDeviceFoundPairedOnline() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getConnectionState());
	}
	
	public void testLostBackgroundOneDeviceFoundOffline() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getConnectionState());
	}
	
	public void testLostBackgroundOneDeviceFoundRemote() {
		PurAirDevice purifier1 = new PurAirDevice(PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		PurAirDevice purifier2 = new PurAirDevice(PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.setPairing(true);
		purifier2.setPairing(true);
		purifier1.setOnlineViaCpp(true);
		purifier2.setOnlineViaCpp(true);
		setPurifierList(new PurAirDevice[] {purifier1, purifier2});
		
		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesEui64()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostDevicesInBackgroundOfflineOrRemote();
		
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getConnectionState());
	}
	
// ***** STOP TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND ***** 
	
// ***** START TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND ***** 
	public void testDiscoveryTimerNetworkNoNetwork() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);
		mDiscMan.onNetworkChanged(NetworkState.NONE, "");
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));

		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
	}
	
	public void testDiscoveryTimerNetworkMobile() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);
		mDiscMan.onNetworkChanged(NetworkState.MOBILE, "");
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}
	public void testDiscoveryTimerNetworkWifi() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		mDiscMan.onNetworkChanged(NetworkState.WIFI_WITH_INTERNET, "myssid");
		assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
		
		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
	}
	
	public void testDiscoveryTimerStartNoNetwork() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		mDiscMan.setDummyNetworkMonitorForTesting(monitor);
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		mDiscMan.start(mock(DiscoveryEventListener.class));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}

	public void testDiscoveryTimerStartMobile() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		mDiscMan.setDummyNetworkMonitorForTesting(monitor);
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		mDiscMan.start(mock(DiscoveryEventListener.class));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}
	
	public void testDiscoveryTimerStartWifi() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.setDummyNetworkMonitorForTesting(monitor);
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		mDiscMan.start(mock(DiscoveryEventListener.class));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}
	
	public void testDiscoveryTimerStop() {
		mDiscMan.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscMan.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.setDummyNetworkMonitorForTesting(monitor);
		
		Handler discoveryHand = mDiscMan.getDiscoveryTimeoutHandlerForTesting();
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE, 10000);
		mDiscMan.start(mock(DiscoveryEventListener.class));
		assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
		
		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE);		
	}
// ***** STOP TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND ***** 
}
