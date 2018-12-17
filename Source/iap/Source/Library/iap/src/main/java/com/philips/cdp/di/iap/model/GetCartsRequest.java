/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.carts.CartsEntity;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.Map;

public class GetCartsRequest extends AbstractModel {
    public GetCartsRequest(StoreListener store, Map<String, String> query,
                           DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), CartsEntity.class);
    }

    @Override
    public int getMethod() {
        IAPLog.d(IAPLog.LOG, "GET");
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        IAPLog.d(IAPLog.LOG, "Request URL = " + store.getCartsUrl());
        return store.getCartsUrl();
    }
}