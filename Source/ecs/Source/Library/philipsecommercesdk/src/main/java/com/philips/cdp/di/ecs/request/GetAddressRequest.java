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
        GetShippingAddressData getShippingAddressData=null;
        Exception exception = null;
        try {
            getShippingAddressData = new Gson().fromJson(response.toString(),
                    GetShippingAddressData.class);
        }catch(Exception e){
            exception=e;
        }
        // TODO to check response json when there is no address added
        if(null==exception && null!=getShippingAddressData){
            ecsCallback.onResponse(getShippingAddressData);
        }else{
            ECSError ecsError = getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong,exception,response.toString());
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