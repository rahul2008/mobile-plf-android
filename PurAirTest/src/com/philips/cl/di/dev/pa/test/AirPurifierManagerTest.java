package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifierManager;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;

public class AirPurifierManagerTest extends InstrumentationTestCase {
	
	private AirPurifierManager mPurifierMan;
	private SubscriptionHandler mSubscriptionMan;
	private AirPurifierEventListener mEventListener;
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		mSubscriptionMan = mock(SubscriptionHandler.class);
		
		mEventListener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(mEventListener);
		
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// Remove mock objects after tests
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		super.tearDown();
	}
	
	private AirPurifier createMockDisconnectedPurifier() {
		AirPurifier device = mock(AirPurifier.class);
		NetworkNode networkNode = mock(NetworkNode.class);
		when(device.getNetworkNode()).thenReturn(networkNode);
		when(networkNode.getConnectionState()).thenReturn(ConnectionState.DISCONNECTED);
		return device;
	}
	
	public void testNoSubscriptionAtStartup() {
		verifyZeroInteractions(mSubscriptionMan);
	}
	
	public void testAddPurifierListener(){
		AirPurifier device = createMockDisconnectedPurifier();	
		
		mPurifierMan.setCurrentPurifier(device);
		
		verify(device,times(1)).setPurifierListener(mPurifierMan);
	}
	
	public void testAddPurifierListenerAfterCurrentPurifierIsAlreadySet(){
		AirPurifier device1 = createMockDisconnectedPurifier();			
		mPurifierMan.setCurrentPurifier(device1);		
		
		AirPurifier device2 = createMockDisconnectedPurifier();		
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(device1,times(1)).setPurifierListener(null);
		verify(device2,times(1)).setPurifierListener(mPurifierMan);
	}
	
	public void testRemovePurifierListener(){
		AirPurifier device1 = createMockDisconnectedPurifier();			
		mPurifierMan.setCurrentPurifier(device1);		
		
		mPurifierMan.removeCurrentPurifier();
		
		verify(device1,times(1)).setPurifierListener(null);
	}

// ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES *****
	public void testSetFirstDisconnectedPurifier() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetFirstLocalPurifier() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetFirstRemotePurifierNotPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetFirstRemotePurifierPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetDisconnectedPurifierAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetLocalPurifierAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierNotPairedAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierPairedAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetDisconnectedPurifierAfterLocally() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
//		verify(mSubscriptionMan).disableLocalSubscription();
//		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetLocalPurifierAfterLocally() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
//		verify(mSubscriptionMan).disableLocalSubscription();
//		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierNotPairedAfterLocally() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
//		verify(mSubscriptionMan).disableLocalSubscription();
//		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierPairedAfterLocally() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device2);
		
//		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
//		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetDisconnectedPurifierAfterNotPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetLocalPurifierAfterNotPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierNotPairedAfterNotPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierPairedAfterNotPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetDisconnectedPurifierAfterPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetLocalPurifierAfterPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierNotPairedAfterPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testSetRemotePurifierPairedAfterPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		AirPurifier device2 = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device2.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
	}
	
	public void testRemovePurifierPairedAfterNoPurifier() {
		reset(mSubscriptionMan);
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterLocal() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterRemoteNotPaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan,never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterRemotePaired() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan,never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents();
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
// ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES *****
	
	
// ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CONNECTIONSTATE CHANGES *****

	public void testPurifierDisconnectedAfterLocal() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		reset(mSubscriptionMan);
		device.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		verify(listener).onAirPurifierChanged();
	}

	public void testPurifierRemotedAfterLocal() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		reset(mSubscriptionMan);
		device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		verify(listener).onAirPurifierChanged();
	}
	
	public void testPurifierLocalAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		reset(mSubscriptionMan);
		device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_LOCALLY);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		verify(listener).onAirPurifierChanged();
	}

	public void testPurifierRemoteAfterDisconnected() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		reset(mSubscriptionMan);
		device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		verify(listener).onAirPurifierChanged();
	}
	
	public void testPurifierLocalAfterRemote() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.DISCONNECTED,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		reset(mSubscriptionMan);
		device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_LOCALLY);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
//		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		verify(listener).onAirPurifierChanged();
	}

	public void testPurifierDisconnectedAfterRemote() {
		AirPurifier device = new AirPurifier(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY,mSubscriptionMan);
		device.getNetworkNode().setPairedState(NetworkNode.PAIRED_STATUS.PAIRED);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		reset(mSubscriptionMan);
		device.getNetworkNode().setConnectionState(ConnectionState.DISCONNECTED);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents();
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		
		verify(listener).onAirPurifierChanged();
	}

// ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CONNECTIONSTATE CHANGES *****

	

// ***** START TEST TO START/STOP SUBSCRIPTION WHEN ADDING PURIFIEREVENTLISTENERS *****
	public void testStartSubscriptionAddFirstEventListener() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		verify(mSubscriptionMan).enableLocalSubscription();
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
	}
	
	public void testStartSubscriptionAddSecondEventListener() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
	}
	
	public void testStopSubscriptionRemoveFirstEventListener() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		mPurifierMan.removeAirPurifierEventListener(listener);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
	}
	
	public void testStopSubscriptionRemoveSecondEventListener() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		
		mPurifierMan.removeAirPurifierEventListener(listener);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
	}
	
	public void testStopSubscriptionRemoveBothEventListeners() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		
		mPurifierMan.removeAirPurifierEventListener(listener);
		mPurifierMan.removeAirPurifierEventListener(listener2);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
	}
	
	public void testStartStopSubscriptionAddRemoveListenersSequence() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		reset(mSubscriptionMan);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener3 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		mPurifierMan.removeAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener3);
		mPurifierMan.removeAirPurifierEventListener(listener2);
		mPurifierMan.removeAirPurifierEventListener(listener3);
		
		verify(mSubscriptionMan).enableLocalSubscription();
//		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
//		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents();
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents();
	}
	
// ***** END TEST TO START/STOP SUBSCRIPTION WHEN ADDING PURIFIEREVENTLISTENERS *****
	
	public void testDeadlock() {
		AirPurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = AirPurifierManager.getInstance();
		AirPurifier device = new AirPurifier(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY,mSubscriptionMan);
		mPurifierMan.setCurrentPurifier(device);
		
		device.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
	}
	
}
