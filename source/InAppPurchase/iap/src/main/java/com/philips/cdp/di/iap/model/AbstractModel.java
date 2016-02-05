package com.philips.cdp.di.iap.model;

import com.philips.cdp.di.iap.store.Store;

import java.util.Map;

public abstract class AbstractModel {
    final protected Store store;

    public AbstractModel(Store store) {
        this.store = store;
    }

    public abstract String getUrl(int requestCode);

    public abstract Object parseResponse(int requestCode, Object response);

    public abstract int getMethod(int requestCode);

    public abstract Map<String, String> requestBody();

    public abstract String getTestUrl(int requestCode);
}
