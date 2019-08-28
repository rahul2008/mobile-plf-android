package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.util.ECSErrorReason;

import org.json.JSONObject;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;


public class GetProductSummaryListRequest extends AppInfraAbstractRequest implements Response.Listener<JSONObject>{

    private final String prxSummaryListURL;
    private final ECSCallback<ECSProductSummary, Exception> ecsCallback;

    public GetProductSummaryListRequest(String prxSummaryListURL, ECSCallback<ECSProductSummary, Exception> ecsCallback) {
        this.prxSummaryListURL = prxSummaryListURL;
        this.ecsCallback = ecsCallback;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return prxSummaryListURL;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessage(error);
        ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
    }

    @Override
    public void onResponse(JSONObject response) {
        ECSProductSummary ecsProductSummary = null;
        Exception exception = null;

        try {
            ecsProductSummary = new Gson().fromJson(response.toString(),
                    ECSProductSummary.class);
        } catch (Exception e) {
            exception = e;
        }
            if(null == exception && null!=ecsProductSummary.getData()) {
                ecsCallback.onResponse(ecsProductSummary);
            }else{
                ECSError ecsError = getErrorLocalizedErrorMessage(ECSErrorEnum.something_went_wrong,exception,response.toString());
                ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
            }
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
