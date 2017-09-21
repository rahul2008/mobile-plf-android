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
@Config(constants = BuildConfig.class, sdk = 21)
public class WebTrackUrlTest {
    private Context mContext;
    WebTrackUrl webTrackUrl;
    private Bundle bundle;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        bundle = new Bundle();

    }


    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {
        SupportFragmentTestUtil.startFragment(webTrackUrl);
    }

    @Test(expected = NullPointerException.class)
    public void shouldgetWebUrl() {
        bundle.getString(IAPConstant.ORDER_TRACK_URL);
        webTrackUrl = WebTrackUrl.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
        webTrackUrl.getWebUrl();
        SupportFragmentTestUtil.startFragment(webTrackUrl);

    }

    @Test(expected = RuntimeException.class)
    public void shouldgetWebUrlNull() {
        webTrackUrl = WebTrackUrl.createInstance(null, InAppBaseFragment.AnimationType.NONE);
        webTrackUrl.getWebUrl();
        SupportFragmentTestUtil.startFragment(webTrackUrl);

    }
}