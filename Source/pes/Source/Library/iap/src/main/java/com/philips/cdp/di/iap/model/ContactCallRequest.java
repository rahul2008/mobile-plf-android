/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.orders.ContactsResponse;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.Map;

public class ContactCallRequest extends AbstractModel {

    public ContactCallRequest(final StoreListener store, final Map<String,
            String> query, DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), ContactsResponse.class);
    }

    @Override
    public int getMethod() {
        IAPLog.d(IAPLog.LOG, "GET");
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {

        if (params == null || !params.containsKey(ModelConstants.CATEGORY)) {
            throw new RuntimeException("Category must be specified");
        }
        String category = params.get(ModelConstants.CATEGORY);
        IAPLog.d(IAPLog.LOG, "Request URL = " + store.getPhoneContactUrl(category));
        return store.getPhoneContactUrl(category);
    }
}
