/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSLog;


import java.util.Map;

public class DeleteCartRequest extends AbstractModel {


    public DeleteCartRequest(StoreListener store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        ECSLog.d(ECSLog.LOG, "DELETE");
        return Request.Method.DELETE;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        ECSLog.d(ECSLog.LOG, "Request URL = " + store.getDeleteCartUrl());
        return store.getDeleteCartUrl();
    }
}
