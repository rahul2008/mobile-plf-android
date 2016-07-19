package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponse;
import com.philips.cdp.prodreg.model.registeredproducts.RegisteredResponseData;
import com.philips.cdp.prodreg.prxrequest.RegisteredProductsRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public class RemoteRegisteredProducts {
    private static final String TAG = RemoteRegisteredProducts.class.getSimpleName();
    @NonNull
    ResponseListener getPrxResponseListenerForRegisteredProducts(final UserWithProducts userWithProducts, final LocalRegisteredProducts localRegisteredProducts, final RegisteredProductsListener registeredProductsListener) {
        return new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                RegisteredResponse registeredDataResponse = (RegisteredResponse) responseData;
                RegisteredResponseData[] results = registeredDataResponse.getResults();
                Gson gson = getGson();
                RegisteredProduct[] registeredProducts = userWithProducts.getRegisteredProductsFromResponse(results, gson);
                localRegisteredProducts.syncLocalCache(registeredProducts);
                registeredProductsListener.getRegisteredProducts(localRegisteredProducts.getRegisteredProducts(), userWithProducts.getTimeStamp());
            }

            @Override
            public void onResponseError(PrxError prxError) {
                try {
                    if (prxError.getStatusCode() == PrxError.PrxErrorType.AUTHENTICATION_FAILURE.getId()) {
                        userWithProducts.onAccessTokenExpire(null);
                    } else
                        registeredProductsListener.getRegisteredProducts(localRegisteredProducts.getRegisteredProducts(), 0);
                } catch (Exception e) {
                    ProdRegLogger.e(TAG, e.getMessage());
                }
            }
        };
    }

    @NonNull
    protected Gson getGson() {
        return new Gson();
    }

    public void getRegisteredProducts(final Context mContext, final UserWithProducts userWithProducts, final User user, final RegisteredProductsListener registeredProductsListener) {
        RegisteredProductsRequest registeredProductsRequest = getRegisteredProductsRequest(user);
        final RequestManager mRequestManager = getRequestManager(mContext);
        mRequestManager.executeRequest(registeredProductsRequest, getPrxResponseListenerForRegisteredProducts(userWithProducts, new LocalRegisteredProducts(mContext, user), registeredProductsListener));
    }

    @NonNull
    protected RegisteredProductsRequest getRegisteredProductsRequest(final User user) {
        RegisteredProductsRequest registeredProductsRequest = new RegisteredProductsRequest();
        registeredProductsRequest.setAccessToken(user.getAccessToken());
        return registeredProductsRequest;
    }

    @NonNull
    protected RequestManager getRequestManager(final Context context) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(context);
        return mRequestManager;
    }
}
