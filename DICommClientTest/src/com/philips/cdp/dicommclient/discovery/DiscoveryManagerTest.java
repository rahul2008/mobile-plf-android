/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import org.mockito.ArgumentCaptor;

import android.os.Handler;

import com.philips.cdp.dicommclient.appliance.DICommApplianceDatabase;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor.NetworkChangedCallback;
import com.philips.cdp.dicommclient.discovery.NetworkMonitor.NetworkState;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.MockitoTestCase;
import com.philips.cdp.dicommclient.testutil.TestAppliance;

public class DiscoveryManagerTest extends MockitoTestCase {

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

		mDiscoveryManager = new DiscoveryManager<TestAppliance>(mMockedCppController, mTestApplianceFactory, mMockedApplianceDatabase, mMockedNetworkMonitor);
		
		
		mListener = mock(DiscoveryEventListener.class);
		mDiscoveryManager.setDummyDiscoveryEventListenerForTesting(mListener);
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		mDiscoveryManager.setDummyNetworkMonitorForTesting(mMockedNetworkMonitor);
	}

	@Override
	protected void tearDown() throws Exception {
		// Clean up resources
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), mock(CppController.class), new TestApplianceFactory());
		super.tearDown();
	}

	// TODO add unit tests for SSDP events
	// TODO add unit tests for Network events

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
	public void testOnStartNoNetwork() {
		DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), mock(CppController.class), new TestApplianceFactory());
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
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), mock(CppController.class), new TestApplianceFactory());
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
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), mock(CppController.class), new TestApplianceFactory());
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
		DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), mock(CppController.class), new TestApplianceFactory());
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
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

    public void testCppConnectNotPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectNotPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedRemoteWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedRemoteMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppConnectPairedRemoteNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectNotPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedRemoteWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedRemoteMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppDisconnectPairedRemoteNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSingleConnectPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSingleConnectPairedDisconnectedWifi2() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSingleDisconnectPairedRemoteWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppInvalidEventReceived() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "I'm an invalid event";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppEventReceivedDifferentPurifier() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + "eui64notexist" + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReaConnectNotPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectNotPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedRemoteWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedRemoteMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqConnectPairedRemoteNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectNotPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedDisconnectedNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedLocallyWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedLocallyMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedLocallyNone() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedRemoteWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqDisconnectPairedRemoteMobile() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppAllConnectPairedDisconnectedWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppAllDisconnectPairedRemoteWifi() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Disconnected\",\"ClientIds\":[\"" + APPLIANCE_CPPID_1 + "\",\"" + APPLIANCE_CPPID_2 + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), false);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqInvalidEventReceived() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "I'm an invalid event";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppReqEventReceivedDifferentPurifier() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		String event = "{\"State\":\"Connected\",\"ClientIds\":[\"" + "eui64notexist" + "\"]}";
		mDiscoveryManager.onDiscoverEventReceived(CppDiscoveryHelper.parseDiscoverInfo(event), true);

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
		verify(mListener).onDiscoveredAppliancesListChanged();
	}

	public void testCppSignonEventReceivedDisconnected() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscoveryManager.onSignedOnViaCpp();

		assertFalse(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
	}

	public void testCppSignonEventReceivedRemoteLocal() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscoveryManager.onSignedOnViaCpp();

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertFalse(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

	public void testCppSignoffEventReceivedRemote() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_REMOTELY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscoveryManager.onSignedOffViaCpp();

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_REMOTELY, appliance2.getNetworkNode().getConnectionState());
	}

	public void testCppSignoffEventReceivedDisconnectedLocal() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		when(mMockedNetworkMonitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscoveryManager.onSignedOffViaCpp();

		assertTrue(appliance1.getNetworkNode().isOnlineViaCpp());
		assertTrue(appliance2.getNetworkNode().isOnlineViaCpp());
		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
		verify(mListener, never()).onDiscoveredAppliancesListChanged();
	}

// ***** STOP TESTS TO UPDATE NETWORKSTATE WHEN CPP EVENT RECEIVED *****

// ***** START TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****
	public void testLostBackgroundAllAppliancesFound() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {APPLIANCE_CPPID_1, APPLIANCE_CPPID_2})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundNoAppliancesFound() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundNoAppliancesFoundPaired() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundNoAppliancesFoundPairedOnline() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneApplianceFound() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {APPLIANCE_CPPID_2})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneApplianceFoundPaired() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(false);
		appliance2.getNetworkNode().setOnlineViaCpp(false);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {APPLIANCE_CPPID_2})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneApplianceFoundPairedOnline() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.CONNECTED_LOCALLY);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_LOCALLY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {APPLIANCE_CPPID_2})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.CONNECTED_LOCALLY, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneApplianceFoundOffline() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.DISCONNECTED);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {APPLIANCE_CPPID_2})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
		assertEquals(ConnectionState.DISCONNECTED, appliance2.getNetworkNode().getConnectionState());
	}

	public void testLostBackgroundOneApplianceFoundRemote() {
		TestAppliance appliance1 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_1, APPLIANCE_IP_1, "Purifier1", 0, ConnectionState.DISCONNECTED);
		TestAppliance appliance2 = createTestAppliance(mock(CommunicationStrategy.class), APPLIANCE_CPPID_2, APPLIANCE_IP_2, "Purifier2", 0, ConnectionState.CONNECTED_REMOTELY);
		appliance1.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		appliance1.getNetworkNode().setOnlineViaCpp(true);
		appliance2.getNetworkNode().setOnlineViaCpp(true);
		setAppliancesList(new TestAppliance[] {appliance1, appliance2});

		SsdpServiceHelper helper = mock(SsdpServiceHelper.class);
		when(helper.getOnlineDevicesCppId()).thenReturn(new ArrayList<String>(Arrays.asList(new String[] {APPLIANCE_CPPID_2})));
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(helper);
		mDiscoveryManager.markLostAppliancesInBackgroundOfflineOrRemote();

		assertEquals(ConnectionState.DISCONNECTED, appliance1.getNetworkNode().getConnectionState());
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
	
	public void testDiscoveryManagerRegistersForNetworkMonitorCallbacks() {
		verify(mMockedNetworkMonitor, times(1)).setListener(any(NetworkChangedCallback.class));
	}
	
	public void testDiscoveryTimerWifiNoNetwork() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkChangedCallback capturedNetworkChangedCallback = captureNetworkChangedCallback();
		Handler discoveryHandler = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
		discoveryHandler.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);

		capturedNetworkChangedCallback.onNetworkChanged(NetworkState.NONE, "");
		
		assertFalse(discoveryHandler.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHandler.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}

	public void testDiscoveryTimerWifiMobile() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkChangedCallback capturedNetworkChangedCallback = captureNetworkChangedCallback();
		Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);
		
		capturedNetworkChangedCallback.onNetworkChanged(NetworkState.MOBILE, "");

		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}
	
	public void testDiscoveryTimerNoNetworkWifi() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkChangedCallback capturedNetworkChangedCallback = captureNetworkChangedCallback();
		Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();

		capturedNetworkChangedCallback.onNetworkChanged(NetworkState.WIFI_WITH_INTERNET, "JeroenMols");

		assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
		
		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
	}

	public void testDiscoveryTimerStartNoNetwork() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.NONE);
		mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);
		Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
		
		mDiscoveryManager.start(mock(DiscoveryEventListener.class));

		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}

	public void testDiscoveryTimerStartMobile() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.MOBILE);
		mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);
		Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
		
		mDiscoveryManager.start(mock(DiscoveryEventListener.class));

		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}

	public void testDiscoveryTimerStartWifi() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);
		Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
		
		mDiscoveryManager.start(mock(DiscoveryEventListener.class));
		
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertFalse(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));
	}

	public void testDiscoveryTimerStop() {
		mDiscoveryManager.setDummySsdpServiceHelperForTesting(mock(SsdpServiceHelper.class));
		mDiscoveryManager.setDummyCppDiscoveryHelperForTesting(mock(CppDiscoveryHelper.class));
		NetworkMonitor monitor = mock(NetworkMonitor.class);
		when(monitor.getLastKnownNetworkState()).thenReturn(NetworkState.WIFI_WITH_INTERNET);
		mDiscoveryManager.setDummyNetworkMonitorForTesting(monitor);

		Handler discoveryHand = mDiscoveryManager.getDiscoveryTimeoutHandlerForTesting();
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE, 10000);
		discoveryHand.sendEmptyMessageDelayed(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE, 10000);
		mDiscoveryManager.start(mock(DiscoveryEventListener.class));
		assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE));
		assertTrue(discoveryHand.hasMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE));

		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_WAITFORLOCAL_MESSAGE);
		discoveryHand.removeMessages(DiscoveryManager.DISCOVERY_SYNCLOCAL_MESSAGE);
	}

// ***** STOP TESTS TO UPDATE CONNECTION STATE FROM TIMER AFTER APP TO FOREGROUND *****

    private TestAppliance createTestAppliance(CommunicationStrategy communicationStrategy, String cppId, String ip, String name, long bootId, ConnectionState connectionState) {

        NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(bootId);
        networkNode.setCppId(cppId);
        networkNode.setIpAddress(ip);
        networkNode.setName(name);
        networkNode.setConnectionState(connectionState);

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
