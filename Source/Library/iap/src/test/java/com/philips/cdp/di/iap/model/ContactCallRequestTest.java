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
import com.philips.cdp.di.iap.response.orders.ContactsResponse;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ContactCallRequestTest {

    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private AbstractModel mModel;
    private StoreListener mStore;

    @Before
    public void setUP() {
        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
        mStore.initStoreConfig("en", "GB", null);
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