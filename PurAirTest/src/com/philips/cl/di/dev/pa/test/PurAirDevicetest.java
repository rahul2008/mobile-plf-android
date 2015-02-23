package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Observer;

import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.PurAirDevice;

public class PurAirDevicetest extends InstrumentationTestCase {

	private PurAirDevice purifier = null;
	private Observer observer = null;
	
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_USN = "uuid:12345678-1234-1234-1234-1c5a6b634357::urn:philips-com:device:DiProduct:1";
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_NAME = "Jeroen_test";
	private static final long PURIFIER_BOOTID = 243;
	private static final String PURIFIER_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		purifier = new PurAirDevice(PURIFIER_EUI64, PURIFIER_USN, PURIFIER_IP, PURIFIER_NAME, PURIFIER_BOOTID, ConnectionState.CONNECTED_LOCALLY);
		purifier.getNetworkNode().setEncryptionKey(PURIFIER_KEY);
		observer = mock(Observer.class);
		
		super.setUp();
	}
	
	public void testNotifyObserverNoChange() {
		purifier.addObserver(observer);
		assertEquals(1,  purifier.countObservers());
		verify(observer, never()).update(eq(purifier), anyObject());
	}
	
	public void testNotifyObserverChangeConnectionState() {
		purifier.addObserver(observer);
		purifier.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(observer).update(eq(purifier), anyObject());
	}

	public void testNotifyObserverSameConnectionState() {
		purifier.addObserver(observer);
		purifier.setConnectionState(purifier.getNetworkNode().getConnectionState());
		verify(observer, never()).update(eq(purifier), anyObject());
	}
	
	public void testNotifyObserverRemoveObserver() {
		purifier.addObserver(observer);
		purifier.deleteObserver(observer);
		purifier.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(observer, never()).update(eq(purifier), anyObject());
	}

	public void testNotifyObserverDoubleObserver() {
		purifier.addObserver(observer);
		purifier.addObserver(observer);
		purifier.setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(observer).update(eq(purifier), anyObject());
	}
	

}