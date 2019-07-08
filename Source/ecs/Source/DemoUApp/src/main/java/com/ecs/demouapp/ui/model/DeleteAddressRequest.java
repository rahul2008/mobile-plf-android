/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.IAPLog;
import com.ecs.demouapp.ui.utils.ModelConstants;


import java.util.Map;

public class DeleteAddressRequest extends AbstractModel{

    public DeleteAddressRequest(final StoreListener store, final Map<String, String> query, DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        IAPLog.d(IAPLog.LOG, "DELETE");
        return Request.Method.DELETE;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        if (params == null || !params.containsKey(ModelConstants.ADDRESS_ID) ||
                !params.containsKey(ModelConstants.ADDRESS_ID)) {
            throw new RuntimeException("Address Id must be specified");
        }
        String addressId = params.get(ModelConstants.ADDRESS_ID);
        IAPLog.d(IAPLog.LOG, "Request URL = " + store.getEditAddressUrl(addressId));
        return store.getEditAddressUrl(addressId);
    }
}
