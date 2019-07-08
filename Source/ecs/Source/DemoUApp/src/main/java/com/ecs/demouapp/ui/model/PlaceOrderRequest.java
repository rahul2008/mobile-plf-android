package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.placeorder.PlaceOrder;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PlaceOrderRequest extends AbstractModel {
    public PlaceOrderRequest(final StoreListener store, final Map<String, String> query, final DataLoadListener listener) {
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
        Map<String, String> params = new HashMap<>();
        String securityCode = this.params.get(ModelConstants.SECURITY_CODE);
        if (securityCode != null)
            params.put(ModelConstants.SECURITY_CODE, securityCode);
        params.put(ModelConstants.CART_ID, "current");
        return params;
    }

    @Override
    public String getUrl() {
        return store.getPlaceOrderUrl();
    }
}
