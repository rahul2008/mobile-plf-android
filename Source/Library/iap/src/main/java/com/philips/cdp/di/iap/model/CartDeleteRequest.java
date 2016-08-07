package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;

import java.util.Map;

/**
 * Created by Apple on 04/08/16.
 */
public class CartDeleteRequest extends AbstractModel {


    public CartDeleteRequest(StoreSpec store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.DELETE;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        return store.getDeleteCartUrl();
    }
}
