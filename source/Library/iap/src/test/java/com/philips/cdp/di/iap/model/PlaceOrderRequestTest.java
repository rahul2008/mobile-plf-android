/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class PlaceOrderRequestTest {
    @Mock
    private StoreSpec mStore;
    private AbstractModel request;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPDependencies());
        mStore.initStoreConfig("en", "us", null);
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.SECURITY_CODE, "122");
        request = new PlaceOrderRequest(mStore, params, null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        assertNotNull(request.requestBody());
    }

    @Test
    public void testQueryParamsHasBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.SECURITY_CODE, "122");
        params.put(ModelConstants.CART_ID, "current");
        assertEquals(request.requestBody(), params);
    }

    @Test
    public void matchPlaceOrderURL() {
        assertEquals(NetworkURLConstants.PLACE_ORDER_URL, request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfPlaceOrderRequestDataType() {
        String oneAddress = TestUtils.readFile(PlaceOrderRequestTest.class, "place_order.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), PlaceOrder.class);
    }
}