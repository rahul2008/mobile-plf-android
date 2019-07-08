/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;


import com.android.volley.Request;
import com.ecs.demouapp.ui.response.carts.AddToCartData;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.IAPLog;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class CartAddProductRequest extends AbstractModel {
    public CartAddProductRequest(final StoreListener store, final Map<String, String> query,
                                 DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), AddToCartData.class);
    }

    @Override
    public int getMethod() {
        IAPLog.d(IAPLog.LOG, "POST");
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PRODUCT_CODE, this.params.get(ModelConstants.PRODUCT_CODE));
        return params;
    }

    @Override
    public String getUrl() {
        IAPLog.d(IAPLog.LOG, "Request URL = "+store.getAddToCartUrl());
        return store.getAddToCartUrl();
    }
}