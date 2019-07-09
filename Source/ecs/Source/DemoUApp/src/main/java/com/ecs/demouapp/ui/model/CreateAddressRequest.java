/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.response.addresses.Addresses;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class CreateAddressRequest extends AbstractModel {

    public CreateAddressRequest(final StoreListener store, final Map<String, String> query,
                                DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), Addresses.class);
    }

    @Override
    public int getMethod() {
        ECSLog.d(ECSLog.LOG, "POST");
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> payload = new HashMap<>();
        payload.put(ModelConstants.FIRST_NAME, getValue(ModelConstants.FIRST_NAME));
        payload.put(ModelConstants.LAST_NAME, getValue(ModelConstants.LAST_NAME));
        payload.put(ModelConstants.TITLE_CODE, getValue(ModelConstants.TITLE_CODE));
        payload.put(ModelConstants.COUNTRY_ISOCODE, getValue(ModelConstants.COUNTRY_ISOCODE));
//        payload.put(ModelConstants.HOUSE_NUMBER, "12");
        payload.put(ModelConstants.LINE_1, getValue(ModelConstants.LINE_1));
        payload.put(ModelConstants.HOUSE_NO, getValue(ModelConstants.HOUSE_NO));
        payload.put(ModelConstants.LINE_2, getValue(ModelConstants.LINE_2));
        payload.put(ModelConstants.POSTAL_CODE, getValue(ModelConstants.POSTAL_CODE));
        payload.put(ModelConstants.TOWN, getValue(ModelConstants.TOWN));
        payload.put(ModelConstants.PHONE_1, getValue(ModelConstants.PHONE_1));
        payload.put(ModelConstants.PHONE_2, getValue(ModelConstants.PHONE_1));
        payload.put(ModelConstants.REGION_ISOCODE, getValue(ModelConstants.REGION_ISOCODE));
        return payload;
    }

    @Override
    public String getUrl() {
        ECSLog.d(ECSLog.LOG, "Request URL = " + store.getAddressesUrl());
        return store.getAddressesUrl();
    }

    private String getValue(String key) {
        if (params.containsKey(key) && null != params.get(key)) {
            return params.get(key);
        }
        return "";
    }
}
