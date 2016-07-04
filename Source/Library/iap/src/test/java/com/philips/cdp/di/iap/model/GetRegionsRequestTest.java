/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class GetRegionsRequestTest {
    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private AbstractModel mModel;

    @Before
    public void setUP() {
        StoreSpec mStore = (new MockStore(mContext, mUser)).getStore();
        mStore.initStoreConfig("en", "US", null);
        mModel = new GetRegionsRequest(mStore, null, null);
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
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.REGION_URL, mModel.getUrl());
    }

    @Test
    public void isValidResponse() {
        String validResponse = TestUtils.readFile(GetRegionsRequestTest.class, "Region.txt");
        Object response = mModel.parseResponse(validResponse);
        assertEquals(response.getClass(), RegionsList.class);
    }
}