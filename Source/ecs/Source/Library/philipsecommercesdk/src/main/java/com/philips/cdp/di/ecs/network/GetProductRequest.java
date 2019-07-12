package com.philips.cdp.di.ecs.network;

import android.content.Context;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.ProductsEntity;
import com.philips.cdp.di.ecs.prx.error.PrxError;
import com.philips.cdp.di.ecs.prx.prxclient.PRXDependencies;
import com.philips.cdp.di.ecs.prx.prxclient.PrxConstants;
import com.philips.cdp.di.ecs.prx.prxclient.RequestManager;
import com.philips.cdp.di.ecs.prx.request.ProductSummaryListRequest;
import com.philips.cdp.di.ecs.prx.response.ResponseData;
import com.philips.cdp.di.ecs.prx.response.ResponseListener;
import com.philips.cdp.di.ecs.prx.summary.Data;
import com.philips.cdp.di.ecs.prx.summary.ECSProductSummary;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.rest.TokenProviderInterface;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetProductRequest extends AppInfraAbstractRequest {

    private final int currentPage;
    private int pageSize = 20;
    private final ECSCallback<Products,Exception> ecsCallback;
    private final Context context;

    public GetProductRequest(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback, Context context) {
        this.currentPage = currentPage;
        this.ecsCallback = ecsCallback;
        this.context = context;
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
    public JSONObject getJSONRequest() {
        return null;
    }

    @Override
    public Response.Listener<JSONObject> getJSONSuccessResponseListener() {
        return this;
    }

    @Override
    public Response.ErrorListener getJSONFailureResponseListener() {
        return this;
    }

    @Override
    public Map<String, String> getHeader() {
        return null;
    }

    @Override
    public Map<String, String> getParams() {
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.CURRENT_PAGE, String.valueOf(currentPage));
        query.put(ModelConstants.PAGE_SIZE, String.valueOf(pageSize));
        return query;
    }

    @Override
    public TokenProviderInterface getTokenProviderInterface() {
        return this;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        setServerError(error);
        ecsCallback.onFailure(error,9000);
    }

    @Override
    public void onResponse(JSONObject response) {
        if(response!=null) {
            Products resp = new Gson().fromJson(response.toString(),
                    Products.class);

            List<ProductsEntity> products = resp.getProducts();
            ArrayList<String> ctns = new ArrayList<>();

            for (ProductsEntity productsEntity:products){
                ctns.add(productsEntity.getCode());
            }
            //Call PRX here
            ProductSummaryListRequest productSummaryListRequest = prepareProductSummaryListRequest(ctns);
            executePRXRequest(productSummaryListRequest,resp);
        }
    }

    @Override
    public Token getToken() {
        return null;
    }

    private void setServerError(VolleyError error) {

        if (error.networkResponse != null) {
            final String encodedString = Base64.encodeToString(error.networkResponse.data, Base64.DEFAULT);
            final byte[] decode = Base64.decode(encodedString, Base64.DEFAULT);
            final String errorString = new String(decode);

            System.out.println("Print volley error"+errorString);
        }
    }

    private ProductSummaryListRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListRequest productSummaryListRequest = new ProductSummaryListRequest(ctns, PrxConstants.Sector.B2C, PrxConstants.Catalog.CONSUMER, null);
        productSummaryListRequest.setRequestTimeOut(NetworkConstants.DEFAULT_TIMEOUT_MS);
        return productSummaryListRequest;
    }

    protected void executePRXRequest(final ProductSummaryListRequest productSummaryListBuilder, Products resp) {
        RequestManager mRequestManager = new RequestManager();
        PRXDependencies prxDependencies = new PRXDependencies(context, ECSConfig.INSTANCE.getAppInfra(), "iap");
        mRequestManager.init(prxDependencies);
        mRequestManager.executeRequest(productSummaryListBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                if(responseData!=null) {
                    ECSProductSummary ecsProductSummary = (ECSProductSummary) responseData;

                    HashMap<String,Data> summaryCtnMap = new HashMap<>();

                    if (ecsProductSummary.isSuccess()) {
                        for (Data data : ecsProductSummary.getData()) {
                            summaryCtnMap.put(data.getCtn(),data);
                        }
                    }

                    for(ProductsEntity productsEntity:resp.getProducts()){
                        Data productSummaryData = summaryCtnMap.get(productsEntity.getCode());
                        productsEntity.setSummary(productSummaryData);
                    }
                }

                ecsCallback.onResponse(resp);
            }
            @Override
            public void onResponseError(final PrxError prxError) {
                prxError.getDescription();
                System.out.println(prxError.getDescription());

            }
        });
    }
}
