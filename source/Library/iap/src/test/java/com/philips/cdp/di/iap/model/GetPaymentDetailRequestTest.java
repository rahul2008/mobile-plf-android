package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetPaymentDetailRequestTest {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

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
    public void matchAddressDetailURL() {
        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.CART_PAYMENT_DETAILS_URL, request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, null, null);
        String paymentResponse = TestUtils.readFile(GetPaymentDetailRequestTest.class, "get_payment_detail.txt");
        Object response = request.parseResponse(paymentResponse);
        assertEquals(response.getClass(), PaymentMethods.class);
    }
}