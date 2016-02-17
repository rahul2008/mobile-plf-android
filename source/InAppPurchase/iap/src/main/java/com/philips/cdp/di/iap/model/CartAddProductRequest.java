/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;

public class CartAddProductRequest extends AbstractModel {
    public CartAddProductRequest(final Store store, final Map<String, String> query,
                                 DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), AddToCartData.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, this.params.get(ModelConstants.PRODUCT_CODE));
        return params;
    }

    @Override
    public String getTestUrl() {
        return NetworkConstants.ADD_TO_CART_URL;
    }
}