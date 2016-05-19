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
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Created by 310164421 on 5/3/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetRegionsRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void testRequestMethodIsGET() {
        GetRegionsRequest request = new GetRegionsRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetRegionsRequest request = new GetRegionsRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void matchAddressDetailURL() {
        GetRegionsRequest request = new GetRegionsRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.REGION_URL, request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        GetRegionsRequest request = new GetRegionsRequest(mStore, null, null);
        String paymentResponse = TestUtils.readFile(GetRegionsRequestTest.class, "get_region.txt");
        Object response = request.parseResponse(paymentResponse);
        assertEquals(response.getClass(), RegionsList.class);
    }
}