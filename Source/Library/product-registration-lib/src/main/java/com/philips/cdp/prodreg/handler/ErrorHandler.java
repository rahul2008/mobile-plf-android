package com.philips.cdp.prodreg.handler;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.backend.UserProduct;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.RegistrationState;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorHandler {
    private Context mContext;

    public ErrorHandler(Context mContext){
       this.mContext=mContext;
    }

    public void handleError( RegisteredProduct registeredProduct, int statusCode,  ProdRegListener appListener){

        if (statusCode == ProdRegError.INVALID_CTN.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_CTN, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct);
        } else if (statusCode == ProdRegError.ACCESS_TOKEN_INVALID.getCode()) {
            getUserProduct(mContext).onAccessTokenExpire(registeredProduct, appListener);
        } else if (statusCode == ProdRegError.INVALID_VALIDATION.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct);
        } else if (statusCode == ProdRegError.INVALID_SERIALNUMBER.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct);
        } else if (statusCode == ProdRegError.NO_INTERNET_AVAILABLE.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.PENDING);
            appListener.onProdRegFailed(registeredProduct);
        } else if (statusCode == ProdRegError.INTERNAL_SERVER_ERROR.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.PENDING);
            appListener.onProdRegFailed(registeredProduct);
        } else if (statusCode == ProdRegError.METADATA_FAILED.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.METADATA_FAILED, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct);
        } else if (statusCode == ProdRegError.TIME_OUT.getCode()) {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.TIME_OUT, RegistrationState.PENDING);
            appListener.onProdRegFailed(registeredProduct);
        } else {
            getUserProduct(mContext).updateLocaleCacheOnError(registeredProduct, ProdRegError.UNKNOWN, RegistrationState.FAILED);
            appListener.onProdRegFailed(registeredProduct);
        }
    }
    @NonNull
    UserProduct getUserProduct(Context context) {
        return new UserProduct(context);
    }
}
