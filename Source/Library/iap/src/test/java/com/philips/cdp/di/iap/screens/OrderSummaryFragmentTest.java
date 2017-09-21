package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.utils.IAPConstant;

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
public class OrderSummaryFragmentTest {
    private Context mContext;
    OrderSummaryFragment orderSummaryFragment;
    @Mock
    PaymentMethod mockPaymentMethod;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        Mockito.when(mockPaymentMethod.getBillingAddress()).thenReturn(new Addresses());
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(IAPConstant.SELECTED_PAYMENT,mockPaymentMethod);

        orderSummaryFragment = OrderSummaryFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
        SupportFragmentTestUtil.startFragment(orderSummaryFragment);
    }
}