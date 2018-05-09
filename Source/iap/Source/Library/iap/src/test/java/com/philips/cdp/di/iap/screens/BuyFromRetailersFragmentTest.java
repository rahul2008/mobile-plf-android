package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class BuyFromRetailersFragmentTest {
    private Context mContext;
    BuyFromRetailersFragment buyFromRetailersFragment;
    @Mock
    AppInfraInterface mockAppInfraInterface;
    @Mock
    AppConfigurationInterface mockAppConfigurationInterface;
    @Mock
    AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
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
        buyFromRetailersFragment.onClickAtRetailer("fdfd", "dfd");
    }

    @Test
    public void shouldCalled_onClickAtRetailerWith_ICELEADS_HATCH() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
        buyFromRetailersFragment.onClickAtRetailer("http://ICELEADS_HATCH", "dfd");
    }

    @Test
    public void shouldCalled_onClickAtRetailer_With_CHANNEL_ADVISOR() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
        buyFromRetailersFragment.onClickAtRetailer("http://CHANNEL_ADVISOR", "dfd");
    }

    @Test
    public void shouldCalled_onClickAtRetailer_With_CHANNEL_SIGHT() throws Exception {
        Mockito.when(mockAppConfigurationError.getErrorCode()).thenReturn(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.DeviceStoreError);
        Mockito.when(mockAppConfigurationInterface.getPropertyForKey("propositionid", "IAP", mockAppConfigurationError)).thenReturn(true);
        Mockito.when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);

        SupportFragmentTestUtil.startFragment(buyFromRetailersFragment);
        buyFromRetailersFragment.onClickAtRetailer("http://CHANNEL_SIGHT", "dfd");
    }
}