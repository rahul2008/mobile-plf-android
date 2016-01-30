package com.philips.cdp.di.iap.model;

import android.os.Bundle;

import com.android.volley.Request;
import com.philips.cdp.di.iap.activity.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartModel implements ModelQuery {
    @Override
    public String getUrl(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return NetworkConstants.getCurrentCartUrl;
        }
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod(int requestCode) {
        switch (requestCode) {
            case RequestCode.GET_CART:
                return Request.Method.GET;
        }
        return 0;
    }

    @Override
    public Bundle requestBody() {
        Bundle params = null;
        return params;
    }
}
