package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;

public class SubscriptionHandlerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	
	private SubscriptionHandler mSubscriptionMan;
	private SubscriptionEventListener mSubListener;
	private NetworkNode mNetworkNode;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		mSubListener = mock(SubscriptionEventListener.class);
		mNetworkNode = mock(NetworkNode.class);
		when(mNetworkNode.getIpAddress()).thenReturn(PURIFIER_IP);		
		when(mNetworkNode.getCppId()).thenReturn(PURIFIER_EUI64);
		
		mSubscriptionMan = new SubscriptionHandler(mNetworkNode, mSubListener);
		super.setUp();
	}

	public void testUDPEventReceivedDataNull() {
		mSubscriptionMan.onUDPEventReceived(null, PURIFIER_IP);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testUDPEventReceivedDataEmptyString() {
		mSubscriptionMan.onUDPEventReceived("", PURIFIER_IP);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testUDPEventReceivedDataNonDecryptableString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubListener).onLocalEventReceived(expected);
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testUDPEventReceivedIpNull() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testUDPEventReceivedIpEmptyString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, "");

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testUDPEventReceivedRightIp() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubListener).onLocalEventReceived(expected);
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testUDPEventReceivedWrongIp() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, "0.0.0.0");

		verify(mSubListener, never()).onLocalEventReceived(expected);
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testDCSEventReceivedDataNull() {
		mSubscriptionMan.onDCSEventReceived(null, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mSubscriptionMan.onDCSEventReceived("", PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(expected, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener).onRemoteEventReceived(expected);
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, null, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, "", null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString());
	}
	
	public void testDCSEventReceivedRightEui64() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener).onRemoteEventReceived(data);
	}
	
	public void testDCSEventReceivedWrongEui64() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, "0.0.0.0", null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener,never()).onRemoteEventReceived(data);
	}
}
