package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class RefreshOAuthRequestTest {

    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    @Mock
    IAPDependencies mIAPDependencies;
    private AbstractModel mModel;

    @Before
    public void setUp() {
        StoreSpec mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPDependencies());
        mStore.initStoreConfig("en", "US", null);
        mModel = new RefreshOAuthRequest(mStore, new HashMap<String, String>());
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
    public void testQueryParamsIsNull() {
        assertNotNull(mModel.requestBody());
    }

    @Test
    public void testParseResponseIsNull() {
        assertNull(mModel.parseResponse(mock(HybrisStore.class)));
    }

    @Test
    public void isValidUrl() {
        assertEquals(mModel.getUrl(), NetworkURLConstants.OAUTH_REFRESH_URL);
    }

}