package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;

import java.util.HashMap;
import java.util.Map;

public class RemoveVoucherRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {

    private final String mVoucherCode;
    private final ECSCallback<Boolean,Exception> ecsCallback;

    public RemoveVoucherRequest(String mVoucherCode, ECSCallback<Boolean, Exception> ecsCallback) {
        this.mVoucherCode = mVoucherCode;
        this.ecsCallback = ecsCallback;
    }

    @Override
    public void onResponse(String response) {
        ecsCallback.onResponse(true);
    }

    @Override
    public int getMethod() {
        return  Request.Method.DELETE;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getDeleteVoucherUrl(mVoucherCode);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ecsCallback.onFailure(error,"Error Deleting voucher",9000);
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }
}
