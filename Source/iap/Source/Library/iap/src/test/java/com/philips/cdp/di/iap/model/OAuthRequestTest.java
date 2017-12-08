package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.response.oauth.OAuthResponse;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

public class OAuthRequestTest {

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
        mStore.initStoreConfig(/*"en", "US",*/ null);
        mModel = new OAuthRequest(mStore, null);
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
        assertNull(mModel.requestBody());
    }

    @Test
    public void isValidUrl() {
        assertEquals(mModel.getUrl(), NetworkURLConstants.OAUTH_URL);
    }

    @Test
    public void isValidResponse() {
        String validResponse = TestUtils.readFile(this.getClass(), "OAuth.txt");
        Object result = mModel.parseResponse(validResponse);
        assertEquals(result.getClass(), OAuthResponse.class);
    }

    @Test
    public void matchAccessTokenAfterParseResponse() {
        String response = TestUtils.readFile(this.getClass(), "OAuth.txt");
        mModel.parseResponse(response);
        assertEquals(((OAuthRequest) mModel).getAccessToken(), "afa814bf-ad4d-477c-9bed-a79f0e37b8dd");
    }

    @Test
    public void matchRefreshTokenAfterParseResponse() {
        String response = TestUtils.readFile(this.getClass(), "OAuth.txt");
        mModel.parseResponse(response);
        assertEquals(((OAuthRequest) mModel).getrefreshToken(), "81eafe29-6036-4729-9118-63e6d089bdba");
    }

    @Test
    public void accessTokenShouldBeNullAfterResetToken() {
        String response = TestUtils.readFile(this.getClass(), "OAuth.txt");
        mModel.parseResponse(response);
        ((OAuthRequest) mModel).resetAccessToken();
        assertNull(((OAuthRequest) mModel).getAccessToken());
    }

    @Test
    public void refreshTokenShouldBeEmptyAfterResetToken() {
        String response = TestUtils.readFile(this.getClass(), "OAuth.txt");
        mModel.parseResponse(response);
        ((OAuthRequest) mModel).resetAccessToken();
        assertEquals("", ((OAuthRequest) mModel).getrefreshToken());
    }

    @Test
    public void requestRefreshAPIIsAvailable() {
        ((OAuthRequest) mModel).refreshToken(null);
    }
}