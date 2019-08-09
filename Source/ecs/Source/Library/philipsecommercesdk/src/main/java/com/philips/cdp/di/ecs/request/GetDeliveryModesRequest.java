package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetDeliveryModesRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject> {

    private final ECSCallback<GetDeliveryModes,Exception> ecsCallback;

    public GetDeliveryModesRequest(ECSCallback<GetDeliveryModes, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
    }

    @Override
    public void onResponse(JSONObject response) {
        Exception exception=null;
        GetDeliveryModes getDeliveryModes=null;
        try {
            getDeliveryModes  = new Gson().fromJson(response.toString(),
                    GetDeliveryModes.class);

        }catch (Exception e){
            exception=e;

        }
        if(null == exception && isValidDeliveryModee(getDeliveryModes)){//todo
            ecsCallback.onResponse(getDeliveryModes);
        }else {
            String errorMessage="";
            if(null!= exception){
                errorMessage= exception.getMessage();
            }else{
                errorMessage = ECSErrors.DeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorMessage();
            }
            String detailMessage = null!=response ? response.toString():"";
            ecsCallback.onFailure(new Exception(errorMessage),detailMessage,ECSErrors.DeliveryModeError.NO_DELIVERY_MODES_FOUND.getErrorCode());
        }


    }

    private boolean isValidDeliveryModee(GetDeliveryModes getDeliveryModes) {
        return getDeliveryModes != null && getDeliveryModes.getDeliveryModes() != null && getDeliveryModes.getDeliveryModes().size() != 0;
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
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
        String errorMessage = ECSErrors.getDetailErrorMessage(error);
        ecsCallback.onFailure(error,errorMessage,9000);
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
