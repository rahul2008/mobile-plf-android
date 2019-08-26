package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.summary.Data;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import static com.philips.cdp.di.ecs.error.ECSErrors.logDetailErrorMessage;
import static com.philips.cdp.di.ecs.error.ECSErrors.getErrorMessage;

public class GetProductForRequest extends AppInfraAbstractRequest implements Response.Listener<JSONObject>{

    private String ctn;
    private final ECSCallback<Product, Exception> ecsCallback;

    public GetProductForRequest(String ctn, ECSCallback<Product, Exception> ecsCallback) {
        this.ctn = ctn;
        this.ecsCallback = ecsCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getProduct(ctn);
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(getErrorMessage(error), logDetailErrorMessage(error),5999);
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            Product product = null;
            product = getJson().fromJson(response.toString(),
                    Product.class);
            if (null != product && null != product.getCode()) {
                ecsCallback.onResponse(product);
            } else {
                ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_GIVEN_PRODUCT_NOT_FOUND), response.toString(), 5999);
            }
        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_GIVEN_PRODUCT_NOT_FOUND), null, 5999);
        }
    }

    public Gson getJson(){

        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getName().equalsIgnoreCase("summary");
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return clazz == Data.class;
                    }
                })
                .create();
        return gson;
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
