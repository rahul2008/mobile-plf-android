package com.philips.cdp.di.ecs.request;

import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorBuilder;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.error.ECSErrors;

import org.json.JSONObject;

public class GetDeliveryModesRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject> {

    private final ECSCallback<GetDeliveryModes, Exception> ecsCallback;

    public GetDeliveryModesRequest(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
    }

    @Override
    public void onResponse(JSONObject response) {
        Pair<GetDeliveryModes, ECSError> deliveryModesECSErrorPair = new ECSErrorBuilder().getDeliveryModesECSErrorPair(response);
        if (deliveryModesECSErrorPair.first != null) {
            ecsCallback.onResponse(deliveryModesECSErrorPair.first);
        } else {
            ecsCallback.onFailure(deliveryModesECSErrorPair.second.getException(), deliveryModesECSErrorPair.second.getErrorMessage(), deliveryModesECSErrorPair.second.getErrorcode());
        }

    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getDeliveryModesUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        String errorMessage = ECSErrors.logDetailErrorMessage(error);
        ecsCallback.onFailure(error, errorMessage, 9000);
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
