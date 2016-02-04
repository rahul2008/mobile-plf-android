package com.philips.cdp.di.iap.model;

import android.os.Bundle;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

public class CartModel extends AbstractModel {
    public final static String PRODUCT_CODE = "code";
    public final static String PRODUCT_QUANTITY = "qty";

    public CartModel(final Store store, Bundle bundle) {
        super(store,bundle);
    }

    @Override
    public String getUrl(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
            case RequestCode.UPDATE_PRODUCT_COUNT:
                //TODO : Need to update real time url
                return null;
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
            case RequestCode.ADD_TO_CART:
                return Request.Method.POST;
            case RequestCode.UPDATE_PRODUCT_COUNT:
                return Request.Method.PUT;
        }
        return 0;
    }

    @Override
    public Bundle requestBody(int requestCode) {
        if (extras != null) {
            if (requestCode == RequestCode.UPDATE_PRODUCT_COUNT) {
                return getProductCountUpdateBody();
            }
        }

        return null;
    }

    private Bundle getProductCountUpdateBody() {
        Bundle params = new Bundle();
        params.putString(PRODUCT_CODE,extras.getString(PRODUCT_CODE));
        params.putString(PRODUCT_QUANTITY,extras.getString(PRODUCT_CODE));
        return params;
    }

    @Override
    public String getTestUrl(final int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
            case RequestCode.UPDATE_PRODUCT_COUNT:
                if (extras == null || !extras.containsKey("PRODUCT_CODE") ||
                        !extras.containsKey("PRODUCT_QUANTITY")) {
                    throw new RuntimeException("product code and quantity must be supplied");
                }
                String productCode = extras.getString(PRODUCT_CODE);
                int quantity = extras.getInt(PRODUCT_QUANTITY);

                return String.format(NetworkConstants.updateProductCount, productCode);
        }
        return null;
    }
}