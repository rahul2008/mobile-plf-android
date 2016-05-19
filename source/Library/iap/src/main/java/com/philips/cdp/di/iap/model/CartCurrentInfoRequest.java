/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.os.Message;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.CartsEntity;
import com.philips.cdp.di.iap.store.Store;

import java.util.ArrayList;
import java.util.List;
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
        //We recieve only one entity and not an array. To support multiple carts, use constructor
        // with list
        CartsEntity entity = new Gson().fromJson(response.toString(), CartsEntity.class);
        List<CartsEntity> list = new ArrayList<CartsEntity>();
        list.add(entity);
        Carts carts = new Carts(list);
        return carts;
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