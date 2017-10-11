/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.WrappedHandler;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static java.util.Collections.singleton;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ConstantConditions")
public class RemoteSubscriptionHandlerTest extends RobolectricTest {

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

    private static final String dcsData = "{\"testKey\":\"testValue\"}";

    private RemoteSubscriptionHandler mRemoteSubscriptionHandler;
    private SubscriptionEventListener mSubscriptionEventListener;
    private NetworkNode mMockNetworkNode;
    private WrappedHandler mSubscriptionEventResponseHandler;
    private CloudController mMockCloudController;

    @Captor
    ArgumentCaptor<Runnable> runnableCaptor;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mSubscriptionEventListener = mock(SubscriptionEventListener.class);
        mMockCloudController = mock(DefaultCloudController.class);
        mMockNetworkNode = mock(NetworkNode.class);
        when(mMockNetworkNode.getIpAddress()).thenReturn(APPLIANCE_IP);
        when(mMockNetworkNode.getCppId()).thenReturn(APPLIANCE_CPPID);

        mRemoteSubscriptionHandler = createMockedRemoteSubscriptionHandler();

        mSubscriptionEventResponseHandler = mock(WrappedHandler.class);

        mRemoteSubscriptionHandler.enableSubscription(mMockNetworkNode, singleton(mSubscriptionEventListener));
    }

    @Test
    public void testDCSEventReceivedDataEmptyString() {
        mRemoteSubscriptionHandler.onDCSEventReceived("", APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedEui64EmptyString() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, "", null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedWrongEui64() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, "0.0.0.0", null);

        verify(mSubscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedRightEui64() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(runnableCaptor.capture());
    }

    @Test
    public void testShouldContainCorrectData_WhenRunnableIsExecuted() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedDataValidString() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(mSubscriptionEventListener).onSubscriptionEventReceived(eq("air"), eq(dcsData));
    }

    @Test
    public void testDCSEventReceivedDataValidErrorString() {
        String dcsPayload = this.createErrorDcsEvent("1", "air", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(mSubscriptionEventResponseHandler, never()).post(runnableCaptor.capture());
    }

    @Test
    public void testDCSEventReceivedDataContentNullString() {
        String dcsPayload = createDcsEvent("1", "air", null);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(mSubscriptionEventResponseHandler, never()).post(runnableCaptor.capture());
    }

    @Test
    public void givenHandlerCreated_whenEmptyPortNameAndDataValidString_thenShouldNotCallListener() {
        String dcsPayload = createDcsEvent("1", "", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(mSubscriptionEventResponseHandler, never()).post(runnableCaptor.capture());
    }

    @Test
    public void givenHandlerCreated_whenEmptyPortNameAndDataInvalidString_thenShouldNotCallListener() {
        String dcsPayload = this.createErrorDcsEvent("1", "", dcsData);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(mSubscriptionEventResponseHandler, never()).post(runnableCaptor.capture());
    }

    @Test
    public void givenHandlerCreated_whenEmptyPortNameAndDataNull_thenShouldNotCallListener() {
        String dcsPayload = createDcsEvent("1", "", null);
        mRemoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(mSubscriptionEventResponseHandler, never()).post(runnableCaptor.capture());
    }

    // TODO Add tests where port name or product ID is invalid (null or "")

    private String createDcsEvent(String productId, String port, String data) {
        return "{ \"product\": \""+productId+"\", \"port\":\""+port+"\", \"data\": "+data+"}";
    }

    private String createErrorDcsEvent(String productId, String port, String data) {
        return "{ \"product\": \""+productId+"\", \"port\":\""+port+"\", \"noData\": "+data+"}";
    }

    private RemoteSubscriptionHandler createMockedRemoteSubscriptionHandler() {
        return new RemoteSubscriptionHandler(mMockCloudController) {
            @Override
            protected WrappedHandler getSubscriptionEventResponseHandler() {
                return mSubscriptionEventResponseHandler;
            }
        };
    }
}

