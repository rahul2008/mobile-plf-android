package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.prodreg.model.RegisteredProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalRegisteredProducts {

    private LocalSharedPreference localSharedPreference;

    public LocalRegisteredProducts(Context context) {
        localSharedPreference = new LocalSharedPreference(context);
    }

    public void storeProductLocally(RegisteredProduct registeredProduct) {
        Gson gson = getGson();
        ArrayList<RegisteredProduct> registeredProducts = getJsonArray();
        registeredProducts.add(registeredProduct);
        localSharedPreference.storeData(UserProduct.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    @NonNull
    private Gson getGson() {
        return new Gson();
    }

    public ArrayList<RegisteredProduct> getJsonArray() {
        final String data = localSharedPreference.getData(UserProduct.PRODUCT_REGISTRATION_KEY);
        Gson gson = getGson();
        RegisteredProduct[] registeredProducts = gson.fromJson(data,
                RegisteredProduct[].class);
        if (registeredProducts == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(registeredProducts));
    }

    public List<RegisteredProduct> getRegisteredProducts() {
        Gson gson = getGson();
        String s = localSharedPreference.getData(UserProduct.PRODUCT_REGISTRATION_KEY);
        RegisteredProduct[] products = gson.fromJson(s, RegisteredProduct[].class);
        return Arrays.asList(products);
    }
}
