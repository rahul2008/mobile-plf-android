package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UpdateAddressRequest extends AbstractModel{

    public UpdateAddressRequest(final Store store, final Map<String, String> query, DataLoadListener loadListener) {
        super(store, query, loadListener);
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> payload = new HashMap<>();
        payload.put(ModelConstants.FIRST_NAME, this.params.get(ModelConstants.FIRST_NAME));
        payload.put(ModelConstants.LAST_NAME, this.params.get(ModelConstants.LAST_NAME));
        payload.put(ModelConstants.TITLE_CODE, this.params.get(ModelConstants.TITLE_CODE));
        payload.put(ModelConstants.COUNTRY_ISOCODE, this.params.get(ModelConstants.COUNTRY_ISOCODE));
        payload.put(ModelConstants.LINE_1, this.params.get(ModelConstants.LINE_1));
        payload.put(ModelConstants.LINE_2, this.params.get(ModelConstants.LINE_2));
        payload.put(ModelConstants.POSTAL_CODE, this.params.get(ModelConstants.POSTAL_CODE));
        payload.put(ModelConstants.TOWN, this.params.get(ModelConstants.TOWN));
        payload.put(ModelConstants.PHONE_NUMBER, this.params.get(ModelConstants.PHONE_NUMBER));
        return payload;
    }

    @Override
    public String getTestUrl() {
        if (params == null || !params.containsKey(ModelConstants.ADDRESS_ID) ||
                !params.containsKey(ModelConstants.ADDRESS_ID)) {
            throw new RuntimeException("Address Id must be specified");
        }
        String addressId = params.get(ModelConstants.ADDRESS_ID);
        String test = String.format(NetworkConstants.UPDATE_OR_DELETE_ADDRESS_URL, addressId);
        return String.format(NetworkConstants.UPDATE_OR_DELETE_ADDRESS_URL, addressId);
    }
}
