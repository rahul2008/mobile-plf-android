package com.philips.cdp.prodreg.register;

import android.content.Context;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.registration.User;

import org.mockito.Mock;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalRegisteredProductsTest extends MockitoTestCase {

    @Mock
    LocalRegisteredProducts localRegisteredProducts;
    Context context;
    LocalSharedPreference localSharedPreference;
    RegisteredProduct mockRegisteredProduct;
    String data;
    RegisteredProduct registeredProduct;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        User user = new User(context);
        localRegisteredProducts = new LocalRegisteredProducts(context, user);
        localSharedPreference = new LocalSharedPreference(context);
        mockRegisteredProduct = mock(RegisteredProduct.class);
        data = localSharedPreference.getData("prod_reg_key");
        registeredProduct = new RegisteredProduct("HC5410/83", "1344", "26/2/2016", Sector.B2C, Catalog.CONSUMER);
        //  testStore(registeredProduct);
    }

    /*public void testStore(RegisteredProduct registeredProductMock) {
        localRegisteredProducts.store(registeredProductMock);
        Set<RegisteredProduct> registeredProducts = localRegisteredProducts.getUniqueRegisteredProducts();
        when(registeredProducts).thenReturn((Set<RegisteredProduct>) registeredProduct);
        assertEquals(registeredProductMock,registeredProduct);
    }

    public void testGetUniqueRegisteredProducts(){
        localRegisteredProducts.getUniqueRegisteredProducts();
        RegisteredProduct[] registeredProducts= new RegisteredProduct[]{};
        String data = localSharedPreference.getData("prod_reg_key");
    }
*/
}
