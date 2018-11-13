/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AddressFragmentTest {

    private Context mContext;

    private AddressFragment addressFragment;

    private AddressPresenter addressPresenter;

    @Mock
    private AddressContractor addressContractorMock;

    @Mock
    private View billingViewMock;

    @Mock
    private View shippingViewMock;

    @Mock
    private View viewMock;

    @Mock
    private TextView tv_checkOutStepsMock;

    @Mock
    private Fragment shippingFragmentMock;

    @Mock
    private Fragment billingFragmentMock;

    @Mock
    private Button buttonContinueMock;

    @Mock
    private Button buttonCancelMock;

    @Mock
    private AddressFields addressFieldsMock;

    @Before
    public void setUp() {
        initMocks(this);

        addressFragment = AddressFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        mContext = getInstrumentation().getContext();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        Mockito.when(addressContractorMock.getBillingAddressView()).thenReturn(billingViewMock);
        Mockito.when(addressContractorMock.getShippingAddressView()).thenReturn(shippingViewMock);
        addressPresenter = new AddressPresenter(addressContractorMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(addressFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldInitializeViews() throws Exception {
        initViews();

        addressFragment.initializeViews(viewMock, addressPresenter);
    }

    private void initViews() {
        addressFragment.onAttach(mContext);
        addressFragment.onAttachFragment(shippingFragmentMock);
        addressFragment.onAttachFragment(billingFragmentMock);
        Mockito.when(viewMock.findViewById(R.id.tv_checkOutSteps)).thenReturn(tv_checkOutStepsMock);
        Mockito.when(viewMock.findViewById(R.id.btn_continue)).thenReturn(buttonContinueMock);
        Mockito.when(viewMock.findViewById(R.id.btn_cancel)).thenReturn(buttonCancelMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldSaveShippingAddress() throws Exception {
        initViews();
        Mockito.when(buttonContinueMock.getText()).thenReturn("Save");
        addressFragment.mBtnContinue = buttonContinueMock;
        viewMock = addressFragment.mBtnContinue;
        addressFragment.onClick(viewMock);
    }

    @Test
    public void shouldReturnAddressFiled_WhenAddressPayloadIsCalled() throws Exception {
        Mockito.when(addressFieldsMock.getTitleCode()).thenReturn("abcdef");
        Mockito.when(addressFieldsMock.getPostalCode()).thenReturn("123456");
        Mockito.when(addressFieldsMock.getPhone1()).thenReturn("1234567890");
        addressPresenter.addressPayload(addressFieldsMock);
    }
}