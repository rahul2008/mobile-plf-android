package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

/**
 * Created by Apple on 08/08/16.
 */
public class CartDeleteRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPDependencies());
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchCartDeleteRequestURL() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Assert.assertEquals(NetworkURLConstants.DELETE_CART_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsPOST() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Assert.assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Assert.assertNull(request.requestBody());
    }


    @Test
    public void parseResponseShouldBeOfCartCreateDataType() {
        DeleteCartRequest request = new DeleteCartRequest(mStore, null, null);
        Object response = request.parseResponse(null);
        Assert.assertNull(response);
    }
}