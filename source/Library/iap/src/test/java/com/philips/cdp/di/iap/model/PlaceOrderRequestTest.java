package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
public class PlaceOrderRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

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
    public void matchPlaceOrderURL() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.PLACE_ORDER_URL, request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfPlaceOrderRequestDataType() {
        PlaceOrderRequest request = new PlaceOrderRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(PlaceOrderRequestTest.class, "place_order.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), PlaceOrder.class);
    }
}