package com.philips.cdp.prodreg.handler;

import com.philips.cdp.prodreg.model.RegisteredResponse;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface GetRegisteredProductsListener {

    void getRegisteredProducts(RegisteredResponse registeredDataResponse);

    void onErrorResponse(String errorMessage, int responseCode);
}
