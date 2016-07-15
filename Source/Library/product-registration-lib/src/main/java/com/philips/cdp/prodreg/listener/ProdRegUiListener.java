package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProdRegUiListener {

    void onProdRegContinue(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);
    void onProdRegBack(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);
}
