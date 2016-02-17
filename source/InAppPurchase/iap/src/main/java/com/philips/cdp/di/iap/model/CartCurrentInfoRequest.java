/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Intent;
import android.os.Message;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.ShoppingCart.PRXProductDataBuilder;
import com.philips.cdp.di.iap.activity.EmptyCartActivity;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.Map;

public class CartCurrentInfoRequest extends AbstractModel {
    public CartCurrentInfoRequest(Store store, Map<String, String> query,
                                  DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    protected void onPostSuccess(Message msg) {
        GetCartData cartData = (GetCartData) msg.obj;
        if (cartData.getCarts().get(0).getEntries() == null) {
            Message msgResult = Message.obtain(msg);
            msgResult.obj = null;
            mDataloadListener.onModelDataLoadFinished(msgResult);
        } else {
            PRXProductDataBuilder builder = new PRXProductDataBuilder(mContext, cartData,
                    mDataloadListener);
            builder.build();
        }
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), GetCartData.class);
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
    public String getTestUrl() {
        return NetworkConstants.GET_CURRENT_CART_URL;
    }
}