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
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.DICommAppliance;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryEventListener;
import com.philips.cl.di.dev.pa.newpurifier.DiscoveryManager;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor;
import com.philips.cl.di.dev.pa.newpurifier.NetworkMonitor.NetworkState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.SsdpServiceHelper;
import com.philips.cl.di.dicomm.appliance.DICommApplianceFactory;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;
import com.philips.cl.di.dicomm.communication.NullStrategy;

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
		DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), new TestApplianceFactory());

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
		DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), new TestApplianceFactory());
		super.tearDown();
	}

	// TODO add unit tests for SSDP events
	// TODO add unit tests for Network events

	private void setPurifierList(DICommAppliance[] appliancesList) {
		if (appliancesList == null || appliancesList.length == 0) {
			fail("Performing test with null/empty devicesList");
		}

		LinkedHashMap<String, DICommAppliance> devices = new LinkedHashMap<String, DICommAppliance>();
		for (DICommAppliance appliance : appliancesList) {
			devices.put(appliance.getNetworkNode().getCppId(), appliance);
		}
		mDiscMan.setAppliancesListForTesting(devices);
	}


// ***** START TESTS FOR START/STOP METHODS *****
	public void testOnStartNoNetwork() {
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), new TestApplianceFactory());
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
	}

	public void testOnStartMobile() {
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), new TestApplianceFactory());
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
	}

	public void testOnStartWifi() {
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), new TestApplianceFactory());
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
	}

	public void testOnStop() {
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), new TestApplianceFactory());
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
	}

// ***** STOP TESTS FOR START/STOP METHODS *****

// ***** START TESTS TO UPDATE NETWORKSTATE WHEN CPP EVENT RECEIVED *****
	public void testCppConnectNotPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

    public void testCppConnectNotPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedRemoteWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedRemoteMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedRemoteNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedRemoteWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedRemoteMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedRemoteNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSingleConnectPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSingleConnectPairedDisconnectedWifi2() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSingleDisconnectPairedRemoteWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppInvalidEventReceived() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "I'm an invalid event";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppEventReceivedDifferentPurifier() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + "eui64notexist" + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReaConnectNotPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedRemoteWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedRemoteMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedRemoteNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedLocallyWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedLocallyMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedLocallyNone() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedRemoteWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedRemoteMobile() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppAllConnectPairedDisconnectedWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppAllDisconnectPairedRemoteWifi() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + PURIFIER_EUI64_1 + "\",\"" + PURIFIER_EUI64_2 + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, false);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqInvalidEventReceived() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "I'm an invalid event";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqEventReceivedDifferentPurifier() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + "eui64notexist" + "\"]}";
		mDiscMan.onDiscoverEventReceived(event, true);

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSignonEventReceivedDisconnected() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOnViaCpp();

		assertFalse(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
	}

	public void testCppSignonEventReceivedRemoteLocal() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOnViaCpp();

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertFalse(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppSignoffEventReceivedRemote() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOffViaCpp();

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
	}

	public void testCppSignoffEventReceivedDisconnectedLocal() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		when(mNetwork.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscMan.onSignedOffViaCpp();

		assertTrue(purifier1.getNetworkNode().isOnlineViaCpp());
		assertTrue(purifier2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

// ***** STOP TESTS TO UPDATE NETWORKSTATE WHEN CPP EVENT RECEIVED *****

// ***** START TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****
	public void testLostBackgroundAllDevicesFound() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_1, PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundNoDevicesFound() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundNoDevicesFoundPaired() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundNoDevicesFoundPairedOnline() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneDeviceFound() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneDeviceFoundPaired() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(false);
		purifier2.getNetworkNode().setOnlineViaCpp(false);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneDeviceFoundPairedOnline() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneDeviceFoundOffline() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, purifier2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneDeviceFoundRemote() {
		AirPurifier purifier1 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_1, null, PURIFIER_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		AirPurifier purifier2 = createAirPurifier(mock(CommunicationStrategy.class), PURIFIER_EUI64_2, null, PURIFIER_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		purifier1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		purifier1.getNetworkNode().setOnlineViaCpp(true);
		purifier2.getNetworkNode().setOnlineViaCpp(true);
		setPurifierList(new AirPurifier[] {purifier1, purifier2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {PURIFIER_EUI64_2})));
		mDiscMan.setDummySsdpServiceHelperForTesting(helper);
		mDiscMan.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, purifier1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, purifier2.getNetworkNode().getConnectionState());
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

    private AirPurifier createAirPurifier(CommunicationStrategy communicationStrategy, String purifierEui641, String usn, String ip, String name, long bootId,
        ConnectionState connectionState) {
    
        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(purifierEui641);
        networkNode.setIpAddress(ip);
        networkNode.setName(name);
        networkNode.setConnectionState(connectionState);
        
        return new AirPurifier(networkNode,communicationStrategy);
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

    private class TestAppliance extends DICommAppliance {

		public TestAppliance(NetworkNode networkNode) {
			super(networkNode, new NullStrategy());
		}
    }

}
