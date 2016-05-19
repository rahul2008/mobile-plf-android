package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.retailers.WebResults;
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
 * Created by 310164421 on 5/3/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetRetailersInfoRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void testRequestMethodIsGET() {
        GetRetailersInfoRequest request = new GetRetailersInfoRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetRetailersInfoRequest request = new GetRetailersInfoRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void matchAddressDetailURL() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.PRODUCT_CODE, ModelConstants.PRODUCT_CODE);
        GetRetailersInfoRequest request = new GetRetailersInfoRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.RETAILORS_URL, request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetRetailersInfoRequestDataType() {
        GetRetailersInfoRequest request = new GetRetailersInfoRequest(mStore, null, null);
        String paymentResponse = TestUtils.readFile(GetRetailersInfoRequestTest.class, "get_catalog.txt");
        Object response = request.parseResponse(paymentResponse);
        assertEquals(response.getClass(), WebResults.class);
    }

    @Test(expected = RuntimeException.class)
    public void testGetUrlWhenParamsEqualToNull() {
        GetRetailersInfoRequest request = new GetRetailersInfoRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.RETAILORS_URL, request.getUrl());
    }
}