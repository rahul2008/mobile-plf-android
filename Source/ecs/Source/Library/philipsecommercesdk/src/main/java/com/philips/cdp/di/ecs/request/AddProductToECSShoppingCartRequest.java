package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSErrors.getVolleyException;

public class AddProductToECSShoppingCartRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {

    private final ECSCallback<Boolean,Exception> ecsCallback;

    private final String ctn;


    public AddProductToECSShoppingCartRequest(String ctn, ECSCallback<Boolean, Exception> ecsCallback) {
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
        ecsCallback.onFailure(getVolleyException(error), 9999);
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> jsonParams = new HashMap<>();
        jsonParams.put("code", ctn);
        return jsonParams;
    }

    @Override
    public void onResponse(String response) {

        if(response!=null || response.length()!=0){
            ecsCallback.onResponse(true);
        }else{
            ecsCallback.onResponse(false);
        }
    }

    public Response.Listener<String> getStringSuccessResponseListener(){
        return this;
    }

}
