package com.philips.cdp.di.ecs.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetECSShoppingCartsRequest extends AppInfraAbstractRequest {

    private final ECSCallback<ECSShoppingCart, Exception> ecsCallback;

    public GetECSShoppingCartsRequest(ECSCallback<ECSShoppingCart, Exception> ecsCallback) {

        this.ecsCallback = ecsCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        Log.d("Cart Url",new ECSURLBuilder().getCartsUrl());
        return new ECSURLBuilder().getCartsUrl();
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String,String> authMap = new HashMap<>();
        authMap.put("Authorization","Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        ECSShoppingCart ecsShoppingCart = new Gson().fromJson(response.toString(), ECSShoppingCart.class);
        ecsCallback.onResponse(ecsShoppingCart);
    }

    @Override
    public Token getToken() {
        return super.getToken();
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return super.getTokenProviderInterface();
    }
}
