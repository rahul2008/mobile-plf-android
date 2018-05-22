package com.philips.cdp.di.iap.model;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.placeorder.PlaceOrder;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PlaceOrderRequest extends AbstractModel {
    public PlaceOrderRequest(final StoreListener store, final Map<String, String> query, final DataLoadListener listener) {
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
        AddressFields billingAddress = CartModelContainer.getInstance().getBillingAddress();

        Map<String, String> params = new HashMap<>();
        String securityCode = this.params.get(ModelConstants.SECURITY_CODE);
        if (securityCode != null)
            params.put(ModelConstants.SECURITY_CODE, securityCode);
        params.put(ModelConstants.CART_ID, "current");
        if (CartModelContainer.getInstance().isSwitchToBillingAddress()) {
            params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
        }
        else
        {
            setBillingAddressParams(billingAddress, params);
        }
        return params;
    }

    @Override
    public String getUrl() {
        return store.getPlaceOrderUrl();
    }

    private void setBillingAddressParams(AddressFields billingAddress, Map<String, String> params) {
        params.put(ModelConstants.TITLE_CODE, billingAddress.getTitleCode().toLowerCase(Locale.getDefault()));
        params.put(ModelConstants.FIRST_NAME, billingAddress.getFirstName());
        params.put(ModelConstants.LAST_NAME, billingAddress.getLastName());
        params.put(ModelConstants.LINE_1, billingAddress.getLine1());
        params.put(ModelConstants.LINE_2, billingAddress.getLine2());
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
}
