package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.address.AddressFields;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class DLSAddressFragmentTest {
    private Context mContext;
    private DLSAddressFragment dlsAddressFragment;

    @Before
    public void setUp() {
        initMocks(this);
        dlsAddressFragment = DLSAddressFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = InflateException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(dlsAddressFragment);
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

    @Test(expected = IllegalStateException.class)
    public void shouldInitializeViews() throws Exception {

        initViews();

       /* Mockito.when(dlsAddressFragment.getFragmentByID(R.id.fragment_shipping_address)).thenReturn(shippingFragmentMock);
        Mockito.when(dlsAddressFragment.getFragmentByID(R.id.fragment_billing_address)).thenReturn(billingFragmentMock);
*/

        dlsAddressFragment.initializeViews(viewMock);

    }

    private void initViews(){
        dlsAddressFragment.onAttach(mContext);
        dlsAddressFragment.onAttachFragment(shippingFragmentMock);
        dlsAddressFragment.onAttachFragment(billingFragmentMock);
        Mockito.when(viewMock.findViewById(R.id.tv_checkOutSteps)).thenReturn(tv_checkOutStepsMock);
        Mockito.when(viewMock.findViewById(R.id.btn_continue)).thenReturn(buttonContinueMock);
        Mockito.when(viewMock.findViewById(R.id.btn_cancel)).thenReturn(buttonCancelMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldSaveShippingAddress() throws Exception {
        initViews();
        Mockito.when(buttonContinueMock.getText()).thenReturn("Save");
        dlsAddressFragment.mBtnContinue=buttonContinueMock;
        viewMock=dlsAddressFragment.mBtnContinue;
        dlsAddressFragment.onClick(viewMock);

    }
    @Mock
    AddressFields addressFieldsMock;

    @Test
    public void shouldReturnAddressFiled_WhenAddressPayloadIsCalled() throws Exception {
        Mockito.when(addressFieldsMock.getTitleCode()).thenReturn("abcdef");
        Mockito.when(addressFieldsMock.getPostalCode()).thenReturn("123456");
        Mockito.when(addressFieldsMock.getPhone1()).thenReturn("1234567890");
        dlsAddressFragment.addressPayload(addressFieldsMock);

    }
}