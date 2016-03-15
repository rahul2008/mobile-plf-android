/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConfiguration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class GetAddressRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsGET() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void testTestingUrilIsNotNull() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        IAPConfiguration iapConfiguration = Mockito.mock(IAPConfiguration.class);
        CartModelContainer.getInstance().setIapConfiguration(iapConfiguration);
        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getHostport()).thenReturn("tst.pl.shop.philips.com");
        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getSite()).thenReturn("US_Tuscany");
        assertNotNull(request.getTestUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        GetAddressRequest request = new GetAddressRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(GetAddressRequestTest.class,"one_Addresses.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), GetShippingAddressData.class);
    }
}
