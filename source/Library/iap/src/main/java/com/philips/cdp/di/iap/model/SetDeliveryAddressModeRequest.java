package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SetDeliveryAddressModeRequest extends AbstractModel {

    public SetDeliveryAddressModeRequest(final Store store, final Map<String, String> query, DataLoadListener listener) {
        super(store, query,listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return IAPConstant.IAP_SUCCESS;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        HashMap<String, String> query = new HashMap<String, String>();
        if ("GB".equals(store.getCountry())) {
            query.put(ModelConstants.DEVLVERY_MODE_ID, "standard-gross");
        } else {
            query.put(ModelConstants.DEVLVERY_MODE_ID, "standard-net");
        }
        return query;
    }

    @Override
    public String getUrl() {
        return store.getUpdateDeliveryModeUrl();
    }
}
