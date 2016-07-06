package com.philips.cdp.di.iap.Fragments;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.BidiFormatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;

/**
 * Created by Apple on 04/07/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(IAPRobolectricGradleTestRunner.class)
public class WebFragmentTest {

    private ViewGroup viewGroup;
    private LayoutInflater inflator;

    @Before
    public void setup() {
        Application application = RuntimeEnvironment.application;
        inflator = LayoutInflater.from(application);
        viewGroup = new RelativeLayout(application.getApplicationContext());
    }

    @Test
    public void ShouldCreateViewWhenOnCreateViewIsCalled() throws Exception {
        final WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.ORDER_TRACK_URL, "www.google.com");
        fragment.setArguments(bundle);

        View view = fragment.onCreateView(inflator, viewGroup, null);

        assertNotNull(view);
    }


    @Test(expected = RuntimeException.class)
    public void ShouldThrowRuntimeException_WhenUrlIsNotPassedOrMethodGetWebUrlIsCalled() throws Exception {
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


}