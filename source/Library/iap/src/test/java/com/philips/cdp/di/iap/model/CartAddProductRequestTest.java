package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.carts.AddToCartData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
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
import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartAddProductRequestTest {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
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
        params.put(ModelConstants.PRODUCT_CODE,NetworkURLConstants.DUMMY_PRODUCT_NUBMBER);
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
        params.put(ModelConstants.PRODUCT_ENTRYCODE, NetworkURLConstants.DUMMY_PRODUCT_NUBMBER);
        params.put(ModelConstants.PRODUCT_CODE, NetworkURLConstants.DUMMY_PRODUCT_ID);
        params.put(ModelConstants.PRODUCT_QUANTITY, "2");
        CartAddProductRequest request = new CartAddProductRequest(mStore, params, null);
        assertEquals(NetworkURLConstants.CART_ADD_TO_URL, request.getUrl());
    }
}