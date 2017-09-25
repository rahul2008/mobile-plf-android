/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class IAPUserTest {
    @Mock
    HybrisStore mHybrisStore;
    @Mock
    Context mContext;

    private IAPUser mIAPUser;

    private IAPUser mUser;
    @Mock
    User mJainrain;

    @Mock
    RefreshLoginSessionHandler refreshLoginSessionHandler;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mJainrain.getJanrainUUID()).thenReturn("");
        Mockito.when(mJainrain.getAccessToken()).thenReturn("");
        Mockito.when(mJainrain.getEmail()).thenReturn("");
        mIAPUser = new MockIAPUser(mHybrisStore);
       // mUser = new IAPUser(mContext, mHybrisStore);
    }

    @Test
    public void testGetJanrainIDShouldNotNull() {
        assertNotNull(mIAPUser.getJanRainID());
    }

    @Test
    public void testGetgetJanRainID() {
        assertNotNull(mIAPUser.getJanRainID());
    }

    @Test
    public void testGetJanrainEmailShouldNotNull() {
        assertNotNull(mIAPUser.getJanRainEmail());
    }


    @Test(expected = NullPointerException.class)
    public void testrefreshLoginSession() {
        // refreshLoginSessionHandler.onRefreshLoginSessionSuccess();

        refreshLoginSessionHandler.onRefreshLoginSessionSuccess();
        mIAPUser.refreshLoginSession();
    }

    @Test
    public void testIsTokenResfressSuccess() {
        mIAPUser.isTokenRefreshSuccessful();
    }


    @Test
    public void testUnLockThread() {
        mIAPUser.unlockOAuthThread();
    }

    @Test
    public void testLogout() {
        mIAPUser.onUserLogoutSuccess();
    }
}