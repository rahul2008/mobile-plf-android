package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.philips.cdp.di.iap.store.StoreListener;

import java.util.Map;

public class DeleteVoucherRequest extends AbstractModel {

    private String mVoucherCode;
    public DeleteVoucherRequest(StoreListener store, Map<String, String> query,
                                DataLoadListener listener, String voucherCode) {
        super(store, query, listener);
        mVoucherCode=voucherCode;
    }



    @Override
    public Object parseResponse(Object response) {
        return response;
    }

    @Override
    public int getMethod() {
       return  Request.Method.DELETE;
    }

    @Override
    public Map<String, String> requestBody() {
        return null;
    }

    @Override
    public String getUrl() {
        return store.getDeleteVoucherUrl(mVoucherCode);
    }
}
