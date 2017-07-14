/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.carts.CreateCartData;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPLog;

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
        IAPLog.d(IAPLog.LOG, "POST");
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        IAPLog.d(IAPLog.LOG, "Request URL = "+store.getCreateCartUrl());
        return store.getCreateCartUrl();
    }
}
