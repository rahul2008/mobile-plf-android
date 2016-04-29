package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface RegisteredProductsListener {
    void getRegisteredProducts(List<RegisteredProduct> registeredProducts, long timeStamp);
}
