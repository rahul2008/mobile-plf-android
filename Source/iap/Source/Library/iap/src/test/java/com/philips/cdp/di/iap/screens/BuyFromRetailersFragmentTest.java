/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.os.Bundle;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class BuyFromRetailersFragmentTest {

    @Mock
    private AppInfraInterface mockAppInfraInterface;

    @Mock
    private AppConfigurationInterface mockAppConfigurationInterface;

    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;

    private BuyFromRetailersFragment buyFromRetailersFragment;

    @Mock
    StoreEntity storeEntityMock;

    @Before
    public void setUp() {
        initMocks(this);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        final Bundle bundle = new Bundle();
        final ArrayList<String> value = new ArrayList<>();
        value.add("THis");
        bundle.putSerializable(IAPConstant.IAP_RETAILER_INFO, value);
        buyFromRetailersFragment = BuyFromRetailersFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
    }

    @Test
    public void shouldCalled_onClickAtRetailer() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);

        Mockito.when(storeEntityMock.getIsPhilipsStore()).thenReturn("Y");
        buyFromRetailersFragment.onClickAtRetailer("fdfd", storeEntityMock);
    }

    @Test
    public void shouldCalled_onClickAtRetailerWith_ICELEADS_HATCH() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
        Mockito.when(storeEntityMock.getIsPhilipsStore()).thenReturn("Y");
        buyFromRetailersFragment.onClickAtRetailer("http://ICELEADS_HATCH", storeEntityMock);
    }

    @Test
    public void shouldCalled_onClickAtRetailer_With_CHANNEL_ADVISOR() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
        Mockito.when(storeEntityMock.getIsPhilipsStore()).thenReturn("Y");
        buyFromRetailersFragment.onClickAtRetailer("http://CHANNEL_ADVISOR", storeEntityMock);
    }

    @Test
    public void shouldCalled_onClickAtRetailer_With_CHANNEL_SIGHT() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
        Mockito.when(storeEntityMock.getIsPhilipsStore()).thenReturn("Y");
        buyFromRetailersFragment.onClickAtRetailer("http://CHANNEL_SIGHT", storeEntityMock);
    }
}