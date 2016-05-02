package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.localcache.LocalSharedPreference;
import com.philips.cdp.registration.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LocalRegisteredProducts {

    public static String PRODUCT_REGISTRATION_KEY = "prod_reg_key";
    private LocalSharedPreference localSharedPreference;
    private String uuid;

    public LocalRegisteredProducts(Context context, User user) {
        localSharedPreference = new LocalSharedPreference(context);
        uuid = user.getJanrainUUID() != null ? user.getJanrainUUID() : "";
    }

    public void store(RegisteredProduct registeredProduct) {
        Gson gson = getGson();
        Set<RegisteredProduct> registeredProducts = getUniqueRegisteredProducts();
        if (registeredProducts.contains(registeredProduct))
            registeredProducts.remove(registeredProduct);
        registeredProducts.add(registeredProduct);
        localSharedPreference.storeData(PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    @NonNull
    protected Gson getGson() {
        return new Gson();
    }

    protected Set<RegisteredProduct> getUniqueRegisteredProducts() {
        final String data = localSharedPreference.getData(PRODUCT_REGISTRATION_KEY);
        Gson gson = getGson();
        RegisteredProduct[] registeredProducts = gson.fromJson(data, RegisteredProduct[].class);
        if (registeredProducts == null) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(registeredProducts));
    }

    public List<RegisteredProduct> getRegisteredProducts() {
        Gson gson = getGson();
        String data = localSharedPreference.getData(PRODUCT_REGISTRATION_KEY);
        RegisteredProduct[] products = gson.fromJson(data, RegisteredProduct[].class);
        if (products != null) {
            ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
            for (RegisteredProduct registeredProduct : products) {
                if (registeredProduct.getUserUUid().length() == 0 || registeredProduct.getUserUUid().equals(uuid)) {
                    registeredProducts.add(registeredProduct);
                }
            }
            return registeredProducts;
        } else
            return new ArrayList<>();
    }

    public void updateRegisteredProducts(final RegisteredProduct registeredProduct) {
        Gson gson = getGson();
        Set<RegisteredProduct> registeredProducts = getUniqueRegisteredProducts();
        if (registeredProducts.contains(registeredProduct)) {
            registeredProducts.remove(registeredProduct);
        }
        registeredProducts.add(registeredProduct);
        localSharedPreference.storeData(PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    protected void syncLocalCache(final RegisteredProduct[] products) {
        Set<RegisteredProduct> localRegisteredProducts = getUniqueRegisteredProducts();
        for (RegisteredProduct registeredProduct : products) {
            registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
            registeredProduct.setUserUUid(uuid);
            if (localRegisteredProducts.contains(registeredProduct)) {
                localRegisteredProducts.remove(registeredProduct);
            }
            localRegisteredProducts.add(registeredProduct);
        }
        localSharedPreference.storeData(PRODUCT_REGISTRATION_KEY, getGson().toJson(localRegisteredProducts));
    }
}
