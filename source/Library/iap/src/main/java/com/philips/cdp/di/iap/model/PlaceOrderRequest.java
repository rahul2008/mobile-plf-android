package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.ModelConstants;

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
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), PlaceOrder.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        String cartNumber = CartModelContainer.getInstance().getCartNumber();
        Map<String, String> params = new HashMap<>();
        params.put(ModelConstants.CART_ID, cartNumber);
        return params;
    }

    @Override
    public String getUrl() {
        return store.getPlaceOrderUrl();
    }
}
