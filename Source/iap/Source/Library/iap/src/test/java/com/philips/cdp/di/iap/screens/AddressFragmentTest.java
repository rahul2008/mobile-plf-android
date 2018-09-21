package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)

public class AddressFragmentTest {
    private Context mContext;
    private AddressFragment addressFragment;

    private AddressPresenter addressPresenter;
    @Mock
    private AddressContractor addressContractorMock;
    @Mock
    private android.view.View billingViewMock;
    @Mock
    private android.view.View shippingViewMock;

    @Before
    public void setUp() {
        initMocks(this);
        addressFragment = AddressFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        mContext = RuntimeEnvironment.application;
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

    @Mock
    View viewMock;

    @Mock
    TextView tv_checkOutStepsMock;

    @Mock
    Fragment shippingFragmentMock;

    @Mock
    Fragment billingFragmentMock;

    @Mock
    Button buttonContinueMock;

    @Mock
    Button buttonCancelMock;

    // TextView tv_checkOutSteps = (TextView) rootView.findViewById(R.id.tv_checkOutSteps);

    /* s mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);*/

    @Test(expected = NullPointerException.class)
    public void shouldInitializeViews() throws Exception {

        initViews();

       /* Mockito.when(addressFragment.getFragmentByID(R.id.fragment_shipping_address)).thenReturn(shippingFragmentMock);
        Mockito.when(addressFragment.getFragmentByID(R.id.fragment_billing_address)).thenReturn(billingFragmentMock);
*/

        addressFragment.initializeViews(viewMock,addressPresenter);

    }

    private void initViews(){
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
        addressFragment.mBtnContinue=buttonContinueMock;
        viewMock= addressFragment.mBtnContinue;
        addressFragment.onClick(viewMock);

    }
    @Mock
    AddressFields addressFieldsMock;

    @Test
    public void shouldReturnAddressFiled_WhenAddressPayloadIsCalled() throws Exception {
        Mockito.when(addressFieldsMock.getTitleCode()).thenReturn("abcdef");
        Mockito.when(addressFieldsMock.getPostalCode()).thenReturn("123456");
        Mockito.when(addressFieldsMock.getPhone1()).thenReturn("1234567890");
        addressPresenter.addressPayload(addressFieldsMock);
    }
}