/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.model;

import android.os.Message;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.products.Products;

import java.util.Map;

public class GetProductCatalogRequest extends AbstractModel{

    public GetProductCatalogRequest(StoreSpec store, Map<String, String> query,
                                    DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    protected void onPostSuccess(Message msg) {
        mDataloadListener.onModelDataLoadFinished(msg);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), Products.class);
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
        return store.getProductCatalogUrl();
    }

}
