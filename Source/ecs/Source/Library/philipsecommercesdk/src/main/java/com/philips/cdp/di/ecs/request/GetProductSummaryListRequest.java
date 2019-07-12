package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.prx.response.ResponseListener;
import com.philips.platform.appinfra.rest.TokenProviderInterface;

import org.json.JSONObject;


public class GetProductSummaryListRequest extends AppInfraAbstractRequest {

    private final String prxSummaryListURL;
    private final ResponseListener ResponseListener;

    public GetProductSummaryListRequest(String prxSummaryListURL,ResponseListener responseListener) {
        this.prxSummaryListURL = prxSummaryListURL;
        ResponseListener = responseListener;
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

    }

    @Override
    public void onResponse(JSONObject response) {

    }

}
