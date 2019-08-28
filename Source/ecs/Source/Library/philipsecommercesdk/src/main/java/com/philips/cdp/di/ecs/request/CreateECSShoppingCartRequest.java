package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;

import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;


import org.json.JSONObject;

public class CreateECSShoppingCartRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject>{
    private final ECSCallback<ECSShoppingCart, Exception> eCSCallback;


    public CreateECSShoppingCartRequest(ECSCallback<ECSShoppingCart, Exception> eCSCallback) {
        this.eCSCallback = eCSCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getCreateCartUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        eCSCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null){
            ECSShoppingCart resp = new Gson().fromJson(response.toString(),
                    ECSShoppingCart.class);
            if(null!=resp && null!=resp.getGuid() && !resp.getGuid().isEmpty()) {
                eCSCallback.onResponse(resp);
            }else {
                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_CART_CANNOT_BE_CREATED) , 7999);
            }
        }else{
            eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_CART_CANNOT_BE_CREATED) , 7999);
        }
    }


    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
