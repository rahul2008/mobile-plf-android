/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class WebPaymentFragmentTest {

    private static final String PAYMENT_SUCCESS_CALLBACK_URL = "http://www.philips.com/paymentSuccess";
    private static final String PAYMENT_PENDING_CALLBACK_URL = "http://www.philips.com/paymentPending";
    private static final String PAYMENT_FAILURE_CALLBACK_URL = "http://www.philips.com/paymentFailure";
    private static final String PAYMENT_CANCEL_CALLBACK_URL = "http://www.philips.com/paymentCancel";

    private Context mContext;
    private WebPaymentFragment webPaymentFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = getInstrumentation().getContext();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        final Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.WEB_PAY_URL, "http://google.com");
        webPaymentFragment = WebPaymentFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
    }

    @Test
    public void shouldDisplayWebPaymentFragment() throws Exception {
//        SupportFragmentController.of(webPaymentFragment).create().start().resume();
    }

    @Test
    public void shouldDisplayGetWebUrl() throws Exception {
        webPaymentFragment.getWebUrl();
//        SupportFragmentController.of(webPaymentFragment).create().start().resume();
    }

    @Test
    public void shouldOverrideURL() throws Exception {
        webPaymentFragment.shouldOverrideUrlLoading("http://google.com");
//        SupportFragmentController.of(webPaymentFragment).create().start().resume();
    }

    @Test
    public void shouldCalled_OnAttach() throws Exception {
        webPaymentFragment.onAttach(mContext);
//        SupportFragmentController.of(webPaymentFragment).create().start().resume();
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_OnResume() throws Exception {
        webPaymentFragment.onResume();
//        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCalled_OnPositiveBtnClick() throws Exception {
        webPaymentFragment.onPositiveBtnClick();
//        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }

    @Test
    public void shouldCalled_OnNegativeBtnClick() throws Exception {
        webPaymentFragment.onNegativeBtnClick();
//        SupportFragmentTestUtil.startFragment(webPaymentFragment);
    }
}