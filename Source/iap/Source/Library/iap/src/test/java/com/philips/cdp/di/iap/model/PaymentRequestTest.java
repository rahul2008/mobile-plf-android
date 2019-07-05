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
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.payment.MakePaymentData;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class PaymentRequestTest {
    private Context mContext;

    @Mock
    private StoreListener mStore;
    @Mock
    private IAPUser mUser;

    private AbstractModel mModel;

    @Before
    public void setUP() {
        mContext = getInstrumentation().getContext();
        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "US",*/ null);
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

    @Test
    public void testQueryParamsHasBodyWithRegionIsoCode() {
        AddressFields mockBillingAddress = Mockito.mock(AddressFields.class);
        CartModelContainer.getInstance().setBillingAddress(mockBillingAddress);
        Mockito.when(mockBillingAddress.getFirstName()).thenReturn("hello");
        Mockito.when(mockBillingAddress.getLastName()).thenReturn("world");
        Mockito.when(mockBillingAddress.getTitleCode()).thenReturn("title");
        Mockito.when(mockBillingAddress.getCountryIsocode()).thenReturn("US");
        Mockito.when(mockBillingAddress.getRegionIsoCode()).thenReturn("US_NY");
        Mockito.when(mockBillingAddress.getLine1()).thenReturn("dfs");
        Mockito.when(mockBillingAddress.getPostalCode()).thenReturn("");
        Mockito.when(mockBillingAddress.getTown()).thenReturn("Delhi?");
        Mockito.when(mockBillingAddress.getPhone1()).thenReturn("5417543010");
        Mockito.when(mockBillingAddress.getPhone2()).thenReturn("5417543010");
        Mockito.when(mockBillingAddress.getEmail()).thenReturn("testinapp@mailinator.com");
        Mockito.when(mockBillingAddress.getRegionName()).thenReturn("US");
        CartModelContainer.getInstance().setBillingAddress(mockBillingAddress);
        CartModelContainer.getInstance().setRegionIsoCode("US_NY");
        CartModelContainer.getInstance().setSwitchToBillingAddress(true);
        CartModelContainer.getInstance().setAddressId("8799470125079");
        HashMap<String, String> addressHashMap = getAddressHashMap("8799470125079");

        PaymentRequest request = new PaymentRequest(mStore, addressHashMap, null);

        assertEquals(request.requestBody(), addressHashMap);
    }


    @Test
    public void testPaymentRequestWithAddressId() {
        CartModelContainer.getInstance().setAddressId("8799470125079");
        HashMap<String, String> addressHashMap = getAddressHashMap("8799470125079");

        PaymentRequest request = new PaymentRequest(mStore, addressHashMap, null);

        assertEquals(request.requestBody(), addressHashMap);
    }


    @Test
    public void testQueryParamsHasBodyWithoutRegionIsoCode() {
        AddressFields mockBillingAddress = Mockito.mock(AddressFields.class);
        CartModelContainer.getInstance().setBillingAddress(mockBillingAddress);
        Mockito.when(mockBillingAddress.getFirstName()).thenReturn("hello");
        Mockito.when(mockBillingAddress.getLastName()).thenReturn("world");
        Mockito.when(mockBillingAddress.getTitleCode()).thenReturn("title");
        Mockito.when(mockBillingAddress.getCountryIsocode()).thenReturn("US");
        Mockito.when(mockBillingAddress.getRegionIsoCode()).thenReturn(null);
        Mockito.when(mockBillingAddress.getLine1()).thenReturn("dfs");
        Mockito.when(mockBillingAddress.getPostalCode()).thenReturn("");
        Mockito.when(mockBillingAddress.getTown()).thenReturn("Delhi?");
        Mockito.when(mockBillingAddress.getPhone1()).thenReturn("5417543010");
        Mockito.when(mockBillingAddress.getPhone2()).thenReturn("5417543010");

        CartModelContainer.getInstance().setBillingAddress(mockBillingAddress);
        CartModelContainer.getInstance().setSwitchToBillingAddress(false);
        CartModelContainer.getInstance().setRegionIsoCode(null);
        HashMap<String, String> addressHashMap = getAddressHashMap(null);

        PaymentRequest request = new PaymentRequest(mStore, addressHashMap, null);
        assertEquals(request.requestBody(), addressHashMap);
    }

    private HashMap<String, String> getAddressHashMap(String addressId) {
        HashMap<String, String> addressHashMap = new HashMap<>();
        if (addressId != null) {
            addressHashMap.put(ModelConstants.ADDRESS_ID, addressId);
        }
        else {
            addressHashMap.put(ModelConstants.FIRST_NAME, "hello");
            addressHashMap.put(ModelConstants.LAST_NAME, "world");
            addressHashMap.put(ModelConstants.TITLE_CODE, "title");
            addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, "US");
            if (CartModelContainer.getInstance().getRegionIsoCode() != null) {
                addressHashMap.put(ModelConstants.REGION_ISOCODE, CartModelContainer.getInstance().getRegionIsoCode());
            } else {
                addressHashMap.put(ModelConstants.REGION_ISOCODE, "");
            }
            addressHashMap.put(ModelConstants.LINE_1, "dfs");
            addressHashMap.put(ModelConstants.POSTAL_CODE, "");
            addressHashMap.put(ModelConstants.TOWN, "Delhi?");
            addressHashMap.put(ModelConstants.PHONE_1, "5417543010");
            addressHashMap.put(ModelConstants.PHONE_2, "5417543010");
        }
        return addressHashMap;
    }
}