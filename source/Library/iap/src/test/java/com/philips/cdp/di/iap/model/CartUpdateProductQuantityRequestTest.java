package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.carts.UpdateCartData;
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
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartUpdateProductQuantityRequestTest extends TestCase {
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
        query.put(ModelConstants.PRODUCT_ENTRYCODE, NetworkURLConstants.DUMMY_PRODUCT_NUBMBER);
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
    public void testGetURLWhenParamsEqualToNull() {
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, request.getUrl());
    }

}