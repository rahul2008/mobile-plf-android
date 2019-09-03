package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.config.HybrisConfigResponse;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;


import org.json.JSONObject;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;

public class GetConfigurationRequest extends AppInfraAbstractRequest implements Response.Listener<JSONObject>{

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
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        eCSCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public void onResponse(JSONObject response) {
        HybrisConfigResponse resp = null;
        Exception exception = null;

        try {
            resp = new Gson().fromJson(response.toString(),
                    HybrisConfigResponse.class);
        } catch (Exception e) {
            exception = e;
        }
            if(null==exception && resp.getCatalogId()!=null && resp.getRootCategory()!=null && resp.getSiteId()!=null){
                eCSCallback.onResponse(resp);
            }else if(response.has("net")) {
                ECSError ecsError = getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong,exception,response.toString());
                eCSCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
            }
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
