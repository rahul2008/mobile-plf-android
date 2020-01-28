/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.adapters.AddressSelectionAdapter;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.addresses.Region;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AddressSelectionFragmentTest {

    @Mock
    private IAPAddressSelectionFragmentMock addrssAddressSelectionFragment;

    @Mock
    private Message mockMessage;

    @Mock
    private AddressSelectionAdapter mockAdapter;

    @Mock
    private DeliveryModes deliveryModesMock;

    @Mock
    private PaymentMethods paymentMethodsMock;

    @Mock
    private Bundle bundleMock;

    @Mock
    private Addresses addressesMock;

    @Mock
    private List<Addresses> mAddressesMock;

    @Mock
    private Country countryMock;

    @Mock
    private Region regionMock;

    @Before
    public void setUp() {
        initMocks(this);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        isNetworkAvailable();
        addrssAddressSelectionFragment = new IAPAddressSelectionFragmentMock();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IAPConstant.SET_DELIVERY_MODE,deliveryModesMock);
        addrssAddressSelectionFragment.setArguments(bundle);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
//        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
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
//        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOnGetAddress_GetShippingAddressData() throws Exception {
        mockMessage.obj = GetShippingAddressData.class;
        addrssAddressSelectionFragment.onGetAddress(mockMessage);
//        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_onSetDeliveryAddress() throws Exception {
        when(mockAdapter.getSelectedPosition()).thenReturn(1);
        mockMessage.obj = IAPConstant.IAP_SUCCESS;
        addrssAddressSelectionFragment.onSetDeliveryAddress(mockMessage);
//        SupportFragmentTestUtil.startFragment(addrssAddressSelectionFragment);
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

    @Test(expected = NullPointerException.class)
    public void shouldSetLayoutManager_OnActivityCreated() throws Exception {
        addrssAddressSelectionFragment.onActivityCreated(bundleMock);
    }

    @Test
    public void shouldEditAddress_WhenADDRESS_SELECTION_EVENT_EDITEventReceived() throws Exception {

        when(regionMock.getIsocodeShort()).thenReturn("911");
        when(regionMock.getIsocode()).thenReturn("911");
        when(addressesMock.getRegion()).thenReturn(regionMock);
        when(countryMock.getIsocode()).thenReturn("91");
        when(addressesMock.getCountry()).thenReturn(countryMock);
        when(addressesMock.getTitleCode()).thenReturn("Bangalore");
        when(mAddressesMock.get(0)).thenReturn(addressesMock);
        addrssAddressSelectionFragment.mAddresses=mAddressesMock;
        addrssAddressSelectionFragment.mAdapter=mockAdapter;
        addrssAddressSelectionFragment.onEventReceived(IAPConstant.ADDRESS_SELECTION_EVENT_EDIT);
    }

    @Test
    public void shouldADD_NEW_ADDRESS_WhenADD_NEW_ADDRESS_EventIsRecieved() throws Exception {
        addrssAddressSelectionFragment.onEventReceived(IAPConstant.ADD_NEW_ADDRESS);

    }

    @Test
    public void shouldCall_onGetPaymentDetails_WhenMessageIsEmpty() throws Exception {


        when(regionMock.getIsocodeShort()).thenReturn("911");
        when(regionMock.getIsocode()).thenReturn("911");
        when(addressesMock.getRegion()).thenReturn(regionMock);
        when(countryMock.getIsocode()).thenReturn("91");
        when(addressesMock.getCountry()).thenReturn(countryMock);
        when(addressesMock.getTitleCode()).thenReturn("Bangalore");

        mockMessage.obj="";
        when(mockAdapter.getSelectedPosition()).thenReturn(0);
        when(mAddressesMock.get(0)).thenReturn(addressesMock);
        addrssAddressSelectionFragment.mAddresses=mAddressesMock;
        addrssAddressSelectionFragment.mAdapter=mockAdapter;

        addrssAddressSelectionFragment.onGetPaymentDetails(mockMessage);
    }

    @Test
    public void shouldCall_onGetPaymentDetails_WhenMessageIsIAPNetworkError() throws Exception {
        when(regionMock.getIsocodeShort()).thenReturn("911");
        when(regionMock.getIsocode()).thenReturn("911");
        when(addressesMock.getRegion()).thenReturn(regionMock);
        when(countryMock.getIsocode()).thenReturn("91");
        when(addressesMock.getCountry()).thenReturn(countryMock);
        when(addressesMock.getTitleCode()).thenReturn("Bangalore");

        when(mockAdapter.getSelectedPosition()).thenReturn(0);
        when(mAddressesMock.get(0)).thenReturn(addressesMock);
        addrssAddressSelectionFragment.mAddresses=mAddressesMock;
        addrssAddressSelectionFragment.mAdapter=mockAdapter;
        mockMessage.obj=paymentMethodsMock;
        addrssAddressSelectionFragment.onGetPaymentDetails(mockMessage);
    }

    @Test
    public void shouldUnregisterEvents_WhenOnDestroyIsCalled() throws Exception {
        addrssAddressSelectionFragment.onDestroy();
        addrssAddressSelectionFragment.unregisterEvents();
    }

    private void isNetworkAvailable() {
        final ConnectivityManager connectivityManager = mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }
}