/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SetDeliveryAddressRequestTest {

    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    @Mock
    IAPDependencies mIAPDependencies;
    private AbstractModel mModel;

    @Before
    public void setUP() {
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
        mStore.initStoreConfig("en", "US", null);
        mModel = new SetDeliveryAddressRequest(mStore, null, null);
    }

    @Test
    public void testRequestMethodIsPUT() {
        assertEquals(Request.Method.PUT, mModel.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        assertNotNull(mockSetDeliveryAddressRequest.requestBody());
    }

    @Test
    public void testQueryParamsHasBody() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        Map<String, String> params = new HashMap<String, String>();
        assertEquals(mockSetDeliveryAddressRequest.requestBody(), params);
    }

    @Test
    public void testTestingUriIsNotNull() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        assertNotNull(mockSetDeliveryAddressRequest.getUrl());
    }

    @Test
    public void testTestingUrilIsForSetDeliveryAddressRequest() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        assertEquals(mockSetDeliveryAddressRequest.getUrl(), NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
    }

    @Test
    public void parseResponseShouldBeOfSetDeliveryAddressRequestDataType() {
        assertEquals(IAPConstant.IAP_SUCCESS, mModel.parseResponse(null));
    }
}