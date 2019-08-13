package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class preparePaymentRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {

    private  ECSCallback<URL,Exception> ecsCallback;
    private String orderID;
    Addresses ecsAddressRequest;

    public preparePaymentRequest(String orderID, Addresses ecsAddressRequest, ECSCallback<URL, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
        this.orderID = orderID;
        this.ecsAddressRequest = ecsAddressRequest;
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(String response) {
        if(!response.isEmpty()){
            try {
                ecsCallback.onResponse(new URL(response));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),""+response,999);
        }

    }

    @Override
    public int getMethod() {
         return Request.Method.POST;
    }

    @Override
    public String getURL() {

        return new ECSURLBuilder().getMakePaymentUrl(orderID);
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public Map<String, String> getParams() {
        return ECSRequestUtility.getAddressParams(ecsAddressRequest);

    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }

    public Response.Listener<String> getStringSuccessResponseListener(){
        return this;
    }
}
