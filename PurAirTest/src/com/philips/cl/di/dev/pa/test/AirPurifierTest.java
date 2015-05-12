package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;
import java.util.Observer;

import android.test.InstrumentationTestCase;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.ConnectionState;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cl.di.dev.pa.newpurifier.AirPurifier;

public class AirPurifierTest extends InstrumentationTestCase {

	private AirPurifier mPurifier = null;
	private Observer mObserver = null;
	private DICommPortListener mAirPortListener;
	private DICommPortListener mFirmwarePortListener;

	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
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

		mPurifier = new AirPurifier(networkNode, mock(CommunicationStrategy.class));

		mAirPortListener = mock(DICommPortListener.class);
		mFirmwarePortListener = mock(DICommPortListener.class);

		mPurifier.getAirPort().registerPortListener(mAirPortListener);
		mPurifier.getFirmwarePort().registerPortListener(mFirmwarePortListener);

		mObserver = mock(Observer.class);

		super.setUp();
	}

	public void testNotifyObserverNoChange() {
		mPurifier.getNetworkNode().addObserver(mObserver);
		assertEquals(1,  mPurifier.getNetworkNode().countObservers());
		verify(mObserver, never()).update(eq(mPurifier.getNetworkNode()), anyObject());
	}

	public void testNotifyObserverChangeConnectionState() {
		mPurifier.getNetworkNode().addObserver(mObserver);
		mPurifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(mObserver).update(eq(mPurifier.getNetworkNode()), anyObject());
	}

	public void testNotifyObserverSameConnectionState() {
		mPurifier.getNetworkNode().addObserver(mObserver);
		mPurifier.getNetworkNode().setConnectionState(mPurifier.getNetworkNode().getConnectionState());
		verify(mObserver, never()).update(eq(mPurifier.getNetworkNode()), anyObject());
	}

	public void testNotifyObserverRemoveObserver() {
		mPurifier.getNetworkNode().addObserver(mObserver);
		mPurifier.getNetworkNode().deleteObserver(mObserver);
		mPurifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(mObserver, never()).update(eq(mPurifier.getNetworkNode()), anyObject());
	}

	public void testNotifyObserverDoubleObserver() {
		mPurifier.getNetworkNode().addObserver(mObserver);
		mPurifier.getNetworkNode().addObserver(mObserver);
		mPurifier.getNetworkNode().setConnectionState(ConnectionState.CONNECTED_REMOTELY);
		verify(mObserver).update(eq(mPurifier.getNetworkNode()), anyObject());
	}

	// ***** START TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED *****

		public void testReceiveIncorrectEvent(){
			String data = "dfasfas";
			mPurifier.onSubscriptionEventReceived(data);

			verify(mAirPortListener, never()).onPortUpdate(any(DICommPort.class));
			verify(mFirmwarePortListener, never()).onPortUpdate(any(DICommPort.class));

			assertNull(mPurifier.getAirPort().getPortProperties());
			assertNull(mPurifier.getFirmwarePort().getPortProperties());
		}

		public void testReceiveValidLocalAPEvent() {
			String data = VALID_LOCALAIRPORTEVENT;
			mPurifier.onSubscriptionEventReceived(data);

			verify(mAirPortListener).onPortUpdate(any(DICommPort.class));
			verify(mFirmwarePortListener, never()).onPortUpdate(any(DICommPort.class));

			assertNotNull(mPurifier.getAirPort().getPortProperties());
			assertNull(mPurifier.getFirmwarePort().getPortProperties());
		}

		public void testReceiveValidLocalFWEvent() {
			String data = VALID_LOCALFWEVENT;
			mPurifier.onSubscriptionEventReceived(data);

			verify(mAirPortListener, never()).onPortUpdate(any(DICommPort.class));
			verify(mFirmwarePortListener).onPortUpdate(any(DICommPort.class));

			assertNull(mPurifier.getAirPort().getPortProperties());
			assertNotNull(mPurifier.getFirmwarePort().getPortProperties());
		}


		public void testReceiveValidRemoteAPEvent() {
			String data = VALID_REMOTEAIRPORTEVENT;
			mPurifier.onSubscriptionEventReceived(data);

			verify(mAirPortListener).onPortUpdate(any(DICommPort.class));
			verify(mFirmwarePortListener, never()).onPortUpdate(any(DICommPort.class));

			assertNotNull(mPurifier.getAirPort().getPortProperties());
		}
	// ***** END TESTS TO UPDATE PURIFIER WHEN SUBSCRIPTION EVENT RECEIVED *****



}