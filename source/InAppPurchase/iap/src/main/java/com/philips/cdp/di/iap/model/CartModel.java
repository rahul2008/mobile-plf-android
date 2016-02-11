package com.philips.cdp.di.iap.model;


import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.response.cart.CreateCartData;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.response.cart.UpdateCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;


public class CartModel extends AbstractModel {
    public final static String PRODUCT_CODE = "code";
    public final static String PRODUCT_QUANTITY = "qty";
    public final static String PRODUCT_ENTRYCODE = "entrycode";
    public final static String ENTRY_CODE = "entrynumber";

    public CartModel(final Store store, Map<String,String> query) {
        super(store,query);
    }

    @Override
    public String getProductionUrl(int requestCode) {
        return null;
    }

    @Override
    public Object parseResponse(int requestCode, Object response) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return new Gson().fromJson(response.toString(), GetCartData.class);
            case RequestCode.UPDATE_PRODUCT_COUNT:
                return new Gson().fromJson(response.toString(), UpdateCartData.class);
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
            case RequestCode.DELETE_PRODUCT:
                return Request.Method.DELETE;
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
        if (requestCode == RequestCode.DELETE_PRODUCT) {
            return getEntryCartDetails();
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
        params.put(PRODUCT_CODE, this.params.get(PRODUCT_CODE));
        return params;
    }

    @Override
    public String getTestUrl(final int requestCode) {
        switch (requestCode) {

            case RequestCode.CREATE_CART:
                return NetworkConstants.CREATE_CART_URL;

            case RequestCode.GET_CART:
                return NetworkConstants.GET_CURRENT_CART_URL;

            case RequestCode.ADD_TO_CART:
                return NetworkConstants.ADD_TO_CART_URL;

            case RequestCode.UPDATE_PRODUCT_COUNT:
                if (params == null || !params.containsKey(PRODUCT_CODE) ||
                        !params.containsKey(PRODUCT_QUANTITY)) {
                    throw new RuntimeException("product code and quantity must be supplied");
                }
                String entrycode = params.get(PRODUCT_ENTRYCODE);
                return String.format(NetworkConstants.UPDATE_QUANTITY_URL, entrycode);

            case RequestCode.DELETE_PRODUCT:
                if (params == null) {
                    throw new RuntimeException("Cart ID and Entry Number has to be supplied");
                }
                int entryNumber = Integer.parseInt(params.get(ENTRY_CODE));
                return String.format(NetworkConstants.DELETE_PRODUCT_URL, String.valueOf(entryNumber));

        }
        return null;
    }

    private Map<String, String> getEntryCartDetails() {
        Map<String, String> payload = new HashMap<>();
        payload.put(PRODUCT_CODE, params.get(PRODUCT_CODE));
        payload.put(ENTRY_CODE, params.get(ENTRY_CODE));
        return payload;
    }
}