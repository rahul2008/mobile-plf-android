package com.philips.cdp.prodreg.handler;

import com.philips.cdp.prodreg.model.RegisteredProduct;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface ProdRegListener {

    void onProdRegSuccess();

    void onProdRegFailed(RegisteredProduct registeredProduct);
}
