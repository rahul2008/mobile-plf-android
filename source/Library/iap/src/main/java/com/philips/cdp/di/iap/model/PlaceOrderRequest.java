package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PlaceOrderRequest extends AbstractModel {
    public PlaceOrderRequest(final StoreSpec store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), PlaceOrder.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        String cartNumber = CartModelContainer.getInstance().getCartNumber();
        String buyDirectCartNumber = CartModelContainer.getInstance().getBuyDirectCartNumber();
        Map<String, String> params = new HashMap<>();
        if (buyDirectCartNumber != null)
            params.put(ModelConstants.CART_ID, buyDirectCartNumber);
        else
            params.put(ModelConstants.CART_ID, cartNumber);
        String securityCode = this.params.get(ModelConstants.SECURITY_CODE);
        if (securityCode != null)
            params.put(ModelConstants.SECURITY_CODE, securityCode);
        return params;
    }

    @Override
    public String getUrl() {
        return store.getPlaceOrderUrl();
    }
}
