package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CancelOrderFragmentTest {
    private Context mContext;
    CancelOrderFragment cancelOrderFragment;

    @Before
    public void setUp() {
        initMocks(this);
        final Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.CUSTOMER_CARE_NUMBER,"df");
        bundle.putString(IAPConstant.CUSTOMER_CARE_WEEKDAYS_TIMING,"df");
        bundle.putString(IAPConstant.CUSTOMER_CARE_SATURDAY_TIMING,"df");
        cancelOrderFragment = CancelOrderFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(cancelOrderFragment);
    }

}