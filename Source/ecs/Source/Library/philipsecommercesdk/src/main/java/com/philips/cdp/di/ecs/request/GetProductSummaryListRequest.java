package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.ProductsEntity;
import com.philips.cdp.di.ecs.prx.summary.Data;
import com.philips.cdp.di.ecs.prx.summary.ECSProductSummary;

import org.json.JSONObject;

import java.util.HashMap;


public class GetProductSummaryListRequest extends AppInfraAbstractRequest {

    private final String prxSummaryListURL;
    private final Products products;
    private final ECSCallback<Products,Exception> ecsCallback;

    public GetProductSummaryListRequest(String prxSummaryListURL, Products products, ECSCallback<Products, Exception> ecsCallback) {
        this.prxSummaryListURL = prxSummaryListURL;
        this.products = products;
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
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {

        if (response != null) {
            ECSProductSummary ecsProductSummary = new Gson().fromJson(response.toString(),
                    ECSProductSummary.class);

            HashMap<String, Data> summaryCtnMap = new HashMap<>();

            if (ecsProductSummary.isSuccess()) {
                for (Data data : ecsProductSummary.getData()) {
                    summaryCtnMap.put(data.getCtn(),data);
                }
            }

            for(ProductsEntity productsEntity:products.getProducts()){
                Data productSummaryData = summaryCtnMap.get(productsEntity.getCode());
                productsEntity.setSummary(productSummaryData);
            }

            ecsCallback.onResponse(products);
        }
    }

}
