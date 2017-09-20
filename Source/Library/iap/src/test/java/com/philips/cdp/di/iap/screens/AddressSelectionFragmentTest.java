package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.adapters.AddressSelectionAdapter;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AddressSelectionFragmentTest {
    private Context mContext;
    @Mock
    AddressSelectionFragment addrssAddressSelectionFragment;
    @Mock
    Message mockMessage;
    @Mock
    private AddressSelectionAdapter mockAdapter;

    @Mock
    AddressController mockAddressController;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        isNetworkAvailable();
        addrssAddressSelectionFragment = AddressSelectionFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
        InAppBaseFragment spyInAppBaseFragment = Mockito.spy(addrssAddressSelectionFragment);
        spyInAppBaseFragment.showFragment(addrssAddressSelectionFragment.getTag());
    }


    @Test
    public void shouldHandleBackEvent() throws Exception {
        Assert.assertFalse(addrssAddressSelectionFragment.handleBackEvent());
    }

    @Test(expected = NullPointerException.class)
    public void shouldOnGetAddress() throws Exception {
        mockMessage.what = RequestCode.DELETE_ADDRESS;
        addrssAddressSelectionFragment.onGetAddress(mockMessage);
        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOnGetAddress_GetShippingAddressData() throws Exception {
        mockMessage.obj = GetShippingAddressData.class;
        addrssAddressSelectionFragment.onGetAddress(mockMessage);
        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_onSetDeliveryAddress() throws Exception {
        when(mockAdapter.getSelectedPosition()).thenReturn(1);
        mockMessage.obj = IAPConstant.IAP_SUCCESS;
        addrssAddressSelectionFragment.onSetDeliveryAddress(mockMessage);
        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
    }

    @Test
    public void shouldCalled_onDestroy() throws Exception {
        addrssAddressSelectionFragment.unregisterEvents();
    }

    @Test
    public void shouldCalled_onGetDeliveryModes() throws Exception {
        mockMessage.obj = GetDeliveryModes.class;
        addrssAddressSelectionFragment.onGetDeliveryModes(mockMessage);

    }

    public void isNetworkAvailable() {
        final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }
}