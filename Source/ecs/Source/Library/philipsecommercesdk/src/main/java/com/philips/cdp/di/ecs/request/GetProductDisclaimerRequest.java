package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.asset.AssetModel;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.model.disclaimer.DisclaimerModel;
import com.philips.cdp.di.ecs.model.disclaimer.Disclaimers;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import static com.philips.cdp.di.ecs.util.ECSErrors.getNetworkErrorMessage;

public class GetProductDisclaimerRequest extends AppInfraAbstractRequest {

    private final String assetUrl;
    private final ECSCallback<Disclaimers,Exception> ecsCallback;


    public GetProductDisclaimerRequest(String assetUrl, ECSCallback<Disclaimers, Exception> ecsCallback) {
        this.assetUrl = assetUrl;
        this.ecsCallback = ecsCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return assetUrl;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(getNetworkErrorMessage(error),5999);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null){
            DisclaimerModel resp = new Gson().fromJson(response.toString(),
                    DisclaimerModel.class);
            if(null!=resp.getData() && null!=resp.getData().getDisclaimers()) {
                Disclaimers disclaimers = resp.getData().getDisclaimers();
                ecsCallback.onResponse(disclaimers);
            }else {
                ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_UNKNOWN_ERROR), 5999);
            }
        }

    }
}
