package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.*;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.cpp.CppDiscoverEventListener;
import com.philips.cl.di.dev.pa.datamodel.DiscoverInfo;
import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;
import com.philips.cl.di.dev.pa.util.DataParser;

public class SubscriptionHandlerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	
	private SubscriptionHandler mSubscriptionMan;
	private SubscriptionEventListener mSubListener;
	private CppDiscoverEventListener mDiscListener;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		SubscriptionHandler.setDummySubscriptionManagerForTesting(null);
		mSubscriptionMan = SubscriptionHandler.getInstance();
		
		mSubListener = mock(SubscriptionEventListener.class);
		mSubscriptionMan.setSubscriptionListener(mSubListener);
		mDiscListener = mock(CppDiscoverEventListener.class);
		mSubscriptionMan.setCppDiscoverListener(mDiscListener);
		
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
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testUDPEventReceivedDataEmptyString() {
		mSubscriptionMan.onUDPEventReceived("", PURIFIER_IP);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testUDPEventReceivedDataNonDecryptableString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubListener).onLocalEventReceived(expected, PURIFIER_IP);
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testUDPEventReceivedIpNull() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testUDPEventReceivedIpEmptyString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, "");

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testUDPEventReceivedIpValid() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected, PURIFIER_IP);

		verify(mSubListener).onLocalEventReceived(expected, PURIFIER_IP);
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedDataNull() {
		mSubscriptionMan.onDCSEventReceived(null, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mSubscriptionMan.onDCSEventReceived("", PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(expected, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener).onRemoteEventReceived(expected, PURIFIER_EUI64);
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, null, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, "", null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedEui64RandomString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener).onRemoteEventReceived(data, PURIFIER_EUI64);
		verify(mDiscListener, never()).onDiscoverEventReceived(anyString(), anyBoolean());
	}
	
	public void testDCSEventReceivedDiscover() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, "CHANGE");

		verify(mDiscListener).onDiscoverEventReceived(data, false);
		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, "DISCOVER");

		verify(mDiscListener).onDiscoverEventReceived(data, true);
		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverEui64Null() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mSubscriptionMan.onDCSEventReceived(data, null, "CHANGE");

		verify(mDiscListener).onDiscoverEventReceived(data, false);
		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverEui64NullRequested() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mSubscriptionMan.onDCSEventReceived(data, null, "DISCOVER");

		verify(mDiscListener).onDiscoverEventReceived(data, true);
		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverActionNull() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mDiscListener).onDiscoverEventReceived(data, false);
		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDiscoverActionEmpty() {
		String data = "{\"State\":\"Connected\",\"ClientIds\":[\"1c5a6bfffe63436c\",\"1c5a6bfffe634357\"]}";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mDiscListener).onDiscoverEventReceived(data, false);
		verify(mSubListener, never()).onLocalEventReceived(anyString(), anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
}
