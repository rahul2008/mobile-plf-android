package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.store.Store;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetPaymentDetailRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsGET() {
        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void testTestingUrilIsNotNull() {
        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, null, null);
        assertNotNull(request.getTestUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, null, null);
        String paymentResponse = TestUtils.readFile(GetPaymentDetailRequestTest.class, "get_payment_detail.txt");
        Object response = request.parseResponse(paymentResponse);
        assertEquals(response.getClass(), PaymentMethods.class);
    }
}