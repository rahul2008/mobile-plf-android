package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.philips.cdp.di.ecs.util.ECSErrors.getNetworkErrorMessage;

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
        //return new ECSURLBuilder().getRawConfigUrl();
        return "https://acc.us.pil.shop.philips.com/pilcommercewebservices/v2/inAppConfig/en_US/IAP_MOB_PHC?lang=en_UT";
    }

    @Override
    public void onErrorResponse(VolleyError error) {

      eCSCallback.onFailure(getNetworkErrorMessage(error),3999);
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
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_UNSUPPORTED_LOCALE),3001);
            }else if(response.has("net")) {
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),3999);
            }
        }else{
            eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),3999);
        }
    }

}
