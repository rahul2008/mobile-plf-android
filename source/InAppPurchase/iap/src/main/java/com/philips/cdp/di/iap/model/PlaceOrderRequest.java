package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PlaceOrderRequest extends AbstractModel {
    public PlaceOrderRequest(final Store store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), PlaceOrder.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.CART_ID, this.params.get(ModelConstants.CART_ID));
        return params;
    }

    @Override
    public String getTestUrl() {
        return NetworkConstants.PLACE_ORDER_URL;
    }
}
