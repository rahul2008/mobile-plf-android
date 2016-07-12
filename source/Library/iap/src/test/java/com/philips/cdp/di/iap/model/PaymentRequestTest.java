/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PaymentRequestTest {
    @Mock
    StoreSpec mStore;
    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;

    private AbstractModel mModel;

    @Before
    public void setUP() {
        mStore = (new MockStore(mContext, mUser)).getStore();
        mStore.initStoreConfig("en", "US", null);
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.ORDER_NUMBER, "H1212");
        mModel = new PaymentRequest(mStore, params, null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        assertEquals(Request.Method.POST, mModel.getMethod());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void isValidResponse() {
        String validResponse = TestUtils.readFile(GetAddressRequestTest.class, "MakePayment.txt");
        Object response = mModel.parseResponse(validResponse);
        assertEquals(response.getClass(), MakePaymentData.class);
    }

    //    AddressFields billingAddress = CartModelContainer.getInstance().getBillingAddress();
//
//    Map<String, String> params = new HashMap<>();
//    if (!CartModelContainer.getInstance().isSwitchToBillingAddress()) {
//        params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
//        setBillingAddressParams(billingAddress, params);
//    } else
//    setBillingAddressParams(billingAddress, params);
//
//    return params;
//
    @Test
    public void testQueryParamsHasBody() {

        CartModelContainer.getInstance().setAddressId("10003423");
        CartModelContainer.getInstance().setSwitchToBillingAddress(true);
        CartModelContainer.getInstance().setRegionIsoCode("US");

        PaymentRequest request = Mockito.mock(PaymentRequest.class);
        Map<String, String> params = new HashMap<String, String>();
//        AddressFields billingAddress = Mockito.mock(AddressFields.class);
//        Mockito.when(billingAddress.getFirstName()).thenReturn("John");
//        Mockito.when(billingAddress.getLastName()).thenReturn("Doe");
//        Mockito.when(billingAddress.getTitleCode()).thenReturn("us");
//        Mockito.when(billingAddress.getCountryIsocode()).thenReturn("US");
//        Mockito.when(billingAddress.getRegionIsoCode()).thenReturn("US");
//        Mockito.when(billingAddress.getLine1()).thenReturn("St 1 Main Road");
//        Mockito.when(billingAddress.getLine2()).thenReturn("New York");
//        Mockito.when(billingAddress.getPostalCode()).thenReturn("10036");
//        Mockito.when(billingAddress.getTown()).thenReturn("New York");
//        Mockito.when(billingAddress.getPhoneNumber()).thenReturn("342423423");
//
//
//
//        params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
//        params.put(ModelConstants.FIRST_NAME, billingAddress.getFirstName());
//        params.put(ModelConstants.LAST_NAME, billingAddress.getLastName());
//        params.put(ModelConstants.TITLE_CODE, billingAddress.getTitleCode());//.toLowerCase(Locale.getDefault()));
//        params.put(ModelConstants.COUNTRY_ISOCODE, billingAddress.getCountryIsocode());
//        params.put(ModelConstants.REGION_ISOCODE, billingAddress.getRegionIsoCode());
//        params.put(ModelConstants.LINE_1, billingAddress.getLine1());
//        params.put(ModelConstants.LINE_2, billingAddress.getLine2());
//        params.put(ModelConstants.POSTAL_CODE, billingAddress.getPostalCode());
//        params.put(ModelConstants.TOWN, billingAddress.getTown());
//        params.put(ModelConstants.PHONE_1, billingAddress.getPhoneNumber());
//        params.put(ModelConstants.PHONE_2, "");
        assertEquals(request.requestBody(), params);
    }


    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.PAYMENT_SET_URL, mModel.getUrl());
    }
}