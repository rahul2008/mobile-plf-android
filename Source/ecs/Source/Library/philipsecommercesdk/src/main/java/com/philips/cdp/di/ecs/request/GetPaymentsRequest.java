package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.payment.PaymentMethods;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;

public class GetPaymentsRequest extends OAuthAppInfraAbstractRequest implements Response.Listener<JSONObject>{

    private final ECSCallback<PaymentMethods , Exception> ecsCallback;

    public GetPaymentsRequest(ECSCallback<PaymentMethods, Exception> ecsCallback) {
        this.ecsCallback = ecsCallback;
    }

    @Override
    public void onResponse(JSONObject response) {
        PaymentMethods getPayment = null;
        Exception exception = null;
        try {
                getPayment = new Gson().fromJson(response.toString(),
                        PaymentMethods.class);
        }catch(Exception e){
            exception=e;
        }
        // TODO to check response json when there is no payment added
        if(null==exception && null!=getPayment) {
            ecsCallback.onResponse(getPayment);
        } else {
            ECSError ecsError = getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong,exception,response.toString());
            ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getPaymentDetailsUrl();
    }

    @Override
    public Map<String, String> getHeader() {
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("Authorization", "Bearer " + ECSConfig.INSTANCE.getAccessToken());
        return authMap;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
