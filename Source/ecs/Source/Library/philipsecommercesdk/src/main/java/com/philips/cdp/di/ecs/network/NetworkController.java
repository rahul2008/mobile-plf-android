package com.philips.cdp.di.ecs.network;

import android.util.Log;

import com.philips.cdp.di.ecs.request.APPInfraJSONRequest;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.rest.request.JsonObjectRequest;

public class NetworkController {


    JsonObjectRequest jsonObjectRequest;

    public NetworkController(APPInfraJSONRequest appInfraJSONRequest) {
        jsonObjectRequest = getAppInfraJSONObject(appInfraJSONRequest);
    }

    private JsonObjectRequest getAppInfraJSONObject(APPInfraJSONRequest appInfraJSONRequest){

        Log.d("Network Controller URL:",appInfraJSONRequest.getURL());
        return new JsonObjectRequest(appInfraJSONRequest.getMethod(),appInfraJSONRequest.getURL(),appInfraJSONRequest.getJSONRequest()
        ,appInfraJSONRequest.getJSONSuccessResponseListener(),appInfraJSONRequest.getJSONFailureResponseListener(),
                appInfraJSONRequest.getHeader(),appInfraJSONRequest.getParams(),appInfraJSONRequest.getTokenProviderInterface());
    }

    public void executeRequest(){
        ECSConfig.INSTANCE.getAppInfra().getRestClient().getRequestQueue().add(jsonObjectRequest);
    }
}
