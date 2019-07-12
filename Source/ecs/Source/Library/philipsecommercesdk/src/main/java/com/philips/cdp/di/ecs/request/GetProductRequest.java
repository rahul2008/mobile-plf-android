package com.philips.cdp.di.ecs.request;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.ProductsEntity;
import com.philips.cdp.di.ecs.network.ModelConstants;
import com.philips.cdp.di.ecs.network.NetworkConstants;
import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.request.ProductSummaryListServiceDiscoveryRequest;
import com.philips.cdp.di.ecs.prx.request.PrxRequest;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetProductRequest extends AppInfraAbstractRequest implements PrxRequest.OnUrlReceived{

    private final int currentPage;
    private int pageSize = 20;
    private final ECSCallback<Products,Exception> ecsCallback;
    private Products mProducts;


    public GetProductRequest(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {
        this.currentPage = currentPage;
        this.ecsCallback = ecsCallback;
        if(pageSize !=0){
            this.pageSize = pageSize;
        }
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public String getURL() {
        return new ECSURLBuilder().getProductCatalogUrl(currentPage,pageSize);
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
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null) {
            mProducts = new Gson().fromJson(response.toString(),
                    Products.class);

            List<ProductsEntity> productsEntities = mProducts.getProducts();
            ArrayList<String> ctns = new ArrayList<>();

            for (ProductsEntity productsEntity:productsEntities){
                ctns.add(productsEntity.getCode());
            }
            //Call PRX here
            ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = prepareProductSummaryListRequest(ctns);
            productSummaryListServiceDiscoveryRequest.getRequestUrlFromAppInfra(this);
        }
    }

    private ProductSummaryListServiceDiscoveryRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListServiceDiscoveryRequest productSummaryListServiceDiscoveryRequest = new ProductSummaryListServiceDiscoveryRequest(ctns, PrxConstants.Sector.B2C, PrxConstants.Catalog.CONSUMER, null);
        productSummaryListServiceDiscoveryRequest.setRequestTimeOut(NetworkConstants.DEFAULT_TIMEOUT_MS);
        return productSummaryListServiceDiscoveryRequest;
    }

    @Override
    public void onSuccess(String url) {
        new GetProductSummaryListRequest(url,mProducts,ecsCallback).executeRequest();
    }

    @Override
    public void onError(ERRORVALUES errorvalues, String s) {

        Log.d(getClass().getSimpleName(),"Service Discovery Error");
    }
}
