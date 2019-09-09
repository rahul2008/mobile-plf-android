package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfiguration;

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
        if(null!=response && response.isEmpty()) {
            // Empty response indicate success
            ecsCallback.onResponse(true);
        }else{
            ECSErrorWrapper ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,null,response);
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }
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
        ECSErrorWrapper ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(error,this);
        ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfiguration.INSTANCE.getAccessToken());
        return header;
    }
}
