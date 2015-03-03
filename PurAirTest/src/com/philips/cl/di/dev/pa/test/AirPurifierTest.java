package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Observer;

import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.PurifierListener;

public class AirPurifierTest extends InstrumentationTestCase {

	private AirPurifier purifier = null;
	private Observer observer = null;
	private PurifierListener mPurifierListener;
	
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_USN = "uuid:12345678-1234-1234-1234-1c5a6b634357::urn:philips-com:device:DiProduct:1";
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_NAME = "Jeroen_test";
	private static final long PURIFIER_BOOTID = 243;
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
		
		purifier = new AirPurifier(PURIFIER_EUI64, PURIFIER_USN, PURIFIER_IP, PURIFIER_NAME, PURIFIER_BOOTID, ConnectionState.CONNECTED_LOCALLY);
		
		purifier.getNetworkNode().setEncryptionKey(PURIFIER_KEY);
		
		mPurifierListener = mock(PurifierListener.class);
		purifier.setPurifierListener(mPurifierListener);
		
		observer = mock(Observer.class);
		
		super.setUp();
	}
	
	public void testNotifyObserverNoChange() {
		purifier.getNetworkNode().addObserver(observer);
		assertEquals(1,  purifier.getNetworkNode().countObservers());
		verify(observer, never()).update(eq(purifier.getNetworkNode()), anyObject());
	}
	
	public void testNotifyObserverChangeConnectionState() {
		purifier.getNetworkNode().addObserver(observer);
		purifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(observer).update(eq(purifier.getNetworkNode()), anyObject());
	}

	public void testNotifyObserverSameConnectionState() {
		purifier.getNetworkNode().addObserver(observer);
		purifier.getNetworkNode().setConnectionState(purifier.getNetworkNode().getConnectionState());
		verify(observer, never()).update(eq(purifier.getNetworkNode()), anyObject());
	}
	
	public void testNotifyObserverRemoveObserver() {
		purifier.getNetworkNode().addObserver(observer);
		purifier.getNetworkNode().deleteObserver(observer);
		purifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(observer, never()).update(eq(purifier.getNetworkNode()), anyObject());
	}

	public void testNotifyObserverDoubleObserver() {
		purifier.getNetworkNode().addObserver(observer);
		purifier.getNetworkNode().addObserver(observer);
		purifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(observer).update(eq(purifier.getNetworkNode()), anyObject());
	}
	
	// ***** START TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED ***** 
	
		public void testReceiveIncorrectLocalEventNoPurfierSet() {
			String data = "dfasfas";
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
		}
		

		public void testReceiveIncorrectLocalEventWrongPurifier() {
			String data = "dfasfas";
			purifier.onLocalEventReceived(data, "0.0.0.0");
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}

		public void testReceiveIncorrectLocalEventRightPurifier() {
			String data = "dfasfas";
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}
		
		public void testReceiveUnencryptedLocalAPEventRightPurifier() {
			String data = VALID_LOCALAIRPORTEVENT;
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}

		public void testReceiveUnencryptedLocalFWEventRightPurifier() {
			String data = VALID_LOCALFWEVENT;
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}
		
		public void testReceiveEncryptedLocalAPEventWrongPurifier() {
			String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
			purifier.onLocalEventReceived(data, "0.0.0.0");
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}
		
		public void testReceiveEncryptedLocalAPEventRightWrongKeyPurifier() {
			purifier.getNetworkNode().setEncryptionKey("bjklsdauionse89084jlk");
			
			String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}

		public void testReceiveEncryptedLocalAPEventRightPurifier() {
			String data = VALID_ENCRYPTED_LOCALAIRPORTVENT;
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNotNull(purifier.getAirPort().getAirPortInfo());
			assertNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}
		
		public void testReceiveEncryptedLocalFWEventRightPurifier() {
			String data = VALID_ENCRYPTED_LOCALFWEVENT;
			purifier.onLocalEventReceived(data, PURIFIER_IP);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
			assertNotNull(purifier.getFirmwarePort().getFirmwarePortInfo());
		}
		
		public void testReceiveIncorrectRemoteEventWrongPurifier() {
			String data = "dfasfas";
			purifier.onRemoteEventReceived(data, "9c5a6bfffe634357");
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
		}

		public void testReceiveIncorrectRemoteEventRightPurifier() {
			String data = "dfasfas";
			purifier.onRemoteEventReceived(data, PURIFIER_EUI64);
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
		}
		
		public void testReceiveRemoteAirPortEventWrongPurifier() {
			String data = VALID_REMOTEAIRPORTEVENT;
			purifier.onRemoteEventReceived(data, "9c5a6bfffe634357");
			
			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNull(purifier.getAirPort().getAirPortInfo());
		}

		public void testReceiveRemoteAirPortEventRightPurifier() {
			String data = VALID_REMOTEAIRPORTEVENT;
			purifier.onRemoteEventReceived(data, PURIFIER_EUI64);
			
			verify(mPurifierListener).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();
			
			assertNotNull(purifier.getAirPort().getAirPortInfo());
		}
	// ***** END TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED ***** 

	

}