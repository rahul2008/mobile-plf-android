/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
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
        mContext = getInstrumentation().getContext();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {

//        SupportFragmentTestUtil.startFragment(cancelOrderFragment);
    }

}