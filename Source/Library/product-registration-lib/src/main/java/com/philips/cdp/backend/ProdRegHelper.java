package com.philips.cdp.backend;

import android.content.Context;

import com.philips.cdp.core.ProdRegConstants;
import com.philips.cdp.handler.Product;
import com.philips.cdp.handler.UserProduct;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegHelper {

    private Context mContext = null;
    private String TAG = getClass() + "";
    private String requestType;
    private String locale;

    /**
     * <b> Helper class used to process product registration backend calls</b>
     *
     * @param context - Context of an Activity
     */
    public ProdRegHelper(Context context) {
        this.mContext = context;
    }

    public void cancelRequest(String requestTag) {
    }

    private void makeRegistrationRequest(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        UserProduct userProduct = new UserProduct();
        userProduct.registerProduct(context, prodRegRequestInfo, listener);
    }

    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    protected void processMetadata(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        Product product = new Product();
        prodRegRequestInfo.setLocale(this.locale);
        product.getProductMetadata(context, prodRegRequestInfo, new ResponseListener() {
            @Override
            public void onResponseSuccess(final ResponseData responseData) {
                makeRegistrationRequest(context, prodRegRequestInfo, listener);
            }

            @Override
            public void onResponseError(final String errorMessage, final int responseCode) {
                listener.onResponseError(errorMessage, responseCode);
            }
        }, listener);
    }

    /**
     * <b> API to get registered products</b>
     *
     * @param context    - Context of an activity
     * @param prodRegRequestInfo - prodRegRequestInfo object
     * @param listener   - Callback listener
     */
    public void getRegisteredProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        requestType = ProdRegConstants.FETCH_REGISTERED_PRODUCTS;
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
    public void registerProduct(final Context context, final ProdRegRequestInfo prodRegRequestInfo, final ResponseListener listener) {
        requestType = ProdRegConstants.PRODUCT_REGISTRATION;
        processMetadata(context, prodRegRequestInfo, listener);
    }
}
