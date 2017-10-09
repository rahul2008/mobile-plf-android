package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
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
        dlsAddressFragment.mContext=mContext;
        dlsAddressFragment.onAttachFragment(shippingFragmentMock);
        dlsAddressFragment.onAttachFragment(billingFragmentMock);
        Mockito.when(viewMock.findViewById(R.id.tv_checkOutSteps)).thenReturn(tv_checkOutStepsMock);

        Mockito.when(viewMock.findViewById(R.id.btn_continue)).thenReturn(buttonContinueMock);
        Mockito.when(viewMock.findViewById(R.id.btn_cancel)).thenReturn(buttonCancelMock);
       /* Mockito.when(dlsAddressFragment.getFragmentByID(R.id.fragment_shipping_address)).thenReturn(shippingFragmentMock);
        Mockito.when(dlsAddressFragment.getFragmentByID(R.id.fragment_billing_address)).thenReturn(billingFragmentMock);
*/

        dlsAddressFragment.initializeViews(viewMock);

    }
}