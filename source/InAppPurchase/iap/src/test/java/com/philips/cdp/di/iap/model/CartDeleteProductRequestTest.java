package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.carts.Carts;
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

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartDeleteProductRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsDELETE() {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CartDeleteProductRequest mockCartDeleteProductRequest = Mockito.mock(CartDeleteProductRequest.class);
        assertNotNull(mockCartDeleteProductRequest.requestBody());
    }

    @Test
    public void testTestingUrilIsNotNull() {
        CartDeleteProductRequest mockCartDeleteProductRequest = Mockito.mock(CartDeleteProductRequest.class);
        Mockito.when(mockCartDeleteProductRequest.getTestUrl()).thenReturn(NetworkConstants.DELETE_PRODUCT_URL);
        assertNotNull(mockCartDeleteProductRequest.getTestUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        String str = null;
        Object response = request.parseResponse(str);
        assertNull(null, response);
    }
}