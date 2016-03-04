package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PaymentRequest extends AbstractModel {
    public PaymentRequest(final Store store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
    }

    @Override
    public String getProductionUrl() {
        return null;
    }

    @Override
    public Object parseResponse(final Object response) {
        return new Gson().fromJson(response.toString(), MakePaymentData.class);
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Map<String, String> requestBody() {
        Addresses deliveryAddress = CartModelContainer.getInstance().getDeliveryAddress();

        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.ADDRESS_ID, deliveryAddress.getId());
        params.put(ModelConstants.FIRST_NAME, deliveryAddress.getFirstName());
        params.put(ModelConstants.LAST_NAME, deliveryAddress.getLastName());
        params.put(ModelConstants.TITLE_CODE, deliveryAddress.getTitleCode());
        params.put(ModelConstants.COUNTRY_ISOCODE, deliveryAddress.getCountry().getIsocode());
        params.put(ModelConstants.LINE_1, deliveryAddress.getLine1());
        params.put(ModelConstants.LINE_2, deliveryAddress.getLine2());
        params.put(ModelConstants.POSTAL_CODE, deliveryAddress.getPostalCode());
        params.put(ModelConstants.TOWN, deliveryAddress.getTown());
        params.put(ModelConstants.PHONE_NUMBER, deliveryAddress.getPhone());
        return params;
    }

    @Override
    public String getTestUrl() {
        //String format with cart No.
        return String.format(NetworkConstants.PAYMENT_URL, params.get(ModelConstants.ORDER_NUMBER));
    }
}
