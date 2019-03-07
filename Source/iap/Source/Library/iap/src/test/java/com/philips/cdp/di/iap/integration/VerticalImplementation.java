/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class VerticalImplementation {
    private MockIAPDependencies mockIAPDependencies;
    private MockIAPSetting mockIAPSetting;
    private MockIAPInterface mockIAPInterface;
    private IAPLaunchInput mockIAPLaunchInput;

    @Mock
    private AppInfra mAppInfra;

    @Mock
    private UserDataInterface mUserDataInterface;

    @Mock
    private IAPListener iapListenerMock;

    @Before
    public void setUp() throws Exception {
        mockIAPDependencies = new MockIAPDependencies(mAppInfra,mUserDataInterface);
        mockIAPSetting = new MockIAPSetting(new Application());
        mockIAPInterface = new MockIAPInterface();
        mockIAPLaunchInput = new IAPLaunchInput();
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
        mockIAPLaunchInput.setIapListener(iapListenerMock);
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
        mockIAPInterface.getProductCartCount(iapListenerMock);
    }

    @Test
    public void getCompleteProductList() throws Exception {
        mockIAPInterface.getCompleteProductList(iapListenerMock);
    }
}
