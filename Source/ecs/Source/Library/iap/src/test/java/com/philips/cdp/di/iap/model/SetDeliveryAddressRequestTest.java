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
public class SetDeliveryAddressRequestTest {

    private Context mContext;

    @Mock
    private IAPUser mUser;

    private AbstractModel mModel;

    @Before
    public void setUP() {
        mContext = getInstrumentation().getContext();
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "US",*/ null);
        mModel = new SetDeliveryAddressRequest(mStore, null, null);
    }

    @Test
    public void testRequestMethodIsPUT() {
        assertEquals(Request.Method.PUT, mModel.getMethod());
    }

    @Test
    public void testQueryParamsIsNotNull() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        assertNotNull(mockSetDeliveryAddressRequest.requestBody());
    }

    @Test
    public void testQueryParamsHasBody() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        Map<String, String> params = new HashMap<String, String>();
        assertEquals(mockSetDeliveryAddressRequest.requestBody(), params);
    }

    @Test
    public void testTestingUriIsNotNull() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        assertNotNull(mockSetDeliveryAddressRequest.getUrl());
    }

    @Test
    public void testTestingUrilIsForSetDeliveryAddressRequest() {
        SetDeliveryAddressRequest mockSetDeliveryAddressRequest = Mockito.mock(SetDeliveryAddressRequest.class);
        Mockito.when(mockSetDeliveryAddressRequest.getUrl()).thenReturn(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
        assertEquals(mockSetDeliveryAddressRequest.getUrl(), NetworkURLConstants.SET_DELIVERY_ADDRESS_URL);
    }

    @Test
    public void parseResponseShouldBeOfSetDeliveryAddressRequestDataType() {
        assertEquals(IAPConstant.IAP_SUCCESS, mModel.parseResponse(null));
    }
}