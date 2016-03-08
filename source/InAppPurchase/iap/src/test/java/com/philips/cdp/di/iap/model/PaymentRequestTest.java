package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPOST() {
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        Mockito.when(mockPaymentRequest.getTestUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
        assertNotNull(mockPaymentRequest.requestBody());
    }

    @Test
    public void testQueryParamsHasBody() {
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        Mockito.when(mockPaymentRequest.getTestUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
        Map<String, String> params = new HashMap<String, String>();

        assertEquals(mockPaymentRequest.requestBody(), params);
    }

    @Test
    public void testTestingUriIsNotNull() {
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        Mockito.when(mockPaymentRequest.getTestUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
        assertNotNull(mockPaymentRequest.getTestUrl());
    }

    @Test
    public void testTestingUrilIsForPaymentRequest() {
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        Mockito.when(mockPaymentRequest.getTestUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
        assertEquals(mockPaymentRequest.getTestUrl(), NetworkConstants.BASE_URL + "orders/5165165165/pay");
    }

    @Test
    public void parseResponseShouldBeOfPaymentRequestDataType() {
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        Mockito.when(mockPaymentRequest.getTestUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");

        String str = TestUtils.readFile(PaymentRequestTest.class, "payment_url.txt");
        Object response = request.parseResponse(str);
        assertEquals(response.getClass(), MakePaymentData.class);
    }
}