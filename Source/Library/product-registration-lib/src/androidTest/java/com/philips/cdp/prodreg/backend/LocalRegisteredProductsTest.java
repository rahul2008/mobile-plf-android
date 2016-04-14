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

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        localRegisteredProducts= new LocalRegisteredProducts(context);
        context = getInstrumentation().getContext();
        localSharedPreference = new LocalSharedPreference(context);
    }

    public void testupdateRegisteredProducts(){
        RegisteredProduct registeredProduct=mock(RegisteredProduct.class);
        int size=0;
        LocalRegisteredProducts localRegisteredProducts = mock(LocalRegisteredProducts.class);
        localRegisteredProducts.updateRegisteredProducts(registeredProduct);
        final String data = localSharedPreference.getData("prod_reg_key");
        Set<RegisteredProduct> registeredProducts = localRegisteredProducts.getUniqueRegisteredProducts();

       }
}