/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class GetAddressRequestTest {
    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private AbstractModel mModel;

    @Before
    public void setUP() {
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPDependencies());
        mStore.initStoreConfig("en", "US", null);
        mModel = new GetAddressRequest(mStore, null, null);
    }

    @Test
    public void testRequestMethodIsGET() {
        assertEquals(Request.Method.GET, mModel.getMethod());
    }

    @Test
    public void testBodyParamsIsNull() {
        assertNull(mModel.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void isValidResponse() {
        String validAddress = TestUtils.readFile(GetAddressRequestTest.class,
                "Addresses.txt");
        Object response = mModel.parseResponse(validAddress);
        assertEquals(response.getClass(), GetShippingAddressData.class);
    }

    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.GET_ADDRESSES_URL, mModel.getUrl());
    }
}
