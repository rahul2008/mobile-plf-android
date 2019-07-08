/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.orders.OrdersData;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.Map;

public class OrderHistoryRequest extends AbstractModel {
    public OrderHistoryRequest(StoreListener store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), OrdersData.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        if (params == null) {
            throw new RuntimeException("Order number has to be supplied");
        }
        return  store.getOrderHistoryUrl(params.get(ModelConstants.CURRENT_PAGE));
    }
}
