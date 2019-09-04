package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;


import java.util.HashMap;
import java.util.Map;

public class SetPaymentMethodRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<String> {

    private final String paymentDetailsId;
    private final ECSCallback<Boolean,Exception> ecsCallback;

    public SetPaymentMethodRequest(String paymentDetailsId,ECSCallback<Boolean, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
        this.paymentDetailsId = paymentDetailsId;
    }

    @Override
    public void onResponse(String response) {
        if(null!=response && response.isEmpty()) {
            ecsCallback.onResponse(true);
        }else{
            ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong,null,response);
            ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getSetPaymentDetailsUrl();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public Response.Listener<String> getStringSuccessResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, paymentDetailsId);
        return params;
    }

    @Override
    public Map<String, String> getHeader() {
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded");
        header.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return header;
    }
}
