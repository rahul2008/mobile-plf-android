package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.InflateException;

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
}