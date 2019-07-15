package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.asset.AssetModel;
import com.philips.cdp.di.ecs.model.asset.Assets;

import org.json.JSONObject;

public class GetProductAssetRequest extends AppInfraAbstractRequest {

    private final String assetUrl;
    private final ECSCallback<Assets,Exception> ecsCallback;


    public GetProductAssetRequest(String assetUrl, ECSCallback<Assets, Exception> ecsCallback) {
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
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null){
            AssetModel resp = new Gson().fromJson(response.toString(),
                    AssetModel.class);
            Assets assets = resp.getData().getAssets();
            ecsCallback.onResponse(assets);
        }

    }
}
