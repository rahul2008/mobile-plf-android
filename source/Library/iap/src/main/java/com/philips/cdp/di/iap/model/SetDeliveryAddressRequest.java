package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SetDeliveryAddressRequest extends AbstractModel {

    public SetDeliveryAddressRequest(final StoreSpec store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public Object parseResponse(final Object response) {
        return IAPConstant.IAP_SUCCESS;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.ADDRESS_ID, this.params.get(ModelConstants.ADDRESS_ID));
        return params;
    }

    @Override
    public String getUrl() {
        return store.getSetDeliveryAddressUrl();
    }
}
