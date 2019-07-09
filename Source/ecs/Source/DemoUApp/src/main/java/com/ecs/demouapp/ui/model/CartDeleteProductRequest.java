/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.ecs.demouapp.ui.utils.ModelConstants;


import java.util.HashMap;
import java.util.Map;

public class CartDeleteProductRequest extends AbstractModel {
    public CartDeleteProductRequest(final StoreListener store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> payload = new HashMap<>();
        payload.put(ModelConstants.PRODUCT_CODE, params.get(ModelConstants.PRODUCT_CODE));
        payload.put(ModelConstants.ENTRY_CODE, params.get(ModelConstants.ENTRY_CODE));
        payload.put(ModelConstants.PRODUCT_QUANTITY, String.valueOf(0));
        return payload;
    }

    @Override
    public String getUrl() {
        if (params == null) {
            throw new RuntimeException("Cart ID and Entry Number has to be supplied");
        }
        int entryNumber = Integer.parseInt(params.get(ModelConstants.ENTRY_CODE));
        ECSLog.d(ECSLog.LOG, "Request URL = " + store.getUpdateProductUrl(String.valueOf(entryNumber)));
        return store.getUpdateProductUrl(String.valueOf(entryNumber));
    }
}