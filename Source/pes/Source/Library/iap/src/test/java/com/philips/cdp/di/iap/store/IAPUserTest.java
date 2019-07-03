/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class IAPUserTest {
    @Mock
    private HybrisStore mHybrisStore;
    @Mock
    private UserDataInterface userDataInterface;

    @Mock
    private RefreshSessionListener refreshLoginSessionHandler;

    private IAPUser mIAPUser;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mIAPUser = new MockIAPUser(mHybrisStore);
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

        refreshLoginSessionHandler.refreshSessionSuccess();
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
        Whitebox.setInternalState(mIAPUser,"mStore",mHybrisStore);
        Whitebox.setInternalState(mIAPUser,"mUserDataInterface",userDataInterface);
        mIAPUser.logoutSessionSuccess();
    }
}