package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConfiguration;

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
public class CreateAddressRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPOST() {
        CreateAddressRequest request = new CreateAddressRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CreateAddressRequest mockCreateAddressRequest = Mockito.mock(CreateAddressRequest.class);
        assertNotNull(mockCreateAddressRequest.requestBody());
    }

    @Test
    public void testTestingUrilIsNotNull() {
        CreateAddressRequest request = new CreateAddressRequest(mStore, null, null);
        IAPConfiguration iapConfiguration = Mockito.mock(IAPConfiguration.class);
//        CartModelContainer.getInstance().setIapConfiguration(iapConfiguration);
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getHostport()).thenReturn("tst.pl.shop.philips.com");
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getSite()).thenReturn("US_Tuscany");
        assertNotNull(request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfCreateAddressRequestDataType() {
        CreateAddressRequest request = new CreateAddressRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CreateAddressRequestTest.class, "create_address.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), Addresses.class);
    }
}