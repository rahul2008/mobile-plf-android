package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;
import com.philips.icpinterface.ResetDevice;

public class PurifierManagerTest extends InstrumentationTestCase {
	
	private PurifierManager mPurifierMan;
	private SubscriptionManager mSubscriptionMan;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		setMockSubscriptionManager();
		
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		// Remove mock objects after tests
		PurifierManager.setDummyPurifierManagerForTesting(null);
		SubscriptionManager.setDummySubscriptionManagerForTesting(null);
		super.tearDown();
	}
	
	private void setMockSubscriptionManager() {
		mSubscriptionMan = mock(SubscriptionManager.class);
		SubscriptionManager.setDummySubscriptionManagerForTesting(mSubscriptionMan);
	}
	
	public void testNoSubscriptionAtStartup() {
		verifyZeroInteractions(mSubscriptionMan);
	}

	public void testSetFirstDisconnectedPurifier() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetFirstLocalPurifier() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetFirstRemotePurifierNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetFirstRemotePurifierPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetDisconnectedPurifierAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetLocalPurifierAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierNotPairedAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierPairedAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device2.setPairing(true);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetDisconnectedPurifierAfterLocally() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetLocalPurifierAfterLocally() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierNotPairedAfterLocally() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierPairedAfterLocally() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device2.setPairing(true);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetDisconnectedPurifierAfterNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetLocalPurifierAfterNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierNotPairedAfterNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierPairedAfterNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device2.setPairing(true);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetDisconnectedPurifierAfterPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetLocalPurifierAfterPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierNotPairedAfterPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());

		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testSetRemotePurifierPairedAfterPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		PurAirDevice device2 = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device2.setPairing(true);
		mPurifierMan.setCurrentPurifier(device2);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}

}
