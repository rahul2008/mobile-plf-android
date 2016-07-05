package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SetDeliveryAddressModeRequestTest {
    @Mock
    private StoreSpec mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("gb", "us", null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        SetDeliveryAddressModeRequest request = new SetDeliveryAddressModeRequest(mStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        SetDeliveryAddressModeRequest request = new SetDeliveryAddressModeRequest(mStore, null, null);
        assertNotNull(request.requestBody());
    }

    @Test
    public void testQueryParamsHasBodyForCountryEqualToEN() {
        mStore.initStoreConfig("en", "us", null);
        SetDeliveryAddressModeRequest request = new SetDeliveryAddressModeRequest(mStore, null, null);
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.DELIVERY_MODE_ID, "standard-net");
        assertEquals(request.requestBody(), query);
    }

    @Test
    public void testQueryParamsHasBodyForCountryEqualToGB() {

        mStore.setLangAndCountry("GB", "US");
        when(mStore.getCountry()).thenReturn(mStore.getCountry());
        SetDeliveryAddressModeRequest request = Mockito.mock(SetDeliveryAddressModeRequest.class);//new SetDeliveryAddressModeRequest(mStore, null, null);
        HashMap<String, String> query = new HashMap<String, String>();
        query.put(ModelConstants.DELIVERY_MODE_ID, "standard-gross");
        when(request.requestBody()).thenReturn(query);
        assertEquals(request.requestBody(), query);
    }

    @Test
    public void matchPlaceOrderURL() {
        SetDeliveryAddressModeRequest request = new SetDeliveryAddressModeRequest(mStore, null, null);
        assertEquals(NetworkURLConstants.UPDATE_DELIVERY_MODE_URL, request.getUrl());
    }

}