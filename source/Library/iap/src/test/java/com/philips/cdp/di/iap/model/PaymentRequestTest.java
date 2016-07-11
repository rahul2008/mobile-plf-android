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
        StoreSpec mStore = (new MockStore(mContext, mUser)).getStore();
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
        AddressFields billingAddress = Mockito.mock(AddressFields.class);
        CartModelContainer.getInstance().setAddressId("10003423");
        CartModelContainer.getInstance().setSwitchToBillingAddress(true);
        CartModelContainer.getInstance().setRegionIsoCode("US");
        PaymentRequest request = new PaymentRequest(mStore, null, null);
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
        params.put(ModelConstants.FIRST_NAME, "John");
        params.put(ModelConstants.LAST_NAME, "Doe");
        params.put(ModelConstants.TITLE_CODE, "Mr.");//.toLowerCase(Locale.getDefault()));
        params.put(ModelConstants.COUNTRY_ISOCODE, "US");
        params.put(ModelConstants.REGION_ISOCODE, "US");
        params.put(ModelConstants.LINE_1, "Street Main 1");
        params.put(ModelConstants.LINE_2, "New York");
        params.put(ModelConstants.POSTAL_CODE,"342342");
        params.put(ModelConstants.TOWN, "London");
        params.put(ModelConstants.PHONE_1, "435343453");
        params.put(ModelConstants.PHONE_2, "");
        assertEquals(request.requestBody(), params);
    }


    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.PAYMENT_SET_URL, mModel.getUrl());
    }
}