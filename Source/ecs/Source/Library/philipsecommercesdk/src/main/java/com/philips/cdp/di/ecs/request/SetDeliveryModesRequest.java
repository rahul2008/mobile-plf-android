package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.cdp.di.ecs.util.ECSErrors;

import java.util.HashMap;
import java.util.Map;

public class SetDeliveryModesRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {


    private final String deliveryModeID;
    private final ECSCallback<Boolean,Exception> ecsCallback;

    public SetDeliveryModesRequest(String deliveryModeID,ECSCallback<Boolean, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
        this.deliveryModeID = deliveryModeID;
    }

    @Override
    public void onResponse(String response) {
        if(response.isEmpty()) {
            ecsCallback.onResponse(true);
        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR),""+response,9000);
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getSetDeliveryModeUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        ecsCallback.onFailure( ECSErrors.getErrorMessage(error),ECSErrors.getDetailErrorMessage(error),9000);
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ModelConstants.DELIVERY_MODE_ID, deliveryModeID);
        return params;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }
}
