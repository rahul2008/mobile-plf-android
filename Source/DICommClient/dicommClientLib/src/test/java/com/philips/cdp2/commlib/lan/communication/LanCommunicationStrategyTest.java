/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

/*
 * (C) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.communication;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.request.ExchangeKeyRequest;
import com.philips.cdp.dicommclient.request.GetKeyRequest;
import com.philips.cdp.dicommclient.request.RequestQueue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LanCommunicationStrategyTest {

    public static final String PORT_NAME = "test";
    public static final int PRODUCT_ID = 1;
    public static final int SUBSCRIPTION_TTL = 10;
    @Mock
    NetworkNode networkNodeMock;

    @Mock
    RequestQueue requestQueueMock;

    LanCommunicationStrategy lanCommunicationStrategy;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        lanCommunicationStrategy = new LanCommunicationStrategy(networkNodeMock) {
            @Override
            RequestQueue createRequestQueue() {
                return requestQueueMock;
            }
        };
    }

    @Test
    public void whenSubscribingViaHttpAndNoKeyPresent_ThenKeyIsExchangeIsStarted() throws Exception {
        when(networkNodeMock.getHttps()).thenReturn(false);

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(ExchangeKeyRequest.class));
    }

    @Test
    public void whenSubscribingViaHttpAndKeyPresent_ThenNoKeyIsExchangeIsStarted() throws Exception {
        when(networkNodeMock.getHttps()).thenReturn(false);
        when(networkNodeMock.getEncryptionKey()).thenReturn("tha_key");

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }

    @Test
    public void whenSubscribingViaHttpsAndNoKeyPresent_ThenKeyRetrievalIsStarted() throws Exception {
        when(networkNodeMock.getHttps()).thenReturn(true);

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock).addRequestInFrontOfQueue(isA(GetKeyRequest.class));
    }

    @Test
    public void whenSubscribingViaHttpsAndKeyPresent_ThenNoKeyRetrievalIsStarted() throws Exception {
        when(networkNodeMock.getHttps()).thenReturn(true);
        when(networkNodeMock.getEncryptionKey()).thenReturn("tha_key");

        lanCommunicationStrategy.subscribe(PORT_NAME, PRODUCT_ID, SUBSCRIPTION_TTL, null);

        verify(requestQueueMock, never()).addRequestInFrontOfQueue(any(LanRequest.class));
    }
}