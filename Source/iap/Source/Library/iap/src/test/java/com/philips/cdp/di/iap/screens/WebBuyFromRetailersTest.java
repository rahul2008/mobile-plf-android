/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.IAPUtility;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentController;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class WebBuyFromRetailersTest {
    private Context mContext;
    WebBuyFromRetailers webBuyFromRetailers;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = getInstrumentation().getContext();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
        webBuyFromRetailers = WebBuyFromRetailers.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
//        SupportFragmentController.of(webBuyFromRetailers).create().start().resume();
    }


    @Test
    public void shouldOnViewCreated() {
        webBuyFromRetailers.onViewCreated(null, null);
//        SupportFragmentController.of(webBuyFromRetailers).create().start().resume();
    }

    @Test(expected = NullPointerException.class)
    public void shouldOnResume() {
        webBuyFromRetailers.onResume();
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

    @Test
    public void shouldTestPhilipsShopTaggedUrlWithoutParameter() {

         /* "append to links without parameters present:
                ?origin=15_global_en_<appName>-app_<appName>-app

        append to links with parameters (and a question mark) present:
&origin=15_global_en_<appName>-app_<appName>-app"*/

        IAPUtility.getInstance().setAppName("Carrier App");
        IAPUtility.getInstance().setLocaleTag("en");

        String expected  = "https://www.buy.philips.co.in?origin=15_global_en_Carrier%20App-app_Carrier%20App-app" ;
        String actual =     webBuyFromRetailers.getPhilipsFormattedUrl("https://www.buy.philips.co.in");
        Assert.assertEquals( expected,actual);
    }

    @Test
    public void shouldTestPhilipsShopTaggedUrlWithParameter() {

         /* "append to links without parameters present:
                ?origin=15_global_en_<appName>-app_<appName>-app

        append to links with parameters (and a question mark) present:
&origin=15_global_en_<appName>-app_<appName>-app"*/

        IAPUtility.getInstance().setAppName("Carrier App");
        IAPUtility.getInstance().setLocaleTag("en");

        String expected  = "https://www.buy.philips.co.in/dp/B00TI5ZK5I/?cstrackid=1056bbef-5e88-4cdc-b5ae-6670c18550dd&tag=wwwphilipsusa-20&origin=15_global_en_Carrier%20App-app_Carrier%20App-app" ;

        String actual = webBuyFromRetailers.getPhilipsFormattedUrl("https://www.buy.philips.co.in/dp/B00TI5ZK5I/?cstrackid=1056bbef-5e88-4cdc-b5ae-6670c18550dd&tag=wwwphilipsusa-20");
        Assert.assertEquals( expected,actual);
    }
}