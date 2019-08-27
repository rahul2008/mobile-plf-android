package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.asset.AssetModel;
import com.philips.cdp.di.ecs.model.asset.Assets;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import static com.philips.cdp.di.ecs.error.ECSErrors.getVolleyException;

public class GetProductAssetRequest extends AppInfraAbstractRequest implements Response.Listener<JSONObject>{

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
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null){
            AssetModel resp = new Gson().fromJson(response.toString(),
                    AssetModel.class);
           if(null!=resp.getData() && null!=resp.getData().getAssets()) {
               Assets assets = resp.getData().getAssets();
               ecsCallback.onResponse(assets);
           }else {
               ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_NO_PRODUCT_DETAIL_FOUND), 5999);
           }
        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_NO_PRODUCT_DETAIL_FOUND), 5999);
        }

    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
