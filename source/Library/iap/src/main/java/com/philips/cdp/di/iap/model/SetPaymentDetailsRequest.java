package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;

public class SetPaymentDetailsRequest extends AbstractModel{

    public SetPaymentDetailsRequest(final StoreSpec store, final Map<String, String> query,
                                    final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, this.params.get(ModelConstants.PAYMENT_DETAILS_ID));
        return params;
    }

    @Override
    public String getUrl() {
        return store.getSetPaymentDetailsUrl();
    }
}
