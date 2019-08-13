/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GetAddressRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject> {

    private final ECSCallback<GetShippingAddressData, Exception> ecsCallback;

    public GetAddressRequest(ECSCallback<GetShippingAddressData, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
    }

    @Override
    public void onResponse(JSONObject response) {
        GetShippingAddressData getDeliveryModes=null;
        Exception exception = null;
        try {

            getDeliveryModes = new Gson().fromJson(response.toString(),
                    GetShippingAddressData.class);
        }catch(Exception e){
            exception=e;
        }
        // TODO to check response json when there is no address added
        if(null!=exception && null!=getDeliveryModes){
            ecsCallback.onResponse(getDeliveryModes);
        }else{
            ecsCallback.onFailure(exception,""+response,9000);
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
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error, "Error fetching DeliveryMode", 9000);
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}