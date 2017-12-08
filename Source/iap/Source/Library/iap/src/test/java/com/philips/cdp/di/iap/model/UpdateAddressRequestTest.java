package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

public class UpdateAddressRequestTest {
    @Mock
    private StoreListener mStore;
    private AbstractModel mModel;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPSetting(mock(Context.class)));
        mStore.initStoreConfig(/*"en", "us", */null);
        mModel = new UpdateAddressRequest(mStore, null, null);
    }

    @Test
    public void testRequestMethodIsPut() {
        assertEquals(Request.Method.PUT, mModel.getMethod());
    }

    @Test
    public void testQueryParamsNotNull() {
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, "hello");
        addressHashMap.put(ModelConstants.LAST_NAME, "world");
        addressHashMap.put(ModelConstants.TITLE_CODE, "title");
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, "US");
        addressHashMap.put(ModelConstants.LINE_1, "dsf");
        addressHashMap.put(ModelConstants.LINE_2, "dfs");
        addressHashMap.put(ModelConstants.POSTAL_CODE, "");
        addressHashMap.put(ModelConstants.TOWN, "Delhi?");
        addressHashMap.put(ModelConstants.ADDRESS_ID, "8799470125079");
        addressHashMap.put(ModelConstants.PHONE_1, "5417543010");
        addressHashMap.put(ModelConstants.PHONE_2, "");
        Boolean isDefautAddress = true;
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, isDefautAddress.toString());

        UpdateAddressRequest request = new UpdateAddressRequest(mStore, addressHashMap, null);
        assertNotNull(request.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void testUpdateAddressResponseIsNull() {
        Object response = mModel.parseResponse(null);
        assertNull(response);
    }

    @Test
    public void MatchUpdateAddressValidUrl() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ADDRESS_ID, NetworkURLConstants.ADDRESS_ID);
        UpdateAddressRequest request = new UpdateAddressRequest(mStore, query, null);
        assertEquals(NetworkURLConstants.GET_UPDATE_ADDRESS_URL, request.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateAddressURLWhenParamsIsNull() throws Exception {
        assertNotEquals(NetworkURLConstants.GET_UPDATE_ADDRESS_URL, mModel.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void testUpdateAddressGetValueReturnType() throws Exception {
        assertEquals("", ((UpdateAddressRequest) mModel).getValue(""));
    }

}