package com.philips.cdp.di.ecs.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSErrors.logDetailErrorMessage;
import static com.philips.cdp.di.ecs.error.ECSErrors.getErrorMessage;

public class GetECSShoppingCartsRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject>{

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
        ecsCallback.onFailure(getErrorMessage(error), logDetailErrorMessage(error),8999);
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            ECSShoppingCart resp = new Gson().fromJson(response.toString(),
                    ECSShoppingCart.class);
            if (null != resp && null!=resp.getGuid() && !resp.getGuid().isEmpty()) {
                ecsCallback.onResponse(resp);
            } else {
                ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_GET_CART_FAILED), response.toString(),8999);
            }
        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_GET_CART_FAILED), null,8999);
        }
    }


    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
