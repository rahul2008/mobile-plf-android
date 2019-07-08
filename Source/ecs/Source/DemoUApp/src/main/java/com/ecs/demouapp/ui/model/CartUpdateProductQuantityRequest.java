/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.carts.UpdateCartData;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.IAPLog;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class CartUpdateProductQuantityRequest extends AbstractModel {
    public CartUpdateProductQuantityRequest(final StoreListener store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), UpdateCartData.class);
    }

    @Override
    public int getMethod() {
        IAPLog.d(IAPLog.LOG, "PUT");
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<>();
        params.put(ModelConstants.PRODUCT_CODE, this.params.get(ModelConstants.PRODUCT_CODE));
        params.put(ModelConstants.PRODUCT_QUANTITY, this.params.get(ModelConstants.PRODUCT_QUANTITY));
        return params;
    }

    @Override
    public String getUrl() {
        if (params == null || !params.containsKey(ModelConstants.PRODUCT_CODE) ||
                !params.containsKey(ModelConstants.PRODUCT_QUANTITY)) {
            throw new RuntimeException("product code and quantity must be supplied");
        }
        String entrycode = params.get(ModelConstants.PRODUCT_ENTRYCODE);
        IAPLog.d(IAPLog.LOG, "Request URL = " + store.getUpdateProductUrl(entrycode));
        return store.getUpdateProductUrl(entrycode);
    }
}