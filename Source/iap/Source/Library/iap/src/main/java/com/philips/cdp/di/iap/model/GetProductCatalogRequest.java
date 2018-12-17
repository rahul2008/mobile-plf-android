/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.products.Products;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.Map;

public class GetProductCatalogRequest extends AbstractModel{

    public GetProductCatalogRequest(StoreListener store, Map<String, String> query,
                                    DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), Products.class);
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
            throw new RuntimeException("Page size not specified");
        }
        return store.getProductCatalogUrl(Integer.parseInt(params.get(ModelConstants.CURRENT_PAGE)),
                Integer.parseInt(params.get(ModelConstants.PAGE_SIZE)));

    }

}
