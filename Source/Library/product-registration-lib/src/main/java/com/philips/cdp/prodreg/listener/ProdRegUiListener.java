package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProdRegUiListener {

    void onProdRegContinue(List<RegisteredProduct> registeredProducts, UserWithProducts userWithProduct);

    void onProdRegBack(List<RegisteredProduct> registeredProducts, UserWithProducts userWithProduct);
}
