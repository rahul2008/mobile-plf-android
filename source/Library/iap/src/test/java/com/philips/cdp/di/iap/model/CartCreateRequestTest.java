package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.carts.CreateCartData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class CartCreateRequestTest extends TestCase {
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
        assertEquals(NetworkURLConstants.CART_CREATE_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsPOST() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartCreateRequest request = new CartCreateRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CartCreateRequestTest.class, "create_cart.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), CreateCartData.class);
    }
}