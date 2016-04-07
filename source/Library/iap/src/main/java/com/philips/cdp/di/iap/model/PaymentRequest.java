package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.store.Store;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PaymentRequest extends AbstractModel {
    String mBillingAddressId;

    public PaymentRequest(final Store store, final Map<String, String> query, final DataLoadListener listener) {
        super(store, query, listener);
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
        AddressFields billingAddress = CartModelContainer.getInstance().getBillingAddress();

        Map<String, String> params = new HashMap<String, String>();

        params.put(ModelConstants.ADDRESS_ID, getBillingAddressId());
        params.put(ModelConstants.FIRST_NAME, billingAddress.getFirstName());
        params.put(ModelConstants.LAST_NAME, billingAddress.getLastName());
        params.put(ModelConstants.TITLE_CODE, billingAddress.getTitleCode().toLowerCase(Locale.getDefault()));
        params.put(ModelConstants.COUNTRY_ISOCODE, billingAddress.getCountryIsocode());
        params.put(ModelConstants.LINE_1, billingAddress.getLine1());
        params.put(ModelConstants.LINE_2, billingAddress.getLine2());
        params.put(ModelConstants.POSTAL_CODE, billingAddress.getPostalCode());
        params.put(ModelConstants.TOWN, billingAddress.getTown());
        params.put(ModelConstants.PHONE_NUMBER, billingAddress.getPhoneNumber());
        return params;
    }

    @Override
    public String getUrl() {
        return store.getSetPaymentUrl(params.get(ModelConstants.ORDER_NUMBER));
    }

    public String getBillingAddressId() {
        if (mBillingAddressId != null)
            return mBillingAddressId;
        else
            return "";
    }
}
