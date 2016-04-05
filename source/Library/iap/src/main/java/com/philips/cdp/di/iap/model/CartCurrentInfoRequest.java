/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.os.Message;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.store.Store;

import java.util.Map;

public class CartCurrentInfoRequest extends AbstractModel {
    public CartCurrentInfoRequest(Store store, Map<String, String> query,
                                  DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    protected void onPostSuccess(Message msg) {
        mDataloadListener.onModelDataLoadFinished(msg);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), Carts.class);
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
        return store.getCurrentCartDetailsUrl();
    }
}