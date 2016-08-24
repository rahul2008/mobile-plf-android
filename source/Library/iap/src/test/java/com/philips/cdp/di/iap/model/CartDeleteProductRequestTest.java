package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
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
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class CartDeleteProductRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPDependencies());
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchCartCreateRequestURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ENTRY_CODE, NetworkURLConstants.DUMMY_PRODUCT_NUMBER);
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void testGetURLWhenParamsEqualToNull() throws Exception {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        assertNotEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsDELETE() {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        Map<String, String> payload = new HashMap<>();
        payload.put(ModelConstants.PRODUCT_CODE, ModelConstants.PRODUCT_CODE);
        payload.put(ModelConstants.ENTRY_CODE, ModelConstants.ENTRY_CODE);
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, payload, null);
        assertNotNull(request.requestBody());
    }


    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        String str = null;
        Object response = request.parseResponse(str);
        assertNull(null, response);
    }

    @Test
    public void testStoreIsNotNull() {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        assertNotNull(request.getStore());
    }
}