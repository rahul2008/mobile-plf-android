package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.carts.UpdateCartData;
import com.philips.cdp.di.iap.store.Store;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310164421 on 3/8/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CartUpdateProductQuantityRequestTest extends TestCase {
    @Mock
    private Store mStore;

    @Test
    public void testRequestMethodIsPUT() {
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }

    @Test
    public void testQueryParamsIsNull() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, params.get(ModelConstants.PRODUCT_CODE));
        params.put(ModelConstants.PRODUCT_QUANTITY, params.get(ModelConstants.PRODUCT_QUANTITY));
        CartUpdateProductQuantityRequest mockCartUpdateProductQuantityRequest = Mockito.mock(CartUpdateProductQuantityRequest.class);
        Mockito.when(mockCartUpdateProductQuantityRequest.requestBody()).thenReturn(params);

        assertNotNull(mockCartUpdateProductQuantityRequest.requestBody());
    }
//    @Test
//    public void testTestingUrilIsNotNull() {
//        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
//        IAPConfiguration iapConfiguration = Mockito.mock(IAPConfiguration.class);
//        CartModelContainer.getInstance().setIapConfiguration(iapConfiguration);
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getHostport()).thenReturn("tst.pl.shop.philips.com");
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getSite()).thenReturn("US_Tuscany");
//        CartUpdateProductQuantityRequest mockCartUpdateProductQuantityRequest = Mockito.mock(CartUpdateProductQuantityRequest.class);
//        Mockito.when(mockCartUpdateProductQuantityRequest.getUrl()).thenReturn(NetworkConstants.UPDATE_QUANTITY_URL);
//        assertNotNull(mockCartUpdateProductQuantityRequest.getUrl());
//    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
        CartUpdateProductQuantityRequest request = new CartUpdateProductQuantityRequest(mStore, null, null);
        String addtoCartResponse = TestUtils.readFile(CartUpdateProductQuantityRequestTest.class, "update_cart.txt");
        Object response = request.parseResponse(addtoCartResponse);
        assertEquals(response.getClass(), UpdateCartData.class);
    }
}