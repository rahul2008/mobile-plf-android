/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;


import org.json.JSONObject;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;


public class GetAddressRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject> {

    private final ECSCallback<GetShippingAddressData, Exception> ecsCallback;

    public GetAddressRequest(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            GetShippingAddressData  getShippingAddressData = new Gson().fromJson(response.toString(),
                    GetShippingAddressData.class);
            ecsCallback.onResponse(getShippingAddressData);
        }catch(Exception exception){
            ECSError ecsError;
            if(response!=null) {
                ecsError = getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong, exception, response.toString());
            }else{
                ecsError = getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong, exception,exception.getMessage());
            }
            ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
        }

    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getAddressesUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}