/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.addresses.GetShippingAddressData;
import com.ecs.demouapp.ui.store.StoreListener;
import com.google.gson.Gson;


import java.util.Map;

public class GetAddressRequest extends AbstractModel {

    public GetAddressRequest(final StoreListener store, final Map<String, String> query, DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), GetShippingAddressData.class);
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
        return store.getAddressesUrl();
    }
}
