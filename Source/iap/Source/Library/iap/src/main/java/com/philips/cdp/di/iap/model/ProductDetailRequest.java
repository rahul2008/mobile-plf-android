package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.products.ProductDetailEntity;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.Map;

/**
 * Created by 310241054 on 6/22/2016.
 */
public class ProductDetailRequest extends AbstractModel {
    public ProductDetailRequest(StoreListener store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }
    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), ProductDetailEntity.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        if (params == null) {
            throw new RuntimeException("Product CTN number has to be supplied");
        }
        return store.getSearchProductUrl(params.get(ModelConstants.PRODUCT_CODE));
    }
}
