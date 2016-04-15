package com.philips.cdp.prodreg.backend;

import android.content.Context;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.prodreg.model.RegisteredProduct;

import java.util.Set;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalRegisteredProductsTest extends MockitoTestCase {

    LocalRegisteredProducts localRegisteredProducts;
    Context context;
    LocalSharedPreference localSharedPreference;
    RegisteredProduct mockRegisteredProduct;
    String data;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        localRegisteredProducts = new LocalRegisteredProducts(context);
        context = getInstrumentation().getContext();
        localSharedPreference = new LocalSharedPreference(context);
        mockRegisteredProduct = mock(RegisteredProduct.class);
        data = localSharedPreference.getData("prod_reg_key");
    }

    public void teststore() {
        LocalRegisteredProducts mocklocalRegisteredProducts = mock(LocalRegisteredProducts.class);
        RegisteredProduct localRegisteredProduct = mock(RegisteredProduct.class);
        mocklocalRegisteredProducts.store(mockRegisteredProduct);
        Set<RegisteredProduct> registeredProductset = mocklocalRegisteredProducts.getUniqueRegisteredProducts();
        final String data = localSharedPreference.getData("prod_reg_key");

        assertEquals(0, registeredProductset.size());
    }

    public void testupdateRegisteredProducts() {
        RegisteredProduct registeredProduct = mock(RegisteredProduct.class);
        int size = 0;
        LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        localRegisteredProducts.updateRegisteredProducts(registeredProduct);
        Set<RegisteredProduct> registeredProducts = localRegisteredProducts.getUniqueRegisteredProducts();
    }

    public void testgetRegisteredProducts() {
        LocalRegisteredProducts mocklocalRegisteredProducts = mock(LocalRegisteredProducts.class);
        final LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        RegisteredProduct[] registeredProductsMock = {new RegisteredProduct(null, null, null, null, null), new RegisteredProduct(null, null, null, null, null)};
        RegisteredProduct registeredProduct = mock(RegisteredProduct.class);
        mocklocalRegisteredProducts.getRegisteredProducts();
        // verify(localRegisteredProducts).syncLocalCache(registeredProductsMock);
    }
}
