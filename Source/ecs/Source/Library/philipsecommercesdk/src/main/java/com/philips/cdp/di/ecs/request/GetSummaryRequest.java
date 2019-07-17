package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ProductSummaryListServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ServiceDiscoveryRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetSummaryRequest extends AppInfraAbstractRequest implements ServiceDiscoveryRequest.OnUrlReceived {

    ArrayList<String> mCtns;
    private final ECSCallback<ECSProductSummary, Exception> mECSCallback;

    public GetSummaryRequest(ArrayList<String> ctns, ECSCallback<ECSProductSummary, Exception> ecsCallback) {
        this.mCtns = ctns;
        this.mECSCallback = ecsCallback;
    }

    @Override
    public void onSuccess(String url) {

    }

    @Override
    public int getMethod() {
        {
            return Request.Method.GET;
        }
    }

    @Override
    public String getURL() {
        return null;
    }

    /**
     * Callback method that an error has been occurred with the provided error code and optional
     * user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {

    }

    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {

    }

    @Override
    public void onError(ERRORVALUES errorvalues, String s) {

    }

    private ProductSummaryListServiceDiscoveryRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = new ProductSummaryListServiceDiscoveryRequest(ctns);
        return productSummaryListServiceDiscoveryRequest;
    }
}
