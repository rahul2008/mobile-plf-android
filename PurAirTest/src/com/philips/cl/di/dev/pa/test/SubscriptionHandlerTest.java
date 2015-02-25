package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;

public class SubscriptionHandlerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	
	private SubscriptionHandler mSubscriptionMan;
	private SubscriptionEventListener mSubListener;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
		mSubscriptionMan = SubscriptionHandler.getInstance(null);
		
		mSubListener = mock(SubscriptionEventListener.class);
		mSubscriptionMan.setSubscriptionListener(mSubListener);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		// Reset SubscriptionManager after tests
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
		super.tearDown();
	}
	
	public void testUDPEventReceivedDataNull() {
		mSubscriptionMan.onUDPEventReceived(null, PURIFIER_IP);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedDataEmptyString() {
		mSubscriptionMan.onUDPEventReceived("", PURIFIER_IP);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedDataNonDecryptableString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubListener).onLocalEventReceived(expected, PURIFIER_IP);
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedIpNull() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedIpEmptyString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, "");

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedIpValid() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubListener).onLocalEventReceived(expected, PURIFIER_IP);
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDataNull() {
		mSubscriptionMan.onDCSEventReceived(null, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mSubscriptionMan.onDCSEventReceived("", PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(expected, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener).onRemoteEventReceived(expected, PURIFIER_EUI64);
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, null, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, "", null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedEui64RandomString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener).onRemoteEventReceived(data, PURIFIER_EUI64);
	}
	
}
