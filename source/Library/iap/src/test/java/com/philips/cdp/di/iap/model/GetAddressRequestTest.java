/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class GetAddressRequestTest {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void testRequestMethodIsGET() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        assertNull(request.requestBody());
    }


    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(GetAddressRequestTest.class, "one_Addresses.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), GetShippingAddressData.class);
    }

    @Test
    public void matchAddressDetailURL() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.ADDRESS_DETAILS_URL, request.getUrl());
    }
}
