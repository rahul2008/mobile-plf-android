package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;

public class AddProductToCartRequest extends OAuthAppInfraAbstractRequest {

    private final ECSCallback<Boolean,Exception> ecsCallback;

    private final String ctn;

    public AddProductToCartRequest( String ctn ,ECSCallback<Boolean, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
        this.ctn = ctn;
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getAddToCartUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),9999);
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }


    @Override
    public JSONObject getJSONRequest() {
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("code", ctn);
        return new JSONObject(jsonParams);
    }

    @Override
    public void onResponse(JSONObject response) {

        if(response!=null && response.length()!=0){
            ecsCallback.onResponse(true);
        }
    }
}
