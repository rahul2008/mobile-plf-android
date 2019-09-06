package com.philips.cdp.di.ecs.request;



import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;

import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;

import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;


import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;


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
        Exception exception = null;
                // created address response is not checked
        try {
            addresses = new Gson().fromJson(response, Addresses.class);
        }catch(Exception e){
            exception = e;

        }
        if(null== exception) {
            ecsCallback.onResponse(addresses);
        }else{
            ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,exception,response);
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
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
        ECSErrorWrapper ecsErrorWrapper = getECSError(error);
        ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());

    }

    public ECSErrorWrapper getECSError(VolleyError error){
        return getErrorLocalizedErrorMessage(error,this);
    }

    public Response.Listener<String> getStringSuccessResponseListener(){
        return this;
    }



}
