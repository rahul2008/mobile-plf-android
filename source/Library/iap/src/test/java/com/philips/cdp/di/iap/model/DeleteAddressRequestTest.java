package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static org.mockito.Mockito.mock;

public class DeleteAddressRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPDependencies());
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchDeleteAddressRequestURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, NetworkURLConstants.DUMMY_PRODUCT_ID);
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, query, null);
        Assert.assertEquals(NetworkURLConstants.EDIT_ADDRESS_URL, request.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void matchDeleteAddressRequestURLWhenParamsEqualToNull() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, NetworkURLConstants.DUMMY_PRODUCT_ID);
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        Assert.assertEquals(NetworkURLConstants.EDIT_ADDRESS_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsDELETE() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        Assert.assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        Assert.assertNull(request.requestBody());
    }

    @Test
    public void parseResponseShouldBeOfDeleteAddressRequestDataType() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        Object response = request.parseResponse(null);
        Assert.assertNull(response);
    }
}