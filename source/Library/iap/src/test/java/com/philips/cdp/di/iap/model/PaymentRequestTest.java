package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.Store;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
public class PaymentRequestTest {
    @Mock
    private Store mStore;

    @Before
    public void setUP() {
        mStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
        mStore.initStoreConfig("en", "us", null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        assertEquals(Request.Method.POST, request.getMethod());
    }

    @Test
    public void parseResponseShouldBeOfMakePaymentData() {
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        String oneAddress = TestUtils.readFile(GetAddressRequestTest.class, "payment_url.txt");
        Object response = request.parseResponse(oneAddress);
        assertEquals(response.getClass(), MakePaymentData.class);
    }

    protected AddressFields setAddressFields() {
        AddressFields addressFields = new AddressFields();
        addressFields.setFirstName("john");
        addressFields.setLastName("dow");
        addressFields.setTitleCode("mr");
        addressFields.setCountryIsocode("us");
        addressFields.setLine1("phonix st");
        addressFields.setLine2("");
        addressFields.setPostalCode("33424534");
        addressFields.setTown("new york");
        addressFields.setPhoneNumber("33434532");

        return addressFields;
    }

    @Test
    public void testQueryParamsWithAddressId() {
        CartModelContainer.getInstance().setBillingAddress(setAddressFields());// setAddressFields(new AddressFields());//new AddressFields();
        Map<String, String> params = new HashMap<>();
        CartModelContainer.getInstance().setSwitchToBillingAddress(false);
        CartModelContainer.getInstance().setAddressId("34242242");
        AddressFields billingAddress = CartModelContainer.getInstance().getBillingAddress();
        params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
        params.put(ModelConstants.FIRST_NAME, billingAddress.getFirstName());
        params.put(ModelConstants.LAST_NAME, billingAddress.getLastName());
        params.put(ModelConstants.TITLE_CODE, billingAddress.getTitleCode());
        params.put(ModelConstants.COUNTRY_ISOCODE, billingAddress.getCountryIsocode());
        params.put(ModelConstants.LINE_1, billingAddress.getLine1());
        params.put(ModelConstants.LINE_2, billingAddress.getLine2());
        params.put(ModelConstants.POSTAL_CODE, billingAddress.getPostalCode());
        params.put(ModelConstants.TOWN, billingAddress.getTown());
        params.put(ModelConstants.PHONE_1, billingAddress.getPhoneNumber());
        params.put(ModelConstants.PHONE_2, "");

        PaymentRequest request = new PaymentRequest(mStore, params, null);
        assertEquals(request.requestBody(), params);
    }

    @Test
    public void testQueryParamsWithoutAddressId() {
        CartModelContainer.getInstance().setBillingAddress(setAddressFields());
        Map<String, String> params = new HashMap<>();
        CartModelContainer.getInstance().setSwitchToBillingAddress(true);
        AddressFields billingAddress = CartModelContainer.getInstance().getBillingAddress();
        params.put(ModelConstants.FIRST_NAME, billingAddress.getFirstName());
        params.put(ModelConstants.LAST_NAME, billingAddress.getLastName());
        params.put(ModelConstants.TITLE_CODE, billingAddress.getTitleCode());
        params.put(ModelConstants.COUNTRY_ISOCODE, billingAddress.getCountryIsocode());
        params.put(ModelConstants.LINE_1, billingAddress.getLine1());
        params.put(ModelConstants.LINE_2, billingAddress.getLine2());
        params.put(ModelConstants.POSTAL_CODE, billingAddress.getPostalCode());
        params.put(ModelConstants.TOWN, billingAddress.getTown());
        params.put(ModelConstants.PHONE_1, billingAddress.getPhoneNumber());
        params.put(ModelConstants.PHONE_2, "");

        PaymentRequest request = new PaymentRequest(mStore, params, null);
        assertEquals(request.requestBody(), params);
    }

//    @Test
//    public void testParamsToSetBillingAddress(Map<String, String> params) {
//        params.put(ModelConstants.FIRST_NAME, "John");
//        params.put(ModelConstants.LAST_NAME, "Deo");
//        params.put(ModelConstants.TITLE_CODE, "mr");
//        params.put(ModelConstants.COUNTRY_ISOCODE, "us");
//        params.put(ModelConstants.LINE_1, "New York");
//        params.put(ModelConstants.LINE_2, "");
//        params.put(ModelConstants.POSTAL_CODE, "10036");
//        params.put(ModelConstants.TOWN, "New York");
//        params.put(ModelConstants.PHONE_1, "4345345");
//        params.put(ModelConstants.PHONE_2, "");
//        assertNotNull(params);
//    }

    @Test
    public void testQueryParamsIsNotNull() {
        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
        assertNotNull(mockPaymentRequest.requestBody());
    }

    @Test
    public void matchAddressDetailURL() {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.ORDER_NUMBER, "H1212");
        PaymentRequest request = new PaymentRequest(mStore, params, null);
        assertEquals(NetworkURLConstants.PAYMENT_SET_URL, request.getUrl());
    }
}