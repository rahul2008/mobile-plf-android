package com.philips.cdp.di.ecs.network;

import android.util.Log;

import com.philips.cdp.di.ecs.request.APPInfraRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;
import com.philips.platform.appinfra.rest.request.StringRequest;

public class NetworkController {


    JsonObjectRequest jsonObjectRequest;
    StringRequest  stringRequest;

    public NetworkController(APPInfraRequest appInfraJSONRequest) {
        if(appInfraJSONRequest.getJSONSuccessResponseListener()!=null) {
            jsonObjectRequest = getAppInfraJSONObject(appInfraJSONRequest);
        }else if(appInfraJSONRequest.getStringSuccessResponseListener() !=null) {
            stringRequest = getStringRequest(appInfraJSONRequest);
        }
    }

    public JsonObjectRequest getAppInfraJSONObject(APPInfraRequest appInfraJSONRequest){

//        Log.d("Network Controller URL:",appInfraJSONRequest.getURL());
        return new JsonObjectRequest(appInfraJSONRequest.getMethod(),appInfraJSONRequest.getURL(),appInfraJSONRequest.getJSONRequest()
        ,appInfraJSONRequest.getJSONSuccessResponseListener(),appInfraJSONRequest.getJSONFailureResponseListener(),
                appInfraJSONRequest.getHeader(),appInfraJSONRequest.getParams(),appInfraJSONRequest.getTokenProviderInterface());
    }

    public void executeRequest(){
        if(jsonObjectRequest!=null) {
            ECSConfig.INSTANCE.getAppInfra().getRestClient().getRequestQueue().add(jsonObjectRequest);
        }else if (stringRequest!=null){
            ECSConfig.INSTANCE.getAppInfra().getRestClient().getRequestQueue().add(stringRequest);
        }
    }

    private StringRequest getStringRequest(APPInfraRequest appInfraJSONRequest){
        return new StringRequest(appInfraJSONRequest.getMethod(),appInfraJSONRequest.getURL()
                ,appInfraJSONRequest.getStringSuccessResponseListener(),appInfraJSONRequest.getJSONFailureResponseListener(),
                appInfraJSONRequest.getHeader(),appInfraJSONRequest.getParams(),appInfraJSONRequest.getTokenProviderInterface());
    }
}
