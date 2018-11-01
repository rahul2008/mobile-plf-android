/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class GetDeliveryModesRequestTest {
    private Context mContext;

    @Mock
    private IAPUser mUser;

    private AbstractModel mModel;

    @Before
    public void setUp() {
        mContext = getInstrumentation().getContext();
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
        mStore.initStoreConfig(/*"en", "US",*/ null);
        mModel = new GetDeliveryModesRequest(mStore, null, null);
    }

    @Test
    public void testRequestMethodIsGET() {
        assertEquals(Request.Method.GET, mModel.getMethod());
    }

    @Test
    public void testBodyParamsIsNull() {
        assertNull(mModel.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }

    @Test
    public void isValidResponse() {
        String validDeliveryModes = TestUtils.readFile(GetDeliveryModesRequestTest.class,
                "DeliveryModes.txt");
        Object response = mModel.parseResponse(validDeliveryModes);
        assertEquals(response.getClass(), GetDeliveryModes.class);
    }

    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.GET_DELIVERY_MODES_URL, mModel.getUrl());
    }
}