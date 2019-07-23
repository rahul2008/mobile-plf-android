package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.response.HybrisConfigResponse;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import static com.philips.cdp.di.ecs.util.ECSErrors.getNetworkErrorMessage;

public class CreateCartRequest extends AppInfraAbstractRequest {
    private final ECSCallback<ECSShoppingCart, Exception> eCSCallback;


    public CreateCartRequest(ECSCallback<ECSShoppingCart, Exception> eCSCallback) {
        this.eCSCallback = eCSCallback;
    }



    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return null;
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        eCSCallback.onFailure(getNetworkErrorMessage(error),7999);
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
            if(null!=resp) {

                eCSCallback.onResponse(resp);
            }else {

                eCSCallback.onFailure(new Exception(ECSErrorReason.ECS_CART_CANNOT_BE_CREATED), 7999);
            }
        }
    }


}
