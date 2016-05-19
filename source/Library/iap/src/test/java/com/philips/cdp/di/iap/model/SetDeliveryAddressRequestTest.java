package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SetDeliveryAddressRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPUT() {
        SetDeliveryAddressRequest request = new SetDeliveryAddressRequest(mStore, null, null);
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
        SetDeliveryAddressRequest request = new SetDeliveryAddressRequest(mStore, null, null);
        Object response = IAPConstant.IAP_SUCCESS;
        assertEquals(response, IAPConstant.IAP_SUCCESS);
    }
}