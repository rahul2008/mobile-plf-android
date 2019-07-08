package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.payment.PaymentMethods;
import com.ecs.demouapp.ui.store.StoreListener;
import com.google.gson.Gson;


import java.util.Map;

public class GetPaymentDetailRequest extends AbstractModel {

    public GetPaymentDetailRequest(StoreListener store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), PaymentMethods.class);
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
        return store.getPaymentDetailsUrl();
    }
}
