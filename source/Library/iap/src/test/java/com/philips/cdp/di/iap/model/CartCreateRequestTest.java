package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.carts.CreateCartData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

public class CartCreateRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchCartCreateRequestURL() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        Assert.assertEquals(NetworkURLConstants.CREATE_CART_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsPOST() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        Assert.assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        Assert.assertNull(request.requestBody());
    }


    @Test
    public void parseResponseShouldBeOfCartCreateDataType() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CartCreateRequestTest.class, "create_cart.txt");
        Object response = request.parseResponse(oneAddress);
        Assert.assertEquals(response.getClass(), CreateCartData.class);
    }
}