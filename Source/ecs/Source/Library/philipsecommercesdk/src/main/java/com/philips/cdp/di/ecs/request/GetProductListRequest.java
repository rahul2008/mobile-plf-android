package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.constants.ModelConstants;
import com.philips.cdp.di.ecs.error.ECSErrorEnum;
import com.philips.cdp.di.ecs.error.ECSErrorWrapper;
import com.philips.cdp.di.ecs.error.ECSNetworkError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.ECSProducts;
import com.philips.cdp.di.ecs.model.products.ECSProduct;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.di.ecs.error.ECSNetworkError.getErrorLocalizedErrorMessage;


public class GetProductListRequest extends AppInfraAbstractRequest implements Response.Listener<JSONObject>{

    private final int currentPage;
    private int pageSize = 0;
    private final ECSCallback<ECSProducts, Exception> ecsCallback;
    private ECSProducts mProducts;


    public GetProductListRequest(int currentPage, int pageSize, ECSCallback<ECSProducts, Exception> ecsCallback) {
        this.currentPage = currentPage;
        this.ecsCallback = ecsCallback;
            this.pageSize = pageSize;
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getProductCatalogUrl(currentPage, pageSize);
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(currentPage));
        query.put(ModelConstants.PAGE_SIZE, String.valueOf(pageSize));
        return query;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        ECSErrorWrapper ecsErrorWrapper = ECSNetworkError.getErrorLocalizedErrorMessage(error,this);
        ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            mProducts = new Gson().fromJson(response.toString(),
                    ECSProducts.class);
            ecsCallback.onResponse(mProducts);

        } catch (Exception exception) {
            ECSErrorWrapper ecsErrorWrapper = getErrorLocalizedErrorMessage(ECSErrorEnum.ECSsomethingWentWrong,exception,response.toString());
            ecsCallback.onFailure(ecsErrorWrapper.getException(), ecsErrorWrapper.getEcsError());
        }

    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }
}
