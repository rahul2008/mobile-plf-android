/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.app.Application;
import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class VerticalImplementation extends MockIAPListener {
    private MockIAPDependencies mockIAPDependencies;
    private MockIAPSetting mockIAPSetting;
    private MockIAPInterface mockIAPInterface;
    private MockIAPLaunchInput mockIAPLaunchInput;
    @Mock
    Context mContext;
    @Mock
    AppInfra mAppInfra;
    @Mock
    User mUser;

    @Before
    public void setUp() throws Exception {
        mockIAPDependencies = new MockIAPDependencies(mAppInfra);
        mockIAPSetting = new MockIAPSetting(new Application());
        mockIAPInterface = new MockIAPInterface();
        mockIAPLaunchInput = new MockIAPLaunchInput();
        mockIAPSetting.setUseLocalData(true);
        mockIAPInterface.init(mockIAPDependencies, mockIAPSetting);
    }

    @Test
    public void onCreate() throws Exception {
        mockIAPSetting.setUseLocalData(true);
        mockIAPInterface.init(mockIAPDependencies, mockIAPSetting);
    }

    @Test
    public void onResume() throws Exception {
        mockIAPLaunchInput.setIapListener(new MockIAPListener());
    }

    @Test
    public void setIsLocaleFalse() throws Exception {
        mockIAPSetting.setUseLocalData(false);
        Assert.assertFalse(mockIAPSetting.isUseLocalData());
    }

    @Test
    public void setIsLocaleTrue() throws Exception {
        mockIAPSetting.setUseLocalData(true);
        Assert.assertTrue(mockIAPSetting.isUseLocalData());
    }

    @Test
    public void getProductCartCount() throws Exception {
        mockIAPInterface.getProductCartCount(this);
    }

    @Test
    public void getCompleteProductList() throws Exception {
        mockIAPInterface.getCompleteProductList(this);
    }
}
