package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
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

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;

public class CreateECSShoppingCartRequest extends OAuthAppInfraAbstractRequest {
    private final ECSCallback<ECSShoppingCart, Exception> eCSCallback;


    public CreateECSShoppingCartRequest(ECSCallback<ECSShoppingCart, Exception> eCSCallback) {
        this.eCSCallback = eCSCallback;
    }



    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getCreateCartUrl();
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {

        eCSCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),7999);
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        if(response!=null){
            ECSShoppingCart resp = new Gson().fromJson(response.toString(),
                    ECSShoppingCart.class);
            if(null!=resp && null!=resp.getGuid() && !resp.getGuid().isEmpty()) {
                eCSCallback.onResponse(resp);
            }else {
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_CART_CANNOT_BE_CREATED) ,response.toString(),7999);
            }
        }else{
            eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_CART_CANNOT_BE_CREATED) ,null,7999);
        }
    }




}
