package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.oauth.OAuthResponse;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
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
    public void isValidResponse() {
        String validResponse = TestUtils.readFile(RefreshOAuthRequestTest.class, "OAuthResponse.txt");
        Object response = mModel.parseResponse(validResponse);
        assertEquals(response.getClass(), OAuthResponse.class);
    }

    @Test
    public void isValidUrl() {
        assertEquals(mModel.getUrl(), NetworkURLConstants.OAUTH_REFRESH_URL);
    }
}