/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.carts.CartsEntity;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.google.gson.Gson;


import java.util.Map;

public class GetCurrentCartRequest extends AbstractModel {

    public GetCurrentCartRequest(StoreListener store, Map<String, String> query,
                                 DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), CartsEntity.class);
    }

    @Override
    public int getMethod() {
        ECSLog.d(ECSLog.LOG, "GET");
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        ECSLog.d(ECSLog.LOG, "Request URL = " + store.getCurrentCartUrl());
        return store.getCurrentCartUrl();
    }
}
