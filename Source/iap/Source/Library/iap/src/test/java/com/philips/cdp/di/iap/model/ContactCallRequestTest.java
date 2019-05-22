/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.orders.ContactsResponse;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class ContactCallRequestTest {
    private Context mContext;

    @Mock
    private IAPUser mUser;

    private AbstractModel mModel;
    private StoreListener mStore;

    @Before
    public void setUp() {
        mContext = getInstrumentation().getContext();
        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "GB",*/ null);
        mModel = new ContactCallRequest(mStore, null, null);
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
        String validContactResponse = TestUtils.readFile(ContactCallRequestTest.class,
                "ContactResponse.txt");
        Object response = mModel.parseResponse(validContactResponse);
        assertEquals(response.getClass(), ContactsResponse.class);
    }

    @Test
    public void isValidUrl() {
        HashMap<String, String> bodyParams = new HashMap<>();
        bodyParams.put(ModelConstants.CATEGORY, NetworkURLConstants.SAMPLE_PRODUCT_CATEGORY);
        ContactCallRequest model = new ContactCallRequest(mStore, bodyParams, null);
        assertEquals(NetworkURLConstants.PHONE_CONTACT_URL, model.getUrl());
    }

    @Test(expected = RuntimeException.class)
    public void isExceptionThrownWithNullBodyParams() {
        assertEquals(NetworkURLConstants.PHONE_CONTACT_URL, mModel.getUrl());
    }

}