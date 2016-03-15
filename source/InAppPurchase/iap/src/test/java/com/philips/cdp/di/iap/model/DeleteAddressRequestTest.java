package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeleteAddressRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsDELETE() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        assertEquals(Request.Method.DELETE, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

//    @Test
//    public void testTestingUrilIsNotNull() {
//        DeleteAddressRequest request = Mockito.mock(DeleteAddressRequest.class);
//        Mockito.when(request.getTestUrl()).thenReturn(NetworkConstants.UPDATE_OR_DELETE_ADDRESS_URL);
//        assertNotNull(request.getTestUrl());
//    }

    @Test
    public void parseResponseShouldBeOfDeleteAddressRequestDataType() {
        DeleteAddressRequest request = new DeleteAddressRequest(mStore, null, null);
        Object response = request.parseResponse(null);
        assertNull(response);
    }
}