package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;

public class GetConfigurationRequest extends AppInfraAbstractRequest {

    private final ECSCallback<HybrisConfigResponse, Exception> eCSCallback;

    public GetConfigurationRequest(ECSCallback<HybrisConfigResponse, Exception> eCSCallback) {
        this.eCSCallback = eCSCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getRawConfigUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        eCSCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),3999);
    }

    @Override
    public void onResponse(JSONObject response) {

        if(response!=null){
            if(response.has("catalogId") && response.has("rootCategory") && response.has("siteId")){

                HybrisConfigResponse resp = new Gson().fromJson(response.toString(),
                        HybrisConfigResponse.class);

                eCSCallback.onResponse(resp);
            }else if(response.has("errors") ) {
                JSONArray errors = response.optJSONArray("errors");
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_UNSUPPORTED_LOCALE),response.toString(),3001);
            }else if(response.has("net")) {
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),response.toString(),3999);
            }
        }else{
            eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),null,3999);
        }
    }

}
