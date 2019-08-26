package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.cdp.di.ecs.error.ECSErrors;

import java.util.HashMap;
import java.util.Map;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UpdateAddressRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String>  {

    private final Addresses addresses;
    private final ECSCallback<Boolean,Exception> ecsCallback;

    public UpdateAddressRequest(Addresses addresses, ECSCallback<Boolean, Exception> ecsCallback) {
        this.addresses = addresses;
        this.ecsCallback = ecsCallback;
    }


    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getEditAddressUrl(addresses.getId());
    }

    @Override
    public Map<String, String> getParams() {

        return ECSRequestUtility.getAddressParams(addresses);
    }


    @Override
    public void onResponse(String response) {
        if(response.isEmpty()) {
            ecsCallback.onResponse(true);
        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),""+response,9000);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        System.out.println("Print error from updateAddress"+ECSErrors.logDetailErrorMessage(error));
        ecsCallback.onFailure(ECSErrors.getErrorMessage(error),ECSErrors.logDetailErrorMessage(error),9000);
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }
}
