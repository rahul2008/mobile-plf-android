package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.cart.EntriesEntity;
import com.philips.cdp.di.ecs.network.ModelConstants;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.HashMap;
import java.util.Map;

public class ECSUpdateCartQuantityRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {

    private final ECSCallback<Boolean, Exception> ecsCallback;
    private final EntriesEntity entriesEntity;
    private final int quantity;

    public ECSUpdateCartQuantityRequest(ECSCallback<Boolean, Exception> ecsCallback, EntriesEntity entriesEntity, int quantity) {
        this.ecsCallback = ecsCallback;
        this.entriesEntity = entriesEntity;
        this.quantity = quantity;
    }

    @Override
    public void onResponse(String response) {
        ecsCallback.onResponse(true);
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
        Map<String, String> payload = new HashMap<>();
        payload.put(ModelConstants.PRODUCT_CODE, entriesEntity.getProduct().getCode());
        payload.put(ModelConstants.ENTRY_CODE, entriesEntity.getEntryNumber()+"");
        payload.put(ModelConstants.PRODUCT_QUANTITY, String.valueOf(quantity));
        return payload;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getUpdateProductUrl(entriesEntity.getEntryNumber()+"");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error, "Error in deleting product", 9000);
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener(){
       return this;
    }
}
