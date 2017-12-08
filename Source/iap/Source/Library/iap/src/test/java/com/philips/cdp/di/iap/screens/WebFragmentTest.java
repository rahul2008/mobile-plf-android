package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class WebFragmentTest {

    private Context mContext;

    private WebFragment webFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        final Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.WEB_PAY_URL, "http://google.com");
        webFragment = new WebFragment() {
            @Override
            protected String getWebUrl() {
                return "http://google.com";
            }
        };
    }

    @Test(expected = IllegalStateException.class)
    public void shouldDisplayWebPaymentFragment() {
        SupportFragmentTestUtil.startFragment(webFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldonViewCreated() {
        webFragment.onViewCreated(mock(View.class), mock(Bundle.class));
        SupportFragmentTestUtil.startFragment(webFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldonPause() {
        webFragment.onPause();
        SupportFragmentTestUtil.startFragment(webFragment);
    }
}
