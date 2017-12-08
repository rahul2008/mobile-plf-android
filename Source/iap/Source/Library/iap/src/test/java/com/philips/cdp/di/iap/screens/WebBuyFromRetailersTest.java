package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class WebBuyFromRetailersTest {
    private Context mContext;
    WebBuyFromRetailers webBuyFromRetailers;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        webBuyFromRetailers = WebBuyFromRetailers.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {
        SupportFragmentTestUtil.startFragment(webBuyFromRetailers);
    }


    @Test(expected = NullPointerException.class)
    public void shouldOnViewCreated() {
       // webBuyFromRetailers = WebBuyFromRetailers.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        webBuyFromRetailers.onViewCreated(null, null);
        SupportFragmentTestUtil.startFragment(webBuyFromRetailers);
    }

    @Test(expected = NullPointerException.class)
    public void shouldOnResume() {
        // webBuyFromRetailers = WebBuyFromRetailers.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        webBuyFromRetailers.onResume();
        SupportFragmentTestUtil.startFragment(webBuyFromRetailers);
    }

    @Mock
    View viewMock;

    @Mock
    WebView webViewMock;

    @Mock
    WebSettings webSettingsMock;

    @Test
    public void shouldInitializeWebViews() throws Exception {
        Mockito.when(webViewMock.getSettings()).thenReturn(webSettingsMock);
        Mockito.when(viewMock.findViewById(R.id.wv_payment)).thenReturn(webViewMock);
        webBuyFromRetailers.initializeWebView(viewMock);

    }
}