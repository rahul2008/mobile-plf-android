package com.philips.cdp.di.ecs.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSConstant;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetECSShoppingCartsRequest extends OAuthAppInfraAbstractRequest {

    private final ECSCallback<ECSShoppingCart, Exception> ecsCallback;


    public GetECSShoppingCartsRequest(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {

        this.ecsCallback = ecsCallback;

    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }

    @Override
    public String getURL() {
       String url =  new ECSURLBuilder().getCartsUrl();
        Log.d("Cart Url", url);
        return url;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error, 9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            ECSShoppingCart resp = new Gson().fromJson(response.toString(),
                    ECSShoppingCart.class);
            if (null != resp) {
                ecsCallback.onResponse(resp);
            } else {
                ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_GET_CART_FAILED), 8999);
            }
        }
    }

}
