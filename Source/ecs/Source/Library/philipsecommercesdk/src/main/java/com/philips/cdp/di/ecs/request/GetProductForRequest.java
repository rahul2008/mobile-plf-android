package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONObject;

public class GetProductForRequest extends AppInfraAbstractRequest {

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
        ecsCallback.onFailure(ECSErrors.getNetworkErrorMessage(error), 27999);
    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        Product product = null;
        if (response != null) {
            product = new Gson().fromJson(response.toString(),
                    Product.class);
        }
        if (null != product) {
            ecsCallback.onResponse(product);
        } else {
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_GIVEN_PRODUCT_NOT_FOUND), 27999);
        }
    }
}
