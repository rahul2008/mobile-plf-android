/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.carts.CreateCartData;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.google.gson.Gson;


import java.util.Map;

public class CartCreateRequest extends AbstractModel {
    public CartCreateRequest(final StoreListener store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), CreateCartData.class);
    }

    @Override
    public int getMethod() {
        ECSLog.d(ECSLog.LOG, "POST");
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        ECSLog.d(ECSLog.LOG, "Request URL = "+store.getCreateCartUrl());
        return store.getCreateCartUrl();
    }
}
