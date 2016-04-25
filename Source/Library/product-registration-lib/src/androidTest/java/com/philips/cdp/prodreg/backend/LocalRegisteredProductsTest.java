package com.philips.cdp.prodreg.backend;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.registration.User;

import java.util.ArrayList;
import java.util.HashSet;
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
        context = getInstrumentation().getContext();
        User user = new User(context);
        localRegisteredProducts = new LocalRegisteredProducts(context, user);
        localSharedPreference = new LocalSharedPreference(context);
        mockRegisteredProduct = mock(RegisteredProduct.class);
        data = localSharedPreference.getData("prod_reg_key");
    }

    public void testStore() {
        User user = mock(User.class);
        final Gson gson = new Gson();
        LocalRegisteredProducts mocklocalRegisteredProducts = new LocalRegisteredProducts(context, user) {
            @Override
            protected Gson getGson() {
                return gson;
            }

        };
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        Set<RegisteredProduct> registeredProductSet = new HashSet<>();
        registeredProducts.add(new RegisteredProduct(null, null, null, null, null));
        registeredProducts.add(new RegisteredProduct(null, null, null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn", "1234", null, null, null));
        registeredProductSet.add(new RegisteredProduct("ctn1", "12345", null, null, null));
    }
}
