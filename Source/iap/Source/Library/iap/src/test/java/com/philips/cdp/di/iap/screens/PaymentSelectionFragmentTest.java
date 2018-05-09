package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class PaymentSelectionFragmentTest {
    private Context mContext;
    PaymentSelectionFragment paymentSelectionFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
        paymentSelectionFragment = PaymentSelectionFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        SupportFragmentTestUtil.startFragment(paymentSelectionFragment);
    }
}