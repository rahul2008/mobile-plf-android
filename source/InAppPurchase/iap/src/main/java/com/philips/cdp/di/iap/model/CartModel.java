package com.philips.cdp.di.iap.model;

import com.android.volley.Request;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartModel implements ModelQuery {
    @Override
    public String getUrl(int reqeustCode) {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String reqeustBody() {
        return null;
    }
}
