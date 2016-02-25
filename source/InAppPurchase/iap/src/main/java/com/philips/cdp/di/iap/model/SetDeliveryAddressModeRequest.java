package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SetDeliveryAddressModeRequest extends AbstractModel {

    public SetDeliveryAddressModeRequest(final Store store, final Map<String, String> query) {
        super(store, query);
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getTestUrl() {
        return NetworkConstants.SET_DELIVERY_ADDRESS_URL;
    }
}
