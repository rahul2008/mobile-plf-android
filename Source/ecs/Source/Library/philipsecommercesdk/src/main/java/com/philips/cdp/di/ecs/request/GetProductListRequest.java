package com.philips.cdp.di.ecs.request;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.network.ModelConstants;
import com.philips.cdp.di.ecs.store.ECSURLBuilder;
import com.philips.cdp.di.ecs.util.ECSErrorReason;
import com.philips.cdp.di.ecs.util.ECSErrors;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.di.ecs.util.ECSErrors.getDetailErrorMessage;
import static com.philips.cdp.di.ecs.util.ECSErrors.getErrorMessage;


public class GetProductListRequest extends AppInfraAbstractRequest {

    private final int currentPage;
    private int pageSize = 20;
    private final ECSCallback<Products, Exception> ecsCallback;
    private Products mProducts;


    public GetProductListRequest(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {
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
        ecsCallback.onFailure(getErrorMessage(error),getDetailErrorMessage(error),4999);

    }

    @Override
    public void onResponse(JSONObject response) {
        if (response != null) {
            mProducts = new Gson().fromJson(response.toString(),
                    Products.class);

            List<Product> productsEntities = mProducts.getProducts();
            ArrayList<String> ctns = new ArrayList<>();

            if(null!=productsEntities && !productsEntities.isEmpty()) {
                for (Product product : productsEntities) {
                    ctns.add(product.getCode());
                }
                ecsCallback.onResponse(mProducts);
            }else{
                ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_NO_PRODUCT_FOUND), response.toString(),4999);
            }

        }else{
            ecsCallback.onFailure(new Exception(ECSErrorReason.ECS_NO_PRODUCT_FOUND), null,4999);
        }
    }
}
