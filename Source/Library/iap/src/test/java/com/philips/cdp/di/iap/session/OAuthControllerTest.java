package com.philips.cdp.di.iap.session;

import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.RefreshOAuthRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OAuthControllerTest {
    OAuthController mTestEnvOAuthHandler;
    @Mock
    RefreshOAuthRequest mRefreshOAuthRequest;

    @Mock
    AbstractModel model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTestEnvOAuthHandler = new OAuthController();
    }

    @Test(expected = NullPointerException.class)
    public void getAccessToken() throws Exception {
        mTestEnvOAuthHandler.getAccessToken();
    }

    @Test(expected = NullPointerException.class)
    public void refreshToken() throws Exception {
        mTestEnvOAuthHandler.refreshToken(null);
    }

    @Test
    public void resetAccessToken() throws Exception {
        mTestEnvOAuthHandler.resetAccessToken();
    }

    @Test(expected = RuntimeException.class)
    public void requestSyncRefreshToken() throws Exception {
        mTestEnvOAuthHandler.requestSyncRefreshToken(mRefreshOAuthRequest, new RequestListener() {
            @Override
            public void onSuccess(Message msg) {

            }

            @Override
            public void onError(Message msg) {

            }
        });
    }

    @Test
    public void createOAuthRequest() throws Exception {
        mTestEnvOAuthHandler.createOAuthRequest(model);
    }

    @Test(expected = NullPointerException.class)
    public void requestSyncOAuthToken() throws Exception {
        mTestEnvOAuthHandler.requestSyncOAuthToken(model);
    }


    @Test
    public void notifySuccessListener() throws Exception {
        mTestEnvOAuthHandler.notifySuccessListener(null, model);
    }
    @Test
    public void notifyErrorListener() throws Exception {
        mTestEnvOAuthHandler.notifyErrorListener(null, model);
    }

    @Test
    public void isInvalidGrantError() throws Exception {
        mTestEnvOAuthHandler.isInvalidGrantError(new VolleyError());
    }
}