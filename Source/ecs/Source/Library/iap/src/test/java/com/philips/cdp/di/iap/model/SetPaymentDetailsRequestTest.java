/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
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
import java.util.Map;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SetPaymentDetailsRequestTest {
    @Mock
    private StoreListener mStore;

    @Before
    public void setUP() {
        Context context = getInstrumentation().getContext();
        mStore = new MockStore(context, mock(IAPUser.class)).getStore(new MockIAPSetting(context),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "us",*/ null);
    }

    @Test
    public void testRequestMethodIsPOST() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        assertEquals(Request.Method.PUT, request.getMethod());
    }


    @Test
    public void testQueryParamsHasBody() {
        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkURLConstants.SET_PAYMENT_DETAIL_URL);
        Map<String, String> params = new HashMap<String, String>();
        assertEquals(mockSetPaymentDetailsRequest.requestBody(), params);
    }

    @Test
    public void testQueryParamsWithPaymentDetailId() {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, ModelConstants.PAYMENT_DETAILS_ID);
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, params, null);
        assertEquals(request.requestBody(), params);
    }

    @Test
    public void testTestingUriIsNotNull() {
        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkURLConstants.SET_PAYMENT_DETAIL_URL);
        assertNotNull(mockSetPaymentDetailsRequest.getUrl());
    }

    @Test
    public void testTestingUrilIsForSetPaymentDetailsRequest() {
        SetPaymentDetailsRequest mockSetPaymentDetailsRequest = Mockito.mock(SetPaymentDetailsRequest.class);
        Mockito.when(mockSetPaymentDetailsRequest.getUrl()).thenReturn(NetworkURLConstants.SET_PAYMENT_DETAIL_URL);
        assertEquals(mockSetPaymentDetailsRequest.getUrl(), NetworkURLConstants.SET_PAYMENT_DETAIL_URL);
    }

    @Test
    public void parseResponseShouldBeOfSetPaymentDetailsRequestDataType() {
        SetPaymentDetailsRequest request = new SetPaymentDetailsRequest(mStore, null, null);
        String str = null;
        Object response = request.parseResponse(str);
        assertEquals(IAPConstant.IAP_SUCCESS, response);
    }

}