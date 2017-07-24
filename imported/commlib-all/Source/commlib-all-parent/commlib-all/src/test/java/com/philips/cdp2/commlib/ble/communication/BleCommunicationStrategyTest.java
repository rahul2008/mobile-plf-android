/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.communication;

import android.os.Handler;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static com.philips.cdp.dicommclient.util.DICommLog.disableLogging;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleCommunicationStrategyTest {

    private static final String PORTNAME = "thePort";
    private static final int PRODUCT_ID = 0;

    @Mock
    private BleDeviceCache deviceCache;
    @Mock
    private ResponseHandler responseHandler;
    @Mock
    private Handler callbackHandler;

    private BleCommunicationStrategy strategy;

    @Before
    public void setUp() {
        initMocks(this);
        disableLogging();

        strategy = new BleCommunicationStrategy("NCC-1701", deviceCache, callbackHandler);
    }

    @Test
    public void testSubscribe() {
        strategy.subscribe(PORTNAME, PRODUCT_ID, 5000, responseHandler);

        verify(responseHandler).onSuccess(anyString());
    }

    @Test
    public void givenSubscribedWhenSubscribingAgainThenSuccessReturned() {
        strategy.subscribe(PORTNAME, PRODUCT_ID, 5000, mock(ResponseHandler.class));

        strategy.subscribe(PORTNAME, PRODUCT_ID, 5000, responseHandler);

        verify(responseHandler).onSuccess(anyString());
    }

    @Test
    public void testUnsubscribe() {
        strategy.subscribe(PORTNAME, PRODUCT_ID, 5000, mock(ResponseHandler.class));
        strategy.unsubscribe(PORTNAME, PRODUCT_ID, responseHandler);

        verify(responseHandler).onSuccess(anyString());
    }

    @Test
    public void givenNoSubscriptionWhenUnsubscribingThenSuccessReturned() {
        strategy.unsubscribe(PORTNAME, PRODUCT_ID, responseHandler);

        verify(responseHandler).onSuccess(anyString());
    }
}
