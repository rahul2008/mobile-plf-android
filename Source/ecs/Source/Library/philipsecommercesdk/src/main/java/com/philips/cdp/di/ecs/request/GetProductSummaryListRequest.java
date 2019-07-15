package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONObject;


public class GetProductSummaryListRequest extends AppInfraAbstractRequest {

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
        ecsCallback.onFailure(ECSErrors.getNetworkErrorMessage(error), 9000);
    }

    @Override
    public void onResponse(JSONObject response) {

        if (response != null) {
            ECSProductSummary ecsProductSummary = new Gson().fromJson(response.toString(),
                    ECSProductSummary.class);
            ecsCallback.onResponse(ecsProductSummary);
        }
    }

}
