/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

import android.os.Handler;

import com.philips.cdp.cloudcontroller.api.CloudController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static java.util.Collections.singleton;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("ConstantConditions")
public class RemoteSubscriptionHandlerTest extends RobolectricTest {

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";

    private static final String dcsData = "{\"testKey\":\"testValue\"}";

    @Mock
    private SubscriptionEventListener subscriptionEventListenerMock;

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private CloudController cloudControllerMock;

    @Mock
    private Handler subscriptionEventResponseHandler;

    @Captor
    ArgumentCaptor<Runnable> runnableCaptor;

    private RemoteSubscriptionHandler remoteSubscriptionHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        initMocks(this);
        HandlerProvider.enableMockedHandler(subscriptionEventResponseHandler);
        DICommLog.disableLogging();

        when(networkNodeMock.getIpAddress()).thenReturn(APPLIANCE_IP);
        when(networkNodeMock.getCppId()).thenReturn(APPLIANCE_CPPID);

        remoteSubscriptionHandler = new RemoteSubscriptionHandler(cloudControllerMock);
        remoteSubscriptionHandler.enableSubscription(networkNodeMock, singleton(subscriptionEventListenerMock));
    }

    @Test
    public void testDCSEventReceivedDataEmptyString() {
        remoteSubscriptionHandler.onDCSEventReceived("", APPLIANCE_CPPID, null);

        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedEui64EmptyString() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, "", null);

        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedWrongEui64() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, "0.0.0.0", null);

        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedRightEui64() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(subscriptionEventResponseHandler).post(runnableCaptor.capture());
    }

    @Test
    public void testShouldContainCorrectData_WhenRunnableIsExecuted() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(subscriptionEventResponseHandler).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedDataValidString() {
        String dcsPayload = createDcsEvent("1", "air", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(subscriptionEventResponseHandler).post(runnableCaptor.capture());
        runnableCaptor.getValue().run();

        verify(subscriptionEventListenerMock).onSubscriptionEventReceived(eq("air"), eq(dcsData));
    }

    @Test
    public void testDCSEventReceivedDataValidErrorString() {
        String dcsPayload = this.createErrorDcsEvent("1", "air", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void testDCSEventReceivedDataContentNullString() {
        String dcsPayload = createDcsEvent("1", "air", null);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void givenHandlerCreated_whenEmptyPortNameAndDataValidString_thenShouldNotCallListener() {
        String dcsPayload = createDcsEvent("1", "", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void givenHandlerCreated_whenEmptyPortNameAndDataInvalidString_thenShouldNotCallListener() {
        String dcsPayload = this.createErrorDcsEvent("1", "", dcsData);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    @Test
    public void givenHandlerCreated_whenEmptyPortNameAndDataNull_thenShouldNotCallListener() {
        String dcsPayload = createDcsEvent("1", "", null);
        remoteSubscriptionHandler.onDCSEventReceived(dcsPayload, APPLIANCE_CPPID, null);

        // Handler shouldn't be called if invalid data is provided
        verify(subscriptionEventResponseHandler, never()).post(any(Runnable.class));
    }

    private String createDcsEvent(String productId, String port, String data) {
        return "{ \"product\": \"" + productId + "\", \"port\":\"" + port + "\", \"data\": " + data + "}";
    }

    private String createErrorDcsEvent(String productId, String port, String data) {
        return "{ \"product\": \"" + productId + "\", \"port\":\"" + port + "\", \"noData\": " + data + "}";
    }
}

