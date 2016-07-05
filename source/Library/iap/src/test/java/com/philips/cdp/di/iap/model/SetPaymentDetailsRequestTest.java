package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class SetPaymentDetailsRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }


    @Test
    public void testQueryParamsHasBody() {
        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkURLConstants.PAYMENT_DETAILS_URL);
        Map<String, String> params = new HashMap<String, String>();
        assertEquals(mockSetPaymentDetailsRequest.requestBody(), params);
    }

    //    @Test
//    public void testQueryParamsWithPaymentDetailId() {
//        Map<String, String> params = new HashMap<>();
//        params.put(ModelConstants.PAYMENT_DETAILS_ID, ModelConstants.PAYMENT_DETAILS_ID);
//        GetPaymentDetailRequest request = new GetPaymentDetailRequest(mStore, params, null);
//        assertEquals(request.requestBody(), params);
//    }
    @Test
    public void testQueryParamsWithPaymentDetailId() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, ModelConstants.PAYMENT_DETAILS_ID);
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, params, null);
        assertEquals(request.requestBody(), params);
    }

    @Test
    public void testTestingUriIsNotNull() {
        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkURLConstants.PAYMENT_DETAILS_URL);
        assertNotNull(mockSetPaymentDetailsRequest.getUrl());
    }

    @Test
    public void testTestingUrilIsForSetPaymentDetailsRequest() {
        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkURLConstants.PAYMENT_DETAILS_URL);
        assertEquals(mockSetPaymentDetailsRequest.getUrl(), NetworkURLConstants.PAYMENT_DETAILS_URL);
    }

    @Test
    public void parseResponseShouldBeOfSetPaymentDetailsRequestDataType() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        String str = null;
        Object response = request.parseResponse(str);
        assertNull(null, response);
    }

    @Test
    public void matchAddressDetailURL() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.PAYMENT_DETAILS_URL, request.getUrl());
    }
}