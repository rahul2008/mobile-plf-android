package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.orders.OrderDetail;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.Map;

/**
 * Created by 310241054 on 6/2/2016.
 */
public class OrderDetailRequest extends AbstractModel {
    public OrderDetailRequest(StoreListener store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }
    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), OrderDetail.class);
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
        return store.getOrderDetailUrl(params.get(ModelConstants.ORDER_NUMBER));
    }
}
