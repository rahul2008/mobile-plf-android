package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.retailers.WebResults;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class GetRetailersInfoRequest extends AbstractModel {

    public GetRetailersInfoRequest(final Store store, final Map<String, String> query, DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), WebResults.class);
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
    public String getUrl() {
        if (params == null || !params.containsKey(ModelConstants.PRODUCT_CODE)) {
            throw new RuntimeException("CTN must be specified");
        }
        String CTN = params.get(ModelConstants.PRODUCT_CODE);
        return store.getRetailersAlterUrl(CTN);
    }
}

