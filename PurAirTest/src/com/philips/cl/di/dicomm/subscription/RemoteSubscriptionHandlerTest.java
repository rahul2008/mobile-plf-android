package com.philips.cl.di.dicomm.subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.subscription.RemoteSubscriptionHandler;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.util.WrappedHandler;
import com.philips.cl.di.dicomm.util.MockitoTestCase;

public class RemoteSubscriptionHandlerTest extends MockitoTestCase {

	private static final String APPLIANCE_IP = "198.168.1.145";
	private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

	private RemoteSubscriptionHandler mRemoteSubscriptionHandler;
	private SubscriptionEventListener mSubscriptionEventListener;
	private NetworkNode mMockNetworkNode;
	private WrappedHandler mSubscriptionEventResponseHandler;
	private CppController mMockCppController;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mSubscriptionEventListener = mock(SubscriptionEventListener.class);
		mMockCppController = mock(CppController.class);
		mMockNetworkNode = mock(NetworkNode.class);
		when(mMockNetworkNode.getIpAddress()).thenReturn(APPLIANCE_IP);
		when(mMockNetworkNode.getCppId()).thenReturn(APPLIANCE_CPPID);

		mRemoteSubscriptionHandler = new RemoteSubscriptionHandlerImpl();

		mSubscriptionEventResponseHandler = mock(WrappedHandler.class);

		mRemoteSubscriptionHandler.enableSubscription(mMockNetworkNode, mSubscriptionEventListener);
	}


	public void testDCSEventReceivedDataNull() {
		mRemoteSubscriptionHandler.onDCSEventReceived(null, APPLIANCE_CPPID, null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}

	public void testDCSEventReceivedDataEmptyString() {
		mRemoteSubscriptionHandler.onDCSEventReceived("", APPLIANCE_CPPID, null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}

	public void testDCSEventReceivedDataRandomString() {
		String expected = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(expected, APPLIANCE_CPPID, null);

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
		mRemoteSubscriptionHandler.onDCSEventReceived(data, APPLIANCE_CPPID, null);

		verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
	}

	public void testDCSEventReceivedWrongEui64() {
		String data = "dfjalsjdfl";
		mRemoteSubscriptionHandler.onDCSEventReceived(data, "0.0.0.0", null);

		verify(mSubscriptionEventResponseHandler,never()).post(any(Runnable.class));
	}

	private class RemoteSubscriptionHandlerImpl extends RemoteSubscriptionHandler {

		public RemoteSubscriptionHandlerImpl() {
			super(mMockCppController);
		}

		@Override
		protected WrappedHandler getSubscriptionEventResponseHandler() {
			return mSubscriptionEventResponseHandler;
		}
	}

}
