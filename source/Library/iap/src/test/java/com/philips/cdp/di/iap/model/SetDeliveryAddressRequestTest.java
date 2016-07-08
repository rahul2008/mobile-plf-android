package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class SetDeliveryAddressRequestTest {
    @Mock
    private HybrisStore mHybrisStore;

    @Test
    public void testRequestMethodIsPUT() {
        SetDeliveryAddressRequest request = new SetDeliveryAddressRequest(mHybrisStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL);
        assertNotNull(mockSetDeliveryAddressRequest.requestBody());
    }

    @Test
    public void testQueryParamsHasBody() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL);
        Map<String, String> params = new HashMap<String, String>();
        assertEquals(mockSetDeliveryAddressRequest.requestBody(), params);
    }

    @Test
    public void testTestingUriIsNotNull() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL);
        assertNotNull(mockSetDeliveryAddressRequest.getUrl());
    }

    @Test
    public void testTestingUrilIsForSetDeliveryAddressRequest() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL);
        assertEquals(mockSetDeliveryAddressRequest.getUrl(), NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL);
    }

    @Test
    public void parseResponseShouldBeOfSetDeliveryAddressRequestDataType() {
        SetDeliveryAddressRequest request = new SetDeliveryAddressRequest(mHybrisStore, null, null);
        assertEquals(IAPConstant.IAP_SUCCESS, request.parseResponse(null));
    }
}