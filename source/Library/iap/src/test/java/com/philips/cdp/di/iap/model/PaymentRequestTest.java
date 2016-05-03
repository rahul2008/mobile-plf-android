package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentRequestTest {
    @Mock
    private Store mStore;
    @Mock
    private AddressFields billingAddress;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void parseResponseShouldBeOfMakePaymentData() {
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(GetAddressRequestTest.class, "payment_url.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), MakePaymentData.class);
    }


    @Test
    public void testQueryParamsIsNotNull() {
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        assertNotNull(mockPaymentRequest.requestBody());
    }

    @Test
    public void matchAddressDetailURL() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.ORDER_NUMBER, "H1212");
        PaymentRequest request = new PaymentRequest(mStore, params, null);
        assertEquals(NetworkURLConstants.PAYMENT_SET_URL, request.getUrl());
    }
}