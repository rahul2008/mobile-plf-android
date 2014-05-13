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

public class PurifierManagerTest extends InstrumentationTestCase {
	
	private PurifierManager mPurifierMan;
	private SubscriptionManager mSubscriptionMan;
	
	public PurifierManagerTest() {
		
	}
	
	@Override
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		mSubscriptionMan = mock(SubscriptionManager.class);
		SubscriptionManager.setDummySubscriptionManagerForTesting(mSubscriptionMan);
		
		super.setUp();
	}
	
	public void testNoSubscriptionAtStartup() {
		verifyZeroInteractions(mSubscriptionMan);
	}

	public void testSetDisconnectedPurifier() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	public void testSetLocalPurifier() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan).disableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	public void testSetRemotePurifierNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
	}
	
	public void testSetRemotePurifierPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
	}
	

}
