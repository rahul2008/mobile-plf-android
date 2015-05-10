package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Observer;

import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;
import com.philips.cl.di.dev.pa.newpurifier.ConnectionState;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.newpurifier.PurifierListener;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class AirPurifierTest extends InstrumentationTestCase {

	private AirPurifier purifier = null;
	private Observer observer = null;
	private PurifierListener mPurifierListener;

	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_USN = "uuid:12345678-1234-1234-1234-1c5a6b634357::urn:philips-com:device:DiProduct:1";
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_NAME = "Jeroen_test";
	private static final long PURIFIER_BOOTID = 243;

	private static final String VALID_REMOTEAIRPORTEVENT = "{\"status\":0,\"data\":{\"aqi\":\"1\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30425\",\"psens\":\"1\"}}";
	private static final String VALID_LOCALAIRPORTEVENT = "{\"aqi\":\"3\",\"om\":\"s\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"0\",\"fs1\":\"33\",\"fs2\":\"881\",\"fs3\":\"2801\",\"fs4\":\"2801\",\"dtrs\":\"0\",\"aqit\":\"30\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"s\",\"tfav\":\"30414\",\"psens\":\"1\"}";
	private static final String VALID_LOCALFWEVENT = "{\"name\":\"HCN_DEVGEN\",\"version\":\"26\",\"upgrade\":\"\",\"state\":\"idle\",\"progress\":0,\"statusmsg\":\"\",\"mandatory\":false}";





	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		NetworkNode networkNode = new NetworkNode();
        networkNode.setBootId(PURIFIER_BOOTID);
        networkNode.setCppId(PURIFIER_EUI64);
        networkNode.setIpAddress(PURIFIER_IP);
        networkNode.setName(PURIFIER_NAME);
        networkNode.setConnectionState(ConnectionState.CONNECTED_LOCALLY);
		
		purifier = new AirPurifier(networkNode, mock(CommunicationStrategy.class));

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

		public void testReceiveIncorrectEvent(){
			String data = "dfasfas";
			purifier.onSuccess(data);

			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();

			assertNull(purifier.getAirPort().getPortProperties());
			assertNull(purifier.getFirmwarePort().getPortProperties());
		}

		public void testReceiveValidLocalAPEvent() {
			String data = VALID_LOCALAIRPORTEVENT;
			purifier.onSuccess(data);

			verify(mPurifierListener).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();

			assertNotNull(purifier.getAirPort().getPortProperties());
			assertNull(purifier.getFirmwarePort().getPortProperties());
		}

		public void testReceiveValidLocalFWEvent() {
			String data = VALID_LOCALFWEVENT;
			purifier.onSuccess(data);

			verify(mPurifierListener, never()).notifyAirPurifierEventListeners();
			verify(mPurifierListener).notifyFirmwareEventListeners();

			assertNull(purifier.getAirPort().getPortProperties());
			assertNotNull(purifier.getFirmwarePort().getPortProperties());
		}


		public void testReceiveValidRemoteAPEvent() {
			String data = VALID_REMOTEAIRPORTEVENT;
			purifier.onSuccess(data);

			verify(mPurifierListener).notifyAirPurifierEventListeners();
			verify(mPurifierListener, never()).notifyFirmwareEventListeners();

			assertNotNull(purifier.getAirPort().getPortProperties());
		}
	// ***** END TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED *****



}