/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.orders.ContactsResponse;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.Map;

public class ContactCallRequest extends AbstractModel {

    public ContactCallRequest(final StoreListener store, final Map<String,
            String> query, DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), ContactsResponse.class);
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

        if (params == null || !params.containsKey(ModelConstants.CATEGORY)) {
            throw new RuntimeException("Category must be specified");
        }
        String category = params.get(ModelConstants.CATEGORY);
        ECSLog.d(ECSLog.LOG, "Request URL = " + store.getPhoneContactUrl(category));
        return store.getPhoneContactUrl(category);
    }
}
