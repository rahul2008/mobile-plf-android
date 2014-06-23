package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.PurAirApplication;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;
import com.philips.cl.di.dev.pa.newpurifier.PurifierManager;
import com.philips.cl.di.dev.pa.purifier.AirPurifierEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;

public class PurifierManagerTest extends InstrumentationTestCase {
	
	private PurifierManager mPurifierMan;
	private SubscriptionHandler mSubscriptionMan;
	private AirPurifierEventListener mEventListener;
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";
	
	private static final String VALID_REMOTEAIRPORTEVENT = "{\"status\":0,\"data\":{\"aqi\":\"1\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30425\",\"psens\":\"1\"}}";
	private static final String VALID_LOCALAIRPORTEVENT = "{\"aqi\":\"3\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30414\",\"psens\":\"1\"}";
	private static final String VALID_LOCALFWEVENT = "{\"name\":\"HCN_DEVGEN\",\"version\":\"26\",\"upgrade\":\"\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";
	
	
	private static final String VALID_ENCRYPTED_LOCALAIRPORTVENT = "kFn051qs6876EV0Q2JItzPE+OUKBRnfUMWFLnbCw1B7yWm0YH8cvZ1yRnolygyCqJqPSD1QGaKZzp6jJ53AfQ5H0i/Xl1Ek3cglWuoeAjpWpL0lWv4hcEb3jgc0x3LUnrrurrlsqhj1w8bcuwWUhrxTFSJqKUGr15E3gRGPkE+lyRJpXb2RoDDgjIL7KwS3Zrre45+UEr9udE8tfqSQILhbPqjfm/7I9KefpKEmHoz3uNkDCvUlvnpyja8gWueBa9Z3LeW2DApHWflvNLHnFEOsH3rgD/XJC2dIrBWn1qQM=";
	private static final String VALID_ENCRYPTED_LOCALFWEVENT = "PoS3wsPK8ryos6txCYtPATYd0LjprYGqf5GW93kn5Xx+X32FkvDkKg8TDE0IF32tiNnCQNCa2vEXkR1gIXtargSg+L5pIkWDEQbjCLtaVLyPcdlFAQRxppZjyryFavl8WHxt24kShNUJZ51bJ1l6hUOkgCfVBp1mNAtXkUe3EZnf8cFUWWtdoko99xtwLL9t";
	
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
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
		super.tearDown();
	}
	
	private void setMockSubscriptionManager() {
		mSubscriptionMan = mock(SubscriptionHandler.class);
		SubscriptionHandler.setDummySubscriptionManagerForTesting(mSubscriptionMan);
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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));

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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));

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
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(device2);
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device2);
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
	}
	
	public void testRemovePurifierPairedAfterNoPurifier() {
		setMockSubscriptionManager();
		
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(any(PurAirDevice.class));
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(any(PurAirDevice.class));
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterLocal() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(PurAirApplication.getAppContext());
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterRemoteNotPaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan,never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
	public void testRemovePurifierPairedAfterRemotePaired() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		
		mPurifierMan.removeCurrentPurifier();
		
		verify(mSubscriptionMan,never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan).unSubscribeFromFirmwareEvents(device);
		
		assertNull(mPurifierMan.getCurrentPurifier());
	}
	
// ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CHANGES *****
	
	
// ***** START TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CONNECTIONSTATE CHANGES *****

	public void testPurifierDisconnectedAfterLocal() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		device.setConnectionState(ConnectionState.DISCONNECTED);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		
		verify(listener).onAirPurifierChanged();
	}

	public void testPurifierRemotedAfterLocal() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_LOCALLY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		
		verify(listener).onAirPurifierChanged();
	}
	
	public void testPurifierLocalAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		device.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		
		verify(listener).onAirPurifierChanged();
	}

	public void testPurifierRemoteAfterDisconnected() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		
		verify(listener).onAirPurifierChanged();
	}
	
	public void testPurifierLocalAfterRemote() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.DISCONNECTED);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		device.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		
		verify(listener).onAirPurifierChanged();
	}

	public void testPurifierDisconnectedAfterRemote() {
		PurAirDevice device = new PurAirDevice(null, null, null, null, -1, ConnectionState.CONNECTED_REMOTELY);
		device.setPairing(true);
		mPurifierMan.setCurrentPurifier(device);
		
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		device.setConnectionState(ConnectionState.DISCONNECTED);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).enableRemoteSubscription(PurAirApplication.getAppContext());
		verify(mSubscriptionMan, never()).enableLocalSubscription();
		verify(mSubscriptionMan, never()).disableRemoteSubscription(any(Context.class));
		
		verify(mSubscriptionMan, never()).subscribeToPurifierEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).subscribeToFirmwareEvents(any(PurAirDevice.class));
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		
		verify(listener).onAirPurifierChanged();
	}

// ***** END TESTS TO TOGGLE SUBSCRIPTION WHEN PURIFIER CONNECTIONSTATE CHANGES *****

	
// ***** START TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED ***** 
	
	public void testReceiveIncorrectLocalEventNoPurfierSet() {
		String data = "dfasfas";
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
	}
	
	public void testReceiveIncorrectLocalEventWrongPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = "dfasfas";
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}

	public void testReceiveIncorrectLocalEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = "dfasfas";
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}
	
	public void testReceiveUnencryptedLocalAPEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_LOCALAIRPORTEVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}

	public void testReceiveUnencryptedLocalFWEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_LOCALFWEVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}
	
	public void testReceiveEncryptedLocalAPEventNoPurifierSet() {
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
	}
	
	public void testReceiveEncryptedLocalAPEventWrongPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, "192.1.1.14", null, -1, ConnectionState.CONNECTED_LOCALLY);
		device.setEncryptionKey(PURIFIER_KEY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}
	
	public void testReceiveEncryptedLocalAPEventRightWrongKeyPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		device.setEncryptionKey("bjklsdauionse89084jlk");
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}

	public void testReceiveEncryptedLocalAPEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		device.setEncryptionKey(PURIFIER_KEY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener).onAirPurifierEventReceived();
		verify(mEventListener, never()).onFirmwareEventReceived();
		
		assertNotNull(device.getAirPortInfo());
		assertNull(device.getFirmwarePortInfo());
	}
	
	public void testReceiveEncryptedLocalFWEventRightPurifier() {
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		device.setEncryptionKey(PURIFIER_KEY);
		mPurifierMan.setCurrentPurifier(device);
		
		String data = VALID_ENCRYPTED_LOCALFWEVENT;
		mPurifierMan.onLocalEventReceived(data, PURIFIER_IP);
		
		verify(mEventListener, never()).onAirPurifierEventReceived();
		verify(mEventListener).onFirmwareEventReceived();
		
		assertNull(device.getAirPortInfo());
		assertNotNull(device.getFirmwarePortInfo());
	}
	
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

// ***** START TEST TO START/STOP SUBSCRIPTION WHEN ADDING PURIFIEREVENTLISTENERS *****
	public void testStartSubscriptionAddFirstEventListener() {
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
	}
	
	public void testStartSubscriptionAddSecondEventListener() {
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		setMockSubscriptionManager();
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		
		verify(mSubscriptionMan).enableLocalSubscription();
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
	}
	
	public void testStopSubscriptionRemoveFirstEventListener() {
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		
		setMockSubscriptionManager();
		mPurifierMan.removeAirPurifierEventListener(listener);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
	}
	
	public void testStopSubscriptionRemoveSecondEventListener() {
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		
		setMockSubscriptionManager();
		mPurifierMan.removeAirPurifierEventListener(listener);
		
		verify(mSubscriptionMan, never()).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
	}
	
	public void testStopSubscriptionRemoveBothEventListeners() {
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		AirPurifierEventListener listener = mock(AirPurifierEventListener.class);
		AirPurifierEventListener listener2 = mock(AirPurifierEventListener.class);
		mPurifierMan.addAirPurifierEventListener(listener);
		mPurifierMan.addAirPurifierEventListener(listener2);
		
		setMockSubscriptionManager();
		mPurifierMan.removeAirPurifierEventListener(listener);
		mPurifierMan.removeAirPurifierEventListener(listener2);
		
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
	}
	
	public void testStartStopSubscriptionAddRemoveListenersSequence() {
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		setMockSubscriptionManager();
		
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
		verify(mSubscriptionMan).subscribeToFirmwareEvents(device);
		verify(mSubscriptionMan).subscribeToPurifierEvents(device);
		verify(mSubscriptionMan).disableLocalSubscription();
		verify(mSubscriptionMan, never()).unSubscribeFromFirmwareEvents(device);
		verify(mSubscriptionMan, never()).unSubscribeFromPurifierEvents(device);
	}
	
// ***** END TEST TO START/STOP SUBSCRIPTION WHEN ADDING PURIFIEREVENTLISTENERS *****
	
	public void testDeadlock() {
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
		PurifierManager.setDummyPurifierManagerForTesting(null);
		mPurifierMan = PurifierManager.getInstance();
		PurAirDevice device = new PurAirDevice(PURIFIER_EUI64, null, PURIFIER_IP, null, -1, ConnectionState.CONNECTED_LOCALLY);
		mPurifierMan.setCurrentPurifier(device);
		
		device.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
	}
	
}
