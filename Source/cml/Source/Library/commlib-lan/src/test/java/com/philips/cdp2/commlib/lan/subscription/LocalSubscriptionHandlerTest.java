/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.subscription;

import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.security.DISecurity;
import com.philips.cdp.dicommclient.subscription.SubscriptionEventListener;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LocalSubscriptionHandlerTest extends RobolectricTest {

    private static final String APPLIANCE_IP = "198.168.1.145";
    private static final String APPLIANCE_CPPID = "1c5a6bfffe634357";
    private static final String APPLIANCE_KEY = "75B9424B0EA8089428915EB0AA1E4B5E";

    private static final String VALID_ENCRYPTED_LOCAL_AIRPORT_EVENT = "kFn051qs6876EV0Q2JItzPE+OUKBRnfUMWFLnbCw1B7yWm0YH8cvZ1yRnolygyCqJqPSD1QGaKZzp6jJ53AfQ5H0i/Xl1Ek3cglWuoeAjpWpL0lWv4hcEb3jgc0x3LUnrrurrlsqhj1w8bcuwWUhrxTFSJqKUGr15E3gRGPkE+lyRJpXb2RoDDgjIL7KwS3Zrre45+UEr9udE8tfqSQILhbPqjfm/7I9KefpKEmHoz3uNkDCvUlvnpyja8gWueBa9Z3LeW2DApHWflvNLHnFEOsH3rgD/XJC2dIrBWn1qQM=";
    private static final String PORT_NAME = "air";

    @Mock
    private NetworkNode networkNodeMock;

    @Mock
    private DISecurity diSecurityMock;

    @Mock
    private UdpEventReceiver udpEventReceiverMock;

    @Mock
    private SubscriptionEventListener subscriptionEventListenerMock;

    @Mock
    private Handler subscriptionEventResponseHandlerMock;

    private LocalSubscriptionHandler localSubscriptionHandler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        initMocks(this);
        DICommLog.disableLogging();

        HandlerProvider.enableMockedHandler(subscriptionEventResponseHandlerMock);

        when(networkNodeMock.getIpAddress()).thenReturn(APPLIANCE_IP);
        when(networkNodeMock.getCppId()).thenReturn(APPLIANCE_CPPID);
        when(networkNodeMock.getEncryptionKey()).thenReturn(APPLIANCE_KEY);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgument(0);
                runnable.run();

                return null;
            }
        }).when(subscriptionEventResponseHandlerMock).post(any(Runnable.class));

        localSubscriptionHandler = new LocalSubscriptionHandler(diSecurityMock, udpEventReceiverMock);
        localSubscriptionHandler.enableSubscription(networkNodeMock, Collections.singleton(subscriptionEventListenerMock));
    }

    @Test
    public void givenASubscriptionIsEnabledWithoutListeners_whenAUdpEventReceived_thenDontNotify() {
        localSubscriptionHandler.enableSubscription(networkNodeMock, Collections.<SubscriptionEventListener>emptySet());

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void givenASubscriptionIsDisabled_whenAUdpEventReceived_thenDontNotify() {
        localSubscriptionHandler.disableSubscription();

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedDataNull() {
        localSubscriptionHandler.onUDPEventReceived(null, PORT_NAME, APPLIANCE_IP);

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedDataEmptyString() {
        localSubscriptionHandler.onUDPEventReceived("", PORT_NAME, APPLIANCE_IP);

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedDataNonDecryptableString() {
        String expected = "dfjalsjdfl";
        localSubscriptionHandler.onUDPEventReceived(expected, PORT_NAME, APPLIANCE_IP);

        verify(subscriptionEventListenerMock).onSubscriptionEventDecryptionFailed(PORT_NAME);
    }

    @Test
    public void testUDPEventReceivedIpNull() {
        localSubscriptionHandler.onUDPEventReceived(VALID_ENCRYPTED_LOCAL_AIRPORT_EVENT, PORT_NAME, null);

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedIpEmptyString() {
        localSubscriptionHandler.onUDPEventReceived(VALID_ENCRYPTED_LOCAL_AIRPORT_EVENT, PORT_NAME, "");

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEventReceivedWrongIp() {
        localSubscriptionHandler.onUDPEventReceived(VALID_ENCRYPTED_LOCAL_AIRPORT_EVENT, PORT_NAME, "0.0.0.0");

        verify(subscriptionEventResponseHandlerMock, never()).post(any(Runnable.class));
    }

    @Test
    public void testUDPEncryptedAPEvent() {
        String data = VALID_ENCRYPTED_LOCAL_AIRPORT_EVENT;
        when(diSecurityMock.decryptData(data)).thenReturn("don't care");

        localSubscriptionHandler.onUDPEventReceived(data, PORT_NAME, APPLIANCE_IP);

        verify(subscriptionEventListenerMock).onSubscriptionEventReceived(PORT_NAME, "don't care");
    }

    @Test
    public void testUDPEncryptedAPEventWrongKey() {
        when(networkNodeMock.getEncryptionKey()).thenReturn("726783627");
        localSubscriptionHandler.onUDPEventReceived(VALID_ENCRYPTED_LOCAL_AIRPORT_EVENT, PORT_NAME, APPLIANCE_IP);

        verify(subscriptionEventListenerMock).onSubscriptionEventDecryptionFailed(PORT_NAME);
    }
}
