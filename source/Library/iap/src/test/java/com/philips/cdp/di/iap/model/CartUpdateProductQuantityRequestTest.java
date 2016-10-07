package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.response.carts.UpdateCartData;
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
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class CartUpdateProductQuantityRequestTest {
    @Mock
    private StoreListener mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPSetting(mock(Context.class)));
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchCartCreateRequestURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.PRODUCT_ENTRYCODE, NetworkURLConstants.DUMMY_PRODUCT_NUMBER);
        query.put(ModelConstants.PRODUCT_CODE, NetworkURLConstants.DUMMY_PRODUCT_ID);
        query.put(ModelConstants.PRODUCT_QUANTITY, "2");
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsPUT() {
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, params.get(ModelConstants.PRODUCT_CODE));
        params.put(ModelConstants.PRODUCT_QUANTITY, params.get(ModelConstants.PRODUCT_QUANTITY));
        CartUpdateProductQuantityRequest mockCartUpdateProductQuantityRequest = Mockito.mock(CartUpdateProductQuantityRequest.class);
        Mockito.when(mockCartUpdateProductQuantityRequest.requestBody()).thenReturn(params);

        assertNotNull(mockCartUpdateProductQuantityRequest.requestBody());
    }

    @Test
    public void testQueryParamsForRequestBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, ModelConstants.PRODUCT_CODE);
        params.put(ModelConstants.PRODUCT_QUANTITY, ModelConstants.PRODUCT_QUANTITY);
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, params, null);
        assertEquals(request.requestBody(), params);
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        String addtoCartResponse = TestUtils.readFile(CartUpdateProductQuantityRequestTest.class, "update_cart.txt");
        Object response = request.parseResponse(addtoCartResponse);
        assertEquals(response.getClass(), UpdateCartData.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGetURLWhenParamsEqualToNull() throws Exception {
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        assertNotEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
    }

    @Test
    public void testStoreIsNotNull() {

        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        assertNotNull(request.getStore());
    }

}