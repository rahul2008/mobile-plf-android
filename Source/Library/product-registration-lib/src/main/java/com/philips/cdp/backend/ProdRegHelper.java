/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.error.ErrorType;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.handler.Product;
import com.philips.cdp.handler.UserProduct;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.model.ProdRegRegisteredDataResponse;
import com.philips.cdp.model.ProdRegRegisteredResults;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.registration.User;

/**
 * <b> Helper class used to process product registration backend calls</b>
 */
public class ProdRegHelper {

    private String locale;

    /**
     * <b> API to get registered products</b>
     *
     * @param context    - Context of an activity
     * @param prodRegRequestInfo - prodRegRequestInfo object
     * @param listener   - Callback listener
     */
    public void getRegisteredProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        prodRegRequestInfo.setLocale(this.locale);
        UserProduct userProduct = new UserProduct();
        userProduct.getRegisteredProducts(context, prodRegRequestInfo, listener);
    }
    /**
     * <b> API to register product</b>
     *
     * @param context            - Context of an activity
     * @param prodRegRequestInfo - PRX Request object
     * @param listener           - Callback listener
     */
    public void registerProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        Validator validator = new Validator();
        processForReg(context, prodRegRequestInfo, listener, validator, new User(context));
    }

    protected void processForReg(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener, final Validator validator, final User user) {
        if (!validator.isUserSignedIn(user, context)) {
            listener.onProdRegFailed(ErrorType.USER_NOT_SIGNED_IN);
        } else {
            if (!validator.isValidaDate(prodRegRequestInfo.getPurchaseDate())) {
                listener.onProdRegFailed(ErrorType.INVALID_DATE);
            } else {
                final ProdRegListener getRegisteredProductsListener = getRegisteredProductsListener(context, prodRegRequestInfo, listener);
                getRegisteredProduct(context, new ProdRegRequestInfo(null, null, Sector.B2C, Catalog.CONSUMER), getRegisteredProductsListener);
            }
        }
    }

    @NonNull
    private ProdRegListener getRegisteredProductsListener(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        return new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
                ProdRegRegisteredDataResponse registeredDataResponse = (ProdRegRegisteredDataResponse) responseData;
                ProdRegRegisteredResults[] results = registeredDataResponse.getResults();
                for (ProdRegRegisteredResults result : results) {
                    if (prodRegRequestInfo.getCtn().equalsIgnoreCase(result.getProductModelNumber())) {
                        listener.onProdRegFailed(ErrorType.PRODUCT_ALREADY_REGISTERED);
                        return;
                    }
                }
                processMetadata(context, prodRegRequestInfo, listener);
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                listener.onProdRegFailed(errorType);
            }
        };
    }

    /**
     * Api to set locale
     *
     * @param language    - language code of type String
     * @param countryCode - country code of type String
     */
    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    private void makeRegistrationRequest(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        UserProduct userProduct = new UserProduct();
        userProduct.registerProduct(context, prodRegRequestInfo, listener);
    }

    protected void processMetadata(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ProdRegListener listener) {
        Product product = new Product();
        prodRegRequestInfo.setLocale(this.locale);
        product.getProductMetadata(context, prodRegRequestInfo, new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                makeRegistrationRequest(context, prodRegRequestInfo, listener);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                listener.onProdRegFailed(ErrorType.METADATA_FAILED);
            }
        }, listener);
    }


}
