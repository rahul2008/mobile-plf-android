package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.carts.Carts;
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
public class CartCurrentInfoRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsGET() {
        CartCurrentInfoRequest request = new CartCurrentInfoRequest(mStore, null, null);
        assertEquals(Request.Method.GET, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        CartCurrentInfoRequest request = new CartCurrentInfoRequest(mStore, null, null);
        assertNull(request.requestBody());
    }

    @Test
    public void testTestingUrilIsNotNull() {
        CartCurrentInfoRequest request = new CartCurrentInfoRequest(mStore, null, null);
        IAPConfiguration iapConfiguration = Mockito.mock(IAPConfiguration.class);
//        CartModelContainer.getInstance().setIapConfiguration(iapConfiguration);
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getHostport()).thenReturn("tst.pl.shop.philips.com");
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getSite()).thenReturn("US_Tuscany");
        assertNotNull(request.getUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartCurrentInfoRequest request = new CartCurrentInfoRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(CartCurrentInfoRequestTest.class, "create_cart.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), Carts.class);
    }
}