package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.network.ModelConstants;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;

public class ECSCartUpdateProductQuantityRequest extends OAuthAppInfraAbstractRequest {

    private final Product product;
    private final int quantity;
    private final ECSCallback<Boolean,Exception> ecsCallback;

    public ECSCartUpdateProductQuantityRequest(Product product, int quantity, ECSCallback<Boolean, Exception> ecsCallback) {
        this.product = product;
        this.quantity = quantity;
        this.ecsCallback = ecsCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getUpdateProductUrl(product.getCode());
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),10999);
    }

    @Override
    public void onResponse(JSONObject response) {

        if(response!=null && response.length()!=0){
            ecsCallback.onResponse(true);
        }
            ecsCallback.onResponse(false);
    }

    @Override
    public JSONObject getJSONRequest() {
        Map<String, String> params = new HashMap<>();
        params.put(ModelConstants.PRODUCT_CODE, product.getCode());
        params.put(ModelConstants.PRODUCT_QUANTITY,""+quantity);
        return new JSONObject(params);
    }
}
