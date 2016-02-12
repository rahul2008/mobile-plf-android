package com.philips.cdp.di.iap.model;

import com.philips.cdp.di.iap.store.Store;

import java.util.Map;

public abstract class AbstractModel {
    final protected Store store;
    protected Map<String,String> params;

    public AbstractModel(Store store, Map<String,String> query) {
        this.store = store;
        this.params = query;
    }

    public abstract String getProductionUrl(int requestCode);

    public abstract Object parseResponse(int requestCode, Object response);

    public abstract int getMethod(int requestCode);

    public abstract Map<String, String> requestBody(int requestCode);

    public abstract String getTestUrl(int requestCode);
}
