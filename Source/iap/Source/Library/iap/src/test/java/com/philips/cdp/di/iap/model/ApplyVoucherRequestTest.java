/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class ApplyVoucherRequestTest {
    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private AbstractModel mModel;
    StoreListener mStore;

    @Before
    public void setUP() {
        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
        mStore.initStoreConfig(/*"en", "US",*/ null);
        HashMap<String, String> query = new HashMap<>();
        mModel = new GetApplyVoucherRequest(mStore, query, null);
    }

    @Test
    public void testApplyVoucher(){
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.VOUCHER_ID, "a1C-KEHN-DRKY-CET");
        mModel = new GetApplyVoucherRequest(mStore, query, null);
    }

    @Test
    public void testRequestMethodIsGET() {
        assertEquals(Request.Method.POST, mModel.getMethod());
    }

    @Test
    public void testBodyParamsIsNull() {
        assertNotNull(mModel.requestBody());
    }


    @Test
    public void isValidUrl() {
        assertEquals(NetworkURLConstants.APPLY_URL, mModel.getUrl());
    }
}