package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetPaymentDetailsRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPOST() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }

//    @Test
//    public void testQueryParamsIsNotNull() {
//        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
//        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkConstants.SET_PAYMENT_DETAILS_URL);
//        assertNotNull(mockSetPaymentDetailsRequest.requestBody());
//    }

//    @Test
//    public void testQueryParamsHasBody() {
//        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
//        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkConstants.SET_PAYMENT_DETAILS_URL);
//        Map<String, String> params = new HashMap<String, String>();
//
//        assertEquals(mockSetPaymentDetailsRequest.requestBody(), params);
//    }

//    @Test
//    public void testTestingUriIsNotNull() {
//        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
//        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkConstants.SET_PAYMENT_DETAILS_URL);
//        assertNotNull(mockSetPaymentDetailsRequest.getUrl());
//    }

//    @Test
//    public void testTestingUrilIsForSetPaymentDetailsRequest() {
//        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
//        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkConstants.SET_PAYMENT_DETAILS_URL);
//        assertEquals(mockSetPaymentDetailsRequest.getUrl(), NetworkConstants.SET_PAYMENT_DETAILS_URL);
//    }

    @Test
    public void parseResponseShouldBeOfSetPaymentDetailsRequestDataType() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        String str = null;
        Object response = request.parseResponse(str);
        assertNull(null, response);
    }
}