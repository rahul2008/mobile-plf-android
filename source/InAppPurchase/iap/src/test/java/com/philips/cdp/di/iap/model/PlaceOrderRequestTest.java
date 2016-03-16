package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
public class PlaceOrderRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPOST() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        assertNotNull(request.requestBody());
    }

    @Test
    public void testQueryParamsHasBody() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        String cartNumber = CartModelContainer.getInstance().getCartNumber();
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.CART_ID, cartNumber);
        assertEquals(request.requestBody(), params);
    }

    @Test
    public void testTestingUrilIsNotNull() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        assertNotNull(request.getUrl());
    }

    @Test
    public void testTestingUrilIsForPlaceOrder() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
//        assertEquals(request.getUrl(), NetworkConstants.PLACE_ORDER_URL);
    }

    @Test
    public void parseResponseShouldBeOfPlaceOrderRequestDataType() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(PlaceOrderRequestTest.class, "place_order.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), PlaceOrder.class);
    }
}