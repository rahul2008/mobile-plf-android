package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.carts.AddToCartData;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConfiguration;

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
public class CartAddProductRequestTest {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPOST() {
        CartAddProductRequest request = new CartAddProductRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, params.get(ModelConstants.PRODUCT_CODE));
        CartAddProductRequest mockCartAddProductRequest = Mockito.mock(CartAddProductRequest.class);
        Mockito.when(mockCartAddProductRequest.requestBody()).thenReturn(params);

        assertNotNull(mockCartAddProductRequest.requestBody());
    }

    @Test
    public void testTestingUrilIsNotNull() {
        CartAddProductRequest request = new CartAddProductRequest(mStore, null, null);
        IAPConfiguration iapConfiguration = Mockito.mock(IAPConfiguration.class);
        CartModelContainer.getInstance().setIapConfiguration(iapConfiguration);
        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getHostport()).thenReturn("tst.pl.shop.philips.com");
        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getSite()).thenReturn("US_Tuscany");
        assertNotNull(request.getTestUrl());
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartAddProductRequest request = new CartAddProductRequest(mStore, null, null);
        String addtoCartResponse = TestUtils.readFile(CartAddProductRequestTest.class, "add_to_cart.txt");
        Object response = request.parseResponse(addtoCartResponse);
        assertEquals(response.getClass(), AddToCartData.class);
    }
}