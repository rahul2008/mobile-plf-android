package com.philips.cdp.di.iap.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.BidiFormatter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.IAPRobolectricGradleTestRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.assertj.android.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;

/**
 * Created by Apple on 04/07/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(IAPRobolectricGradleTestRunner.class)
public class WebFragmentTest {

    @Before
    public void setup(){

    }

    @Test(expected = RuntimeException.class)
    public void testAppFragmentStart() throws Exception{
        final WebFragment fragment = new WebFragment();

        SupportFragmentTestUtil.startFragment(fragment, FragmentActivity.class);

        // My test examples - hamcrest matchers
        Assert.assertThat(fragment, CoreMatchers.not(CoreMatchers.nullValue()));
        Assert.assertThat(fragment.getView(), CoreMatchers.not(CoreMatchers.nullValue()));
        Assert.assertThat(fragment.getActivity(), CoreMatchers.not(CoreMatchers.nullValue()));
        Assert.assertThat(fragment.getActivity(), CoreMatchers.instanceOf(FragmentActivity.class));

        // Your tests
        View webView = fragment.getView().findViewById(R.id.wv_payment);
        View progressView = fragment.getView().findViewById(R.id.cl_progress);

        Assert.assertNotSame(webView.getVisibility(), View.VISIBLE);
        Assert.assertNotSame(progressView.getVisibility(), View.VISIBLE);
    }
    @Test
    public void shouldNotNull() throws Exception {
        WebFragment fragment = new WebFragment();
        IAPBaseFragmentTestRunner.addFragment(fragment, fragment.getClass().getName());
        assertNotNull(fragment);
    }

    @Test
    public void shouldHaveWebview() throws Exception{
        WebFragment webFragment = new WebFragment();
        View view = View.inflate(webFragment.getContext(), R.layout.iap_web_payment, null);
        WebView webView = (WebView) view.findViewById(R.id.wv_payment);
//        Bundle bundle = new Bundle();
//        bundle.putString(IAPConstant.ORDER_TRACK_URL,"http://www.google.com");
//        webFragment.getArguments();
//        webFragment.getWebUrl();
        Assertions.assertThat(webView);
    }
}