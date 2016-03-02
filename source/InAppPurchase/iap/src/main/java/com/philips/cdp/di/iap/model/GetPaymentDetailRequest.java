package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.Map;

public class GetPaymentDetailRequest extends AbstractModel {

    public GetPaymentDetailRequest(Store store, Map<String, String> query, DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(Object response) {
        return new Gson().fromJson(response.toString(), PaymentMethods.class);
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
    public String getTestUrl() {
        return NetworkConstants.GET_PAYMENT_DETAILS_URL;
    }
}
