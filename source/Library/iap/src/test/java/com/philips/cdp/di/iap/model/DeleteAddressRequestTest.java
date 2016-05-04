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

import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteAddressRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void matchDeleteAddressRequestURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, NetworkURLConstants.DUMMY_PRODUCT_ID);
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.ADDRESS_ALTER_URL, request.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void matchDeleteAddressRequestURLWhenParamsEqualToNull() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, NetworkURLConstants.DUMMY_PRODUCT_ID);
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.ADDRESS_ALTER_URL, request.getUrl());
    }

    @Test
    public void testRequestMethodIsDELETE() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void parseResponseShouldBeOfDeleteAddressRequestDataType() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        Object response = request.parseResponse(null);
        assertNull(response);
    }
}