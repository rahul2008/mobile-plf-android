package com.philips.cdp.di.iap.model;


import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.response.cart.CreateCartData;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;


public class CartModel extends AbstractModel {
    public final static String PRODUCT_CODE = "code";
    public final static String PRODUCT_QUANTITY = "qty";

    public CartModel(final Store store, Map<String,String> query) {
        super(store,query);
    }

    @Override
    public String getUrl(int requestCode) {
        switch (requestCode) {
            case RequestCode.ADD_TO_CART:
                return NetworkConstants.addToCartUrl;

            case RequestCode.UPDATE_PRODUCT_COUNT:
                //TODO : Need to update real time url
                return null;
        }
        return null;
    }

    @Override
    public Object parseResponse(int requestCode, Object response) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return new Gson().fromJson(response.toString(), GetCartData.class);
            case RequestCode.ADD_TO_CART:
                return new Gson().fromJson(response.toString(), AddToCartData.class);
            case RequestCode.CREATE_CART:
                return new Gson().fromJson(response.toString(), CreateCartData.class);
        }
        return null;
    }

    @Override
    public int getMethod(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return Request.Method.GET;
            case RequestCode.CREATE_CART:
            case RequestCode.ADD_TO_CART:
                return Request.Method.POST;
            case RequestCode.UPDATE_PRODUCT_COUNT:
                return Request.Method.PUT;
        }
        return 0;
    }

    @Override
    public Map<String, String> requestBody(int requestCode) {
        //TODO: Move this with params validation
        if(requestCode == RequestCode.ADD_TO_CART) {
            return getAddToCartPayload();
        }
        if (this.params != null) {
            if (requestCode == RequestCode.UPDATE_PRODUCT_COUNT) {
                return getProductCountUpdatePayload();
            }
        }
        return null;
    }

    private Map<String, String> getProductCountUpdatePayload() {
        Map<String, String> params = new HashMap<String,String>();
        params.put(PRODUCT_CODE, this.params.get(PRODUCT_CODE));
        params.put(PRODUCT_QUANTITY, this.params.get(PRODUCT_QUANTITY));
        return params;
    }

    private Map<String, String> getAddToCartPayload() {
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
            case RequestCode.UPDATE_PRODUCT_COUNT:
                if (params == null || !params.containsKey(PRODUCT_CODE) ||
                        !params.containsKey(PRODUCT_QUANTITY)) {
                    throw new RuntimeException("product code and quantity must be supplied");
                }
                String productCode = params.get(PRODUCT_CODE);
                return String.format(NetworkConstants.updateProductCount, productCode);
            case RequestCode.CREATE_CART:
                return NetworkConstants.createCartUrl;
        }
        return null;
    }
}