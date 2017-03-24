package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.response.carts.AddToCartData;
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
import static org.mockito.Mockito.mock;

public class CartAddProductRequestTest {
    @Mock
    private StoreListener mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPSetting(mock(Context.class)));
        mStore.initStoreConfig(/*"en", "us",*/ null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        CartAddProductRequest request = new CartAddProductRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, params.get(ModelConstants.PRODUCT_CODE));
        CartAddProductRequest mockCartAddProductRequest = Mockito.mock(CartAddProductRequest.class);
        Mockito.when(mockCartAddProductRequest.requestBody()).thenReturn(params);

        assertNotNull(mockCartAddProductRequest.requestBody());
    }

    @Test
    public void testQueryParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, NetworkURLConstants.DUMMY_PRODUCT_NUMBER);
        CartAddProductRequest request = new CartAddProductRequest(mStore, params, null);
        assertNotNull(request.requestBody());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartAddProductRequest request = new CartAddProductRequest(mStore, null, null);
        String addtoCartResponse = TestUtils.readFile(CartAddProductRequestTest.class, "add_to_cart.txt");
        Object response = request.parseResponse(addtoCartResponse);
        assertEquals(response.getClass(), AddToCartData.class);
    }

    @Test
    public void matchCartAddProductRequestURL() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_ENTRYCODE, NetworkURLConstants.DUMMY_PRODUCT_NUMBER);
        params.put(ModelConstants.PRODUCT_CODE, NetworkURLConstants.DUMMY_PRODUCT_ID);
        params.put(ModelConstants.PRODUCT_QUANTITY, "2");
        CartAddProductRequest request = new CartAddProductRequest(mStore, params, null);
        assertEquals(NetworkURLConstants.ADD_TO_CART_URL, request.getUrl());
    }
}