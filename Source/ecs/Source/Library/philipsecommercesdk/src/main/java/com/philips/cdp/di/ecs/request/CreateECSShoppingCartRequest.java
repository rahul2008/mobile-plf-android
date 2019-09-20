package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;

import com.philips.cdp.di.ecs.store.ECSURLBuilder;



import org.json.JSONObject;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;

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

        ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(error,this);
        eCSCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
    }

    @Override
    public void onResponse(JSONObject response) {
        ECSShoppingCart ecsShoppingCart = null;
        Exception exception = null;
        try {
            ecsShoppingCart = new Gson().fromJson(response.toString(),
                    ECSShoppingCart.class);
        } catch(Exception e){
            exception = e;

        }
        if(null == exception && null!= ecsShoppingCart && null!= ecsShoppingCart.getGuid() && !ecsShoppingCart.getGuid().isEmpty() ) {
            eCSCallback.onResponse(ecsShoppingCart);
        }else{
            ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,exception,response.toString());
            eCSCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
    }


    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}