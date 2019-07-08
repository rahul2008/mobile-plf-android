package com.ecs.demouapp.ui.model;

import com.android.volley.Request;
import com.ecs.demouapp.ui.address.AddressFields;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.response.payment.MakePaymentData;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PaymentRequest extends AbstractModel {

    public PaymentRequest(final StoreListener store, final Map<String, String> query,
                          final DataLoadListener listener) {
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

        Map<String, String> params = new HashMap<>();
        if (CartModelContainer.getInstance().isSwitchToBillingAddress()) {
            params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
          //  setBillingAddressParams(billingAddress, params);
        } else {
            setBillingAddressParams(billingAddress, params);
        }

        return params;
    }

    private void setBillingAddressParams(AddressFields billingAddress, Map<String, String> params) {
        params.put(ModelConstants.TITLE_CODE, billingAddress.getTitleCode().toLowerCase(Locale.getDefault()));
        params.put(ModelConstants.FIRST_NAME, billingAddress.getFirstName());
        params.put(ModelConstants.LAST_NAME, billingAddress.getLastName());
        params.put(ModelConstants.LINE_1, billingAddress.getLine1());
        params.put(ModelConstants.HOUSE_NO, billingAddress.getHouseNumber());
        params.put(ModelConstants.POSTAL_CODE, billingAddress.getPostalCode());
        params.put(ModelConstants.TOWN, billingAddress.getTown());
        params.put(ModelConstants.COUNTRY_ISOCODE, billingAddress.getCountryIsocode());
        if (billingAddress.getRegionIsoCode() != null) {
            params.put(ModelConstants.REGION_ISOCODE, billingAddress.getRegionIsoCode());
        } else {
            params.put(ModelConstants.REGION_ISOCODE, "");
        }
        params.put(ModelConstants.PHONE_1, billingAddress.getPhone1());
        params.put(ModelConstants.PHONE_2, billingAddress.getPhone1());
    }

    @Override
    public String getUrl() {
        return store.getMakePaymentUrl(params.get(ModelConstants.ORDER_NUMBER));
    }
}
