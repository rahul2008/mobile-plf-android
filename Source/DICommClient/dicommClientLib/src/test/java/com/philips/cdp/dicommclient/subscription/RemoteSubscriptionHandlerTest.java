/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.dicommclient.cpp.CppController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.WrappedHandler;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RemoteSubscriptionHandlerTest extends RobolectricTest {

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

    public static final String dscData = "{\"testKey\":\"testValue\"}";
    public static final String dscResponse = "{\"data\":" + dscData + "}";
    public static final String dscResponseError = "{\"noData\":" + dscData + "}";
    public static final String dscResponseNullData = "{}";

    private RemoteSubscriptionHandler mRemoteSubscriptionHandler;
    private SubscriptionEventListener mSubscriptionEventListener;
    private NetworkNode mMockNetworkNode;
    private WrappedHandler mSubscriptionEventResponseHandler;
    private CppController mMockCppController;

    @Captor
    ArgumentCaptor<Runnable> runnableCaptor;

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

    @Test
    public void testDCSEventReceivedDataNull() {
        mRemoteSubscriptionHandler.onDCSEventReceived(null, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedDataEmptyString() {
        mRemoteSubscriptionHandler.onDCSEventReceived("", APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedEui64Null() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponse, null, null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedEui64EmptyString() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponse, "", null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedWrongEui64() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponse, "0.0.0.0", null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedRightEui64() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponse, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(runnableCaptor.capture());
    }

    @Test
    public void testShouldContainCorrectData_WhenRunnableIsExecuted() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponse, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedDataValidString() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponse, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(mSubscriptionEventListener).onSubscriptionEventReceived(dscData);
    }

    @Test
    public void testDCSEventReceivedDataValidErrorString() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponseError, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(mSubscriptionEventListener).onSubscriptionEventReceived(startsWith("Error"));
    }

    @Test
    public void testDCSEventReceivedDataContentNullString() {
        mRemoteSubscriptionHandler.onDCSEventReceived(dscResponseNullData, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(mSubscriptionEventListener).onSubscriptionEventReceived(startsWith("Error"));
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

