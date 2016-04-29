package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProdRegListener {

    void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);

    void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);
}
