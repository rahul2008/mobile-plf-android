package com.philips.cl.di.dicomm.subscription;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.RemoteSuscriptionHandler;
import com.philips.cl.di.dicomm.communication.SubscriptionEventListener;

public class RemoteSubscriptionHandlerTest extends InstrumentationTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";
	
	private RemoteSuscriptionHandler mRemoteSubscriptionHandler;
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	
	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

		mSubscriptionEventListener = mock(SubscriptionEventListener.class);
		mNetworkNode = mock(NetworkNode.class);
		when(mNetworkNode.getIpAddress()).thenReturn(PURIFIER_IP);		
		when(mNetworkNode.getCppId()).thenReturn(PURIFIER_EUI64);
		when(mNetworkNode.getEncryptionKey()).thenReturn(PURIFIER_KEY);
		
		mRemoteSubscriptionHandler = new RemoteSuscriptionHandler();
		mRemoteSubscriptionHandler.registerSubscriptionListener(mSubscriptionEventListener);
		mRemoteSubscriptionHandler.enableSubscription(mNetworkNode);
		super.setUp();
	}

		
	public void testDCSEventReceivedDataNull() {
		mRemoteSubscriptionHandler.onDCSEventReceived(null, PURIFIER_EUI64, null);

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mRemoteSubscriptionHandler.onDCSEventReceived("", PURIFIER_EUI64, null);

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(expected, PURIFIER_EUI64, null);

		verify(mSubscriptionEventListener).onSubscriptionEventReceived(expected);
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, null, null);

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, "", null);

		verify(mSubscriptionEventListener, never()).onSubscriptionEventReceived(anyString());
	}
	
	public void testDCSEventReceivedRightEui64() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mSubscriptionEventListener).onSubscriptionEventReceived(data);
	}
	
	public void testDCSEventReceivedWrongEui64() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, "0.0.0.0", null);

		verify(mSubscriptionEventListener,never()).onSubscriptionEventReceived(data);
	}
	
	
}
