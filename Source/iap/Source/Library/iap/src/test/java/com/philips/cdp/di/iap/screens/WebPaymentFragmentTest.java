package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

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

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class WebPaymentFragmentTest {
    private Context mContext;
    WebPaymentFragment webPaymentFragment;
    private static final String PAYMENT_SUCCESS_CALLBACK_URL = "http://www.philips.com/paymentSuccess";
    private static final String PAYMENT_PENDING_CALLBACK_URL = "http://www.philips.com/paymentPending";
    private static final String PAYMENT_FAILURE_CALLBACK_URL = "http://www.philips.com/paymentFailure";
    private static final String PAYMENT_CANCEL_CALLBACK_URL = "http://www.philips.com/paymentCancel";


    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        final Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.WEB_PAY_URL, "http://google.com");
        webPaymentFragment = WebPaymentFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayWebPaymentFragment() {
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayGetWebUrl() {
        webPaymentFragment.getWebUrl();
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOverrideURL() {
        webPaymentFragment.shouldOverrideUrlLoading("http://google.com");
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOverrideURL_PAYMENT_SUCCESS_CALLBACK_URL() {
        webPaymentFragment.shouldOverrideUrlLoading(PAYMENT_SUCCESS_CALLBACK_URL + "http://google.com");
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOverrideURL_PAYMENT_PENDING_CALLBACK_URL() {
        webPaymentFragment.shouldOverrideUrlLoading(PAYMENT_PENDING_CALLBACK_URL + "http://google.com");
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldOverrideURL_PAYMENT_FAILURE_CALLBACK_URL() {
        webPaymentFragment.shouldOverrideUrlLoading(PAYMENT_FAILURE_CALLBACK_URL + "http://google.com");
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOverrideURL_PAYMENT_CANCEL_CALLBACK_URL() {
        webPaymentFragment.shouldOverrideUrlLoading(PAYMENT_CANCEL_CALLBACK_URL + "http://google.com");
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_OnAttach() throws Exception {
        webPaymentFragment.onAttach(mContext);
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_OnResume() throws Exception {
        webPaymentFragment.onResume();
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_OnPositiveBtnClick() throws Exception {
        webPaymentFragment.onPositiveBtnClick();
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_OnNegativeBtnClick() throws Exception {
        webPaymentFragment.onNegativeBtnClick();
        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }
}