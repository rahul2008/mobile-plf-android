package com.philips.cdp.di.ecs.request;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.ProductsEntity;
import com.philips.cdp.di.ecs.model.summary.Data;
import com.philips.cdp.di.ecs.model.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.network.ModelConstants;
import com.philips.cdp.di.ecs.prx.serviceDiscovery.ProductSummaryListServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.di.ecs.prx.serviceDiscovery.ServiceDiscoveryRequest.OnUrlReceived;


public class GetProductRequest extends AppInfraAbstractRequest implements OnUrlReceived {

    private final int currentPage;
    private int pageSize = 20;
    private final ECSCallback<Products, Exception> ecsCallback;
    private Products mProducts;


    public GetProductRequest(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {
        this.currentPage = currentPage;
        this.ecsCallback = ecsCallback;
        if (pageSize != 0) {
            this.pageSize = pageSize;
        }
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
        ecsCallback.onFailure(ECSErrors.getNetworkErrorMessage(error), 4999);
    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            mProducts = new Gson().fromJson(response.toString(),
                    Products.class);

            List<ProductsEntity> productsEntities = mProducts.getProducts();
            ArrayList<String> ctns = new ArrayList<>();

            for (ProductsEntity productsEntity : productsEntities) {
                ctns.add(productsEntity.getCode());
            }
            //Call PRX here
            ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = prepareProductSummaryListRequest(ctns);
            productSummaryListServiceDiscoveryRequest.getRequestUrlFromAppInfra(this);
        }
    }

    private ProductSummaryListServiceDiscoveryRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = new ProductSummaryListServiceDiscoveryRequest(ctns);
        return productSummaryListServiceDiscoveryRequest;
    }

    @Override
    public void onSuccess(String url) {
        new GetProductSummaryListRequest(url, new ECSCallback<ECSProductSummary, Exception>() {
            @Override
            public void onResponse(ECSProductSummary ecsProductSummary) {

                HashMap<String, Data> summaryCtnMap = new HashMap<>();

                if (ecsProductSummary.isSuccess()) {
                    for (Data data : ecsProductSummary.getData()) {
                        summaryCtnMap.put(data.getCtn(), data);
                    }
                }

                for (ProductsEntity productsEntity : mProducts.getProducts()) {
                    Data productSummaryData = summaryCtnMap.get(productsEntity.getCode());
                    productsEntity.setSummary(productSummaryData);
                }

                ecsCallback.onResponse(mProducts);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

                ecsCallback.onFailure(error, 9000);
            }
        }).executeRequest();
    }

    @Override
    public void onError(ERRORVALUES errorvalues, String s) {

        Log.d(getClass().getSimpleName(), "Service Discovery Error");
    }

}
