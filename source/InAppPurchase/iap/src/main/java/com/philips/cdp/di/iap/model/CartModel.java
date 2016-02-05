package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;


public class CartModel extends AbstractModel {
    public CartModel(final Store store) {
        super(store);
    }

    @Override
    public String getUrl(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
            case RequestCode.ADD_TO_CART:
                return NetworkConstants.addToCartUrl;
        }
        return null;
    }

    @Override
    public Object parseResponse(int requestCode, Object response) {
        switch (requestCode){
            case RequestCode.GET_CART:
                return new Gson().fromJson(response.toString(), GetCartData.class);
            case RequestCode.ADD_TO_CART:
                return new Gson().fromJson(response.toString(), AddToCartData.class);
        }
        return null;
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
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("code", "HX8331_11");
        return params;
    }

    @Override
    public String getTestUrl(final int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
            case RequestCode.ADD_TO_CART:
                return NetworkConstants.addToCartUrl;
        }
        return null;}
}