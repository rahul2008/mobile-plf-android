package com.philips.cdp.prodreg.handler;

import com.philips.cdp.prodreg.backend.RegisteredProduct;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProdRegListener {

    void onProdRegSuccess(RegisteredProduct registeredProduct);

    void onProdRegFailed(RegisteredProduct registeredProduct);
}
