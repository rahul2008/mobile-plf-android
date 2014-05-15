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
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;

public class PurifierManagerTest extends InstrumentationTestCase {
	
	private PurifierManager mPurifierMan;
	private SubscriptionManager mSubscriptionMan;
	private AirPurifierEventListener mEventListener;
	
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String VALID_REMOTEAIRPORTEVENT = "{\"status\":0,\"data\":{\"aqi\":\"1\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30425\",\"psens\":\"1\"}}";
	private static final String VALID_LOCALAIRPORTEVENT = "{\"aqi\":\"3\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30414\",\"psens\":\"1\"}";
	private static final String VALID_LOCALFWEVENT = "{\"name\":\"HCN_DEVGEN\",\"version\":\"26\",\"upgrade\":\"\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		setMockSubscriptionManager();
		
		mEventListener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(mEventListener);
		
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

// ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES ***** 
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
// ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES *****
	
// ***** START TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED ***** 
	public void testReceiveIncorrectRemoteEventNoPurfierSet() {
		String data = "dfasfas";
		mPurifierMan.onRemoteEventReceived(data, PURIFIER_EUI64);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
	}
	
	public void testReceiveIncorrectRemoteEventWrongPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = "dfasfas";
		mPurifierMan.onRemoteEventReceived(data, "9c5a6bfffe634357");
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
	}

	public void testReceiveIncorrectRemoteEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = "dfasfas";
		mPurifierMan.onRemoteEventReceived(data, PURIFIER_EUI64);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
	}
	
	public void testReceiveRemoteAirPortEventNoPurfierSet() {
		String data = VALID_REMOTEAIRPORTEVENT;
		mPurifierMan.onRemoteEventReceived(data, PURIFIER_EUI64);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
	}
	
	public void testReceiveRemoteAirPortEventWrongPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_REMOTEAIRPORTEVENT;
		mPurifierMan.onRemoteEventReceived(data, "9c5a6bfffe634357");
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
	}

	public void testReceiveRemoteAirPortEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_REMOTEAIRPORTEVENT;
		mPurifierMan.onRemoteEventReceived(data, PURIFIER_EUI64);
		
		verify(mEventListener).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNotNull(device.getAirPortInfo());
	}
// ***** END TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED ***** 

}
