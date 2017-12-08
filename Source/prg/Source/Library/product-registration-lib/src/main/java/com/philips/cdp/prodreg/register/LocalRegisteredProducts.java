/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.register;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.registration.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocalRegisteredProducts {

    private ProdRegCache prodRegCache;
    private String uuid;
    private User user;
    private Gson gson;

    public LocalRegisteredProducts(User user) {
        this.user = user;
        prodRegCache = new ProdRegCache();
        gson = new Gson();
        uuid = user.getJanrainUUID() != null ? user.getJanrainUUID() : "";
    }

    void store(RegisteredProduct registeredProduct) {
        Gson gson = getGSon();
        Set<RegisteredProduct> registeredProducts = getLocalRegisteredProducts().getUniqueRegisteredProducts();
        if (registeredProducts.contains(registeredProduct))
            registeredProducts.remove(registeredProduct);
        registeredProducts.add(registeredProduct);
        getProdRegCache().storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    @NonNull
    protected Gson getGSon() {
        return gson;
    }

    protected Set<RegisteredProduct> getUniqueRegisteredProducts() {
        final String data = getProdRegCache().getStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY);
        Gson gson = getGSon();
        RegisteredProduct[] registeredProducts = getRegisteredProducts(gson, data);
        if (registeredProducts == null) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(registeredProducts));
    }

    public List<RegisteredProduct> getRegisteredProducts() {
        Gson gson = getGSon();
        String data = getProdRegCache().getStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY);
        RegisteredProduct[] products = getRegisteredProducts(gson, data);
        if (user.isUserSignIn() && products != null) {
            ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
            for (RegisteredProduct registeredProduct : products) {
                if (registeredProduct.getUserUUid().length() == 0 || registeredProduct.getUserUUid().equals(uuid)) {
                    registeredProduct.setUserUUid(uuid);
                    registeredProducts.add(registeredProduct);
                }
            }
            return registeredProducts;
        } else
            return new ArrayList<>();
    }

    protected RegisteredProduct[] getRegisteredProducts(final Gson gson, final String data) {
        return gson.fromJson(data, RegisteredProduct[].class);
    }

    public void updateRegisteredProducts(final RegisteredProduct registeredProduct) {
        Gson gson = getGSon();
        Set<RegisteredProduct> registeredProducts = getUniqueRegisteredProducts();
        if (registeredProducts.contains(registeredProduct)) {
            registeredProducts.remove(registeredProduct);
        }
        registeredProducts.add(registeredProduct);
        getProdRegCache().storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, gson.toJson(registeredProducts));
    }

    protected void migrateLegacyCache(final RegisteredProduct[] products) {
        Set<RegisteredProduct> localRegisteredProducts = getUniqueRegisteredProducts();
        for (RegisteredProduct registeredProduct : products) {
            registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
            registeredProduct.setUserUUid(uuid);
            if (localRegisteredProducts.contains(registeredProduct)) {
                localRegisteredProducts.remove(registeredProduct);
            }
            localRegisteredProducts.add(registeredProduct);
        }
        removeCachedRegisteredProducts(Arrays.asList(products), localRegisteredProducts);
        getProdRegCache().storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, getGSon().toJson(localRegisteredProducts));
    }

    private Set<RegisteredProduct> removeCachedRegisteredProducts(final List<RegisteredProduct> products, final Set<RegisteredProduct> localRegisteredProducts) {
        for (final java.util.Iterator<RegisteredProduct> itr = localRegisteredProducts.iterator(); itr.hasNext(); ) {
            final RegisteredProduct registeredProduct = itr.next();
            if (registeredProduct.getRegistrationState() != null && registeredProduct.getRegistrationState() == RegistrationState.REGISTERED && !products.contains(registeredProduct)) {
                itr.remove();
            }
        }
        return localRegisteredProducts;
    }

    public LocalRegisteredProducts getLocalRegisteredProducts() {
        return this;
    }

    protected ProdRegCache getProdRegCache() {
        return prodRegCache;
    }

    protected User getUser() {
        return user;
    }

    public void removeProductFromCache(final RegisteredProduct registeredProduct) {
        Set<RegisteredProduct> registeredProducts = getUniqueRegisteredProducts();
        if (registeredProducts.contains(registeredProduct)) {
            registeredProducts.remove(registeredProduct);
        }
        getProdRegCache().storeStringData(ProdRegConstants.PRODUCT_REGISTRATION_KEY, getGSon().toJson(registeredProducts));
    }
}
