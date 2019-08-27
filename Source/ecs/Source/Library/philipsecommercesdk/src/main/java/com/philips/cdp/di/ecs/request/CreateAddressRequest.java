package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;

import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import java.util.HashMap;
import java.util.Map;

public class CreateAddressRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {


    Addresses ecsAddressRequest;
    private  ECSCallback<Addresses,Exception> ecsCallback;

    public CreateAddressRequest(Addresses ecsAddressRequest, ECSCallback<Addresses, Exception> ecsCallback) {
        this.ecsAddressRequest = ecsAddressRequest;
        this.ecsCallback = ecsCallback;
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(String response) {
        Addresses addresses=null;
        Exception exception = new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR);
                // created address response is not checked
        try {
            addresses = new Gson().fromJson(response, Addresses.class);
        }catch(Exception e){
            exception = e;

        }
        if(null!= exception && null!=addresses && null!=addresses.getId() ) {
            ecsCallback.onResponse(addresses);
        }else{
            ecsCallback.onFailure(exception, 12999);
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getAddressesUrl();
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }

    @Override
    public Map<String, String> getParams() {
        return ECSRequestUtility.getAddressParams(ecsAddressRequest);
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = ECSNetworkError.getECSErrorForAddress(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());

    }

    public Response.Listener<String> getStringSuccessResponseListener(){
        return this;
    }
}
