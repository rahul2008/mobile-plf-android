/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.os.Message;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.ShoppingCart.PRXProductDataBuilder;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.Map;

public class CartCurrentInfoRequest extends AbstractModel {
    public CartCurrentInfoRequest(Store store, Map<String, String> query,
                                  DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    protected void onPostSuccess(Message msg) {

        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            // TODO: 3/4/2016  
        } else if (msg.obj instanceof Carts) {
            Carts cartData = (Carts) msg.obj;
            if (cartData.getCarts().get(0).getEntries() == null) {
                Message msgResult = Message.obtain(msg);
                msgResult.obj = null;
                mDataloadListener.onModelDataLoadFinished(msgResult);
            } else {
                PRXProductDataBuilder builder = new PRXProductDataBuilder(mContext, cartData,
                        mDataloadListener);
                builder.build();
            }
        } else if (msg.obj instanceof VolleyError) {
            // TODO: 3/4/2016  
        }
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), Carts.class);
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