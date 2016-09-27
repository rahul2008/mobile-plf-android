package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class IAPUserTest {
    @Mock
    HybrisStore mHybrisStore;
    @Mock
    Context mContext;

    IAPUser mIAPUser;

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
    public void testGetJanrainEmailShouldNotNull() {
        assertNotNull(mIAPUser.getJanRainEmail());
    }

}