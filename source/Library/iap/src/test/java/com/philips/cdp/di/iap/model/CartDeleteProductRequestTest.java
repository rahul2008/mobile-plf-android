package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.ModelConstants;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartDeleteProductRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchCartCreateRequestURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ENTRY_CODE, NetworkURLConstants.DUMMY_PRODUCT_NUBMBER);
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void testGetURLWhenParamsEqualToNull() {
        CartDeleteProductRequest request = new CartDeleteProductRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
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
}