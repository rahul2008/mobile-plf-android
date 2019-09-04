package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;


import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SetDeliveryAddressRequest  extends OAuthAppInfraAbstractRequest implements Response.Listener<String>  {


    public static final String ADDRESS_ID = "addressId";

    private final ECSCallback<Boolean,Exception> ecsCallback;
    private final String addressID;

    public SetDeliveryAddressRequest(String addressID, ECSCallback<Boolean, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
        this.addressID = addressID;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getSetDeliveryAddressUrl();
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ADDRESS_ID, addressID);
        return params;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }

    @Override
    public void onResponse(String response) {
        if(null!=response && response.isEmpty()) {
        ecsCallback.onResponse(true);
        }else{
            ecsCallback.onResponse(false);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessageForAddress(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }
}
