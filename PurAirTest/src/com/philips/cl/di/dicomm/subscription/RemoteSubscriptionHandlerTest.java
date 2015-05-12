package com.philips.cl.di.dicomm.subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dicomm.communication.RemoteSubscriptionHandler;
import com.philips.cl.di.dicomm.communication.SubscriptionEventListener;
import com.philips.cl.di.dicomm.util.MockitoTestCase;
import com.philips.cl.di.dicomm.util.WrappedHandler;

public class RemoteSubscriptionHandlerTest extends MockitoTestCase {
	
	private static final String PURIFIER_IP = "198.168.1.145";
	private static final String PURIFIER_EUI64 = "1c5a6bfffe634357";
	private static final String PURIFIER_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";
	
	private RemoteSubscriptionHandler mRemoteSubscriptionHandler;
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mNetworkNode;
	private WrappedHandler mSubscriptionEventResponseHandler;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mSubscriptionEventListener = mock(SubscriptionEventListener.class);
		mNetworkNode = mock(NetworkNode.class);
		when(mNetworkNode.getIpAddress()).thenReturn(PURIFIER_IP);		
		when(mNetworkNode.getCppId()).thenReturn(PURIFIER_EUI64);
		when(mNetworkNode.getEncryptionKey()).thenReturn(PURIFIER_KEY);
		
		mRemoteSubscriptionHandler = new RemoteSubscriptionHandlerImpl();
		
		mSubscriptionEventResponseHandler = mock(WrappedHandler.class);
		
		mRemoteSubscriptionHandler.enableSubscription(mNetworkNode, mSubscriptionEventListener);
	}

		
	public void testDCSEventReceivedDataNull() {
		mRemoteSubscriptionHandler.onDCSEventReceived(null, PURIFIER_EUI64, null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}
	
	public void testDCSEventReceivedDataEmptyString() {
		mRemoteSubscriptionHandler.onDCSEventReceived("", PURIFIER_EUI64, null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}
	
	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(expected, PURIFIER_EUI64, null);

		verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
	}
	
	public void testDCSEventReceivedEui64Null() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, null, null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}
	
	public void testDCSEventReceivedEui64EmptyString() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, "", null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}
	
	public void testDCSEventReceivedRightEui64() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, PURIFIER_EUI64, null);

		verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
	}
	
	public void testDCSEventReceivedWrongEui64() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, "0.0.0.0", null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}
	
	private class RemoteSubscriptionHandlerImpl extends RemoteSubscriptionHandler {
		
		@Override
		protected WrappedHandler getSubscriptionEventResponseHandler() {
			return mSubscriptionEventResponseHandler;
		}
	}
	
}
