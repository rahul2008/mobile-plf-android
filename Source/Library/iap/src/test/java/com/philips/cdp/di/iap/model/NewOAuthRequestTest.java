package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.response.oauth.OAuthResponse;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
public class NewOAuthRequestTest {

    @Mock
    Context mContext;
    @Mock
    IAPUser mUser;
    private StoreSpec mStore;
    private AbstractModel mModel;

    @Before
    public void setUp() {
        mStore = (new MockStore(mContext, mUser)).getStore();
        mStore.initStoreConfig("en","US", null);
        mModel = new NewOAuthRequest(mStore, null);
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
    public void testTestingUrilIsNotNull() {
        assertEquals(mModel.getUrl(), NetworkURLConstants.OAUTH_URL);
    }

    @Test
    public void parseResponseShouldBeOfGetShippingAddressDataType() {
//        CartAddProductRequest request = new CartAddProductRequest(mStore, null, null);
        String response = TestUtils.readFile(this.getClass(), "oauth_response.txt");
        Object result = mModel.parseResponse(response);
        assertEquals(result.getClass(), OAuthResponse.class);
    }

    @Test
    public void matchAccessTokenAfterParseRespnse() {
        String response = TestUtils.readFile(this.getClass(), "oauth_response.txt");
        mModel.parseResponse(response);
        assertEquals(((NewOAuthRequest)mModel).getAccessToken(), "afa814bf-ad4d-477c-9bed-a79f0e37b8dd");
    }

    @Test
    public void matchRefreshTokenAfterParseRespnse() {
        String response = TestUtils.readFile(this.getClass(), "oauth_response.txt");
        mModel.parseResponse(response);
        assertEquals(((NewOAuthRequest) mModel).getrefreshToken(), "81eafe29-6036-4729-9118-63e6d089bdba");
    }

    @Test
    public void accessTokenShouldBeNullAfterResetToken() {
        String response = TestUtils.readFile(this.getClass(), "oauth_response.txt");
        mModel.parseResponse(response);
        ((NewOAuthRequest) mModel).resetAccessToken();
        assertNull(((NewOAuthRequest) mModel).getAccessToken());
    }

    @Test
    public void refreshTokenShouldBeEmptyAfterResetToken() {
        String response = TestUtils.readFile(this.getClass(), "oauth_response.txt");
        mModel.parseResponse(response);
        ((NewOAuthRequest) mModel).resetAccessToken();
        assertEquals("", ((NewOAuthRequest) mModel).getrefreshToken());
    }

    @Test
    public void requestRefreshAPIIsAvailable() {
        ((NewOAuthRequest) mModel).refreshToken(null);
    }
}