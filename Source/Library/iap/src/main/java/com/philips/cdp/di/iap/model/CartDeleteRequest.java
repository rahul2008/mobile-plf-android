package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Apple on 04/08/16.
 */
public class CartDeleteRequest extends AbstractModel {


    public CartDeleteRequest(StoreSpec store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.DELETE;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> payload = new HashMap<>();
        payload.put(ModelConstants.CART_ID, params.get(ModelConstants.CART_ID));
        return payload;
    }

    @Override
    public String getUrl() {
        if (params == null) {
            throw new RuntimeException("Cart ID has to be supplied");
        }
        String cartNumber =params.get(ModelConstants.CART_ID);
        return store.getDeleteCartUrl(cartNumber);
    }
}
