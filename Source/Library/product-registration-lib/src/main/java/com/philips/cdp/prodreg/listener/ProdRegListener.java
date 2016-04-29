package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.backend.RegisteredProduct;
import com.philips.cdp.prodreg.backend.UserWithProducts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProdRegListener {

    void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);

    void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);
}
