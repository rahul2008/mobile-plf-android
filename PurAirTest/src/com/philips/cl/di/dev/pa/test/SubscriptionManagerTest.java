package com.philips.cl.di.dev.pa.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.anyString;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;

public class SubscriptionManagerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_EUI64 = "jsdk984305lks";
	
	private SubscriptionManager mSubscriptionMan;
	private SubscriptionEventListener mSubListener;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		
		SubscriptionManager.setDummySubscriptionManagerForTesting(null);
		mSubscriptionMan = SubscriptionManager.getInstance();
		
		mSubListener = mock(SubscriptionEventListener.class);
		mSubscriptionMan.setSubscriptionListener(mSubListener);
		
		super.setUp();
	}

	protected void tearDown() throws Exception {
		// Reset SubscriptionManager after tests
		SubscriptionManager.setDummySubscriptionManagerForTesting(null);
		super.tearDown();
	}
	
	public void testUDPEventReceivedNull() {
		mSubscriptionMan.onUDPEventReceived(null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedEmptyString() {
		mSubscriptionMan.onUDPEventReceived("");

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testUDPEventReceivedNonDecryptableString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onUDPEventReceived(expected);

		verify(mSubListener).onLocalEventReceived(expected);
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDataNull() {
		mSubscriptionMan.onDCSEventReceived(null, PURIFIER_EUI64);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mSubscriptionMan.onDCSEventReceived("", PURIFIER_EUI64);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(expected, PURIFIER_EUI64);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener).onRemoteEventReceived(expected, PURIFIER_EUI64);
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, null);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, "");

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener, never()).onRemoteEventReceived(anyString(), anyString());
	}
	
	public void testDCSEventReceivedEui64RandomString() {
		String data = "dfjalsjdfl";
		mSubscriptionMan.onDCSEventReceived(data, PURIFIER_EUI64);

		verify(mSubListener, never()).onLocalEventReceived(anyString());
		verify(mSubListener).onRemoteEventReceived(data, PURIFIER_EUI64);
	}
	
}
