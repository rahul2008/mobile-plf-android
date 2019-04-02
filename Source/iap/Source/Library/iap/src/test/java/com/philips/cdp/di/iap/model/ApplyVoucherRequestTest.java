/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
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
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class ApplyVoucherRequestTest {
    private Context mContext;
    private StoreListener mStore;
    private AbstractModel mModel;

    @Mock
    private IAPUser mUser;

    @Before
    public void setUp() {
        mContext = getInstrumentation().getContext();
        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
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


}