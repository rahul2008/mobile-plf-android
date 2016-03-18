package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
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
    public Object parseResponse(final Object response) {
        return null;
    }

    @Override
    public int getMethod() {
        return Request.Method.PUT;
    }

    @Override
    public Map<String, String> requestBody() {
        Map<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, this.params.get(ModelConstants.FIRST_NAME));
        addressHashMap.put(ModelConstants.LAST_NAME, this.params.get(ModelConstants.LAST_NAME));
        addressHashMap.put(ModelConstants.TITLE_CODE, this.params.get(ModelConstants.TITLE_CODE));
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, this.params.get(ModelConstants.COUNTRY_ISOCODE));
        addressHashMap.put(ModelConstants.LINE_1, this.params.get(ModelConstants.LINE_1));
        addressHashMap.put(ModelConstants.LINE_2, this.params.get(ModelConstants.LINE_2));
        addressHashMap.put(ModelConstants.POSTAL_CODE, this.params.get(ModelConstants.POSTAL_CODE));
        addressHashMap.put(ModelConstants.TOWN, this.params.get(ModelConstants.TOWN));
        addressHashMap.put(ModelConstants.PHONE_NUMBER, this.params.get(ModelConstants.PHONE_NUMBER));
        addressHashMap.put(ModelConstants.REGION_ISOCODE, this.params.get(ModelConstants.REGION_ISOCODE));
        if (params.containsKey(ModelConstants.DEFAULT_ADDRESS)) {
            addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, this.params.get(ModelConstants.DEFAULT_ADDRESS));
        }
        //payload.put(ModelConstants.ADDRESS_ID,this.params.get(ModelConstants.ADDRESS_ID));
        return addressHashMap;
    }

    @Override
    public String getUrl() {
        if (params == null || !params.containsKey(ModelConstants.ADDRESS_ID) ||
                !params.containsKey(ModelConstants.ADDRESS_ID)) {
            throw new RuntimeException("Address Id must be specified");
        }
        String addressId = params.get(ModelConstants.ADDRESS_ID);
        return store.getAddressAlterUrl(addressId);
    }
}
