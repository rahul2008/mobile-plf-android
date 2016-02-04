package com.philips.cdp.di.iap.model;

import android.os.Bundle;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;


public class CartModel extends AbstractModel {
    public CartModel(final Store store) {
        super(store);
    }

    @Override
    public String getUrl(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
        }
        return null;
    }

    @Override
    public Object parseResponse(int requestCode, Object response) {
        return new Gson().fromJson(response.toString(), GetCartData.class);
    }

    @Override
    public int getMethod(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return Request.Method.GET;
            case RequestCode.CREATE_CART:
                return Request.Method.POST;
            case RequestCode.ADD_TO_CART:
                return Request.Method.POST;
        }
        return 0;
    }

    @Override
    public Bundle requestBody() {
        Bundle params = null;
        return params;
    }

    @Override
    public String getTestUrl(final int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
        }
        return null;}
}