package com.philips.cdp.prodreg.error;

import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorHandler {

    public void handleError(final UserWithProducts userProduct, RegisteredProduct registeredProduct, int statusCode, ProdRegListener appListener) {

        if (statusCode == ProdRegError.INVALID_CTN.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.INVALID_CTN, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.ACCESS_TOKEN_INVALID.getCode()) {
            userProduct.onAccessTokenExpire(registeredProduct, appListener);
        } else if (statusCode == ProdRegError.INVALID_VALIDATION.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.INVALID_SERIALNUMBER.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.NO_INTERNET_AVAILABLE.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.INTERNAL_SERVER_ERROR.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.TIME_OUT.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.TIME_OUT, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.NETWORK_ERROR.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.NETWORK_ERROR, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.PARSE_ERROR.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.PARSE_ERROR, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else if (statusCode == ProdRegError.INVALID_SERIAL_NUMBER_AND_PURCHASE_DATE.getCode()) {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.INVALID_SERIAL_NUMBER_AND_PURCHASE_DATE, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        } else {
            userProduct.updateLocaleCache(registeredProduct, ProdRegError.UNKNOWN, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct, userProduct);
        }
    }
}
