/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
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
    OAuthController mOAuthController;
    @Mock
    RefreshOAuthRequest mRefreshOAuthRequest;
    @Mock
    AbstractModel model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOAuthController = new OAuthController();
    }

    @Test
    public void getAccessToken() throws Exception {
        mOAuthController.getAccessToken();
    }

    @Test(expected = NullPointerException.class)
    public void refreshToken() throws Exception {
        mOAuthController.refreshToken(null);
    }

    @Test
    public void resetAccessToken() throws Exception {
        mOAuthController.resetAccessToken();
    }

    @Test(expected = RuntimeException.class)
    public void requestSyncRefreshToken() throws Exception {
        mOAuthController.requestSyncRefreshToken(mRefreshOAuthRequest, new RequestListener() {
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
        mOAuthController.createOAuthRequest(model);
    }

    @Test(expected = NullPointerException.class)
    public void requestSyncOAuthToken() throws Exception {
        mOAuthController.requestSyncOAuthToken(model);
    }

    @Test
    public void notifySuccessListener() throws Exception {
        mOAuthController.notifySuccessListener(null, model);
    }

    @Test
    public void notifyErrorListener() throws Exception {
        mOAuthController.notifyErrorListener(null, model);
    }

    @Test
    public void isInvalidGrantError() throws Exception {
        mOAuthController.isInvalidGrantError(new VolleyError());
    }
}