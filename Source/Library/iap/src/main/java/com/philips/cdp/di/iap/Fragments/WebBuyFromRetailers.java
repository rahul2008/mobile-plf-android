/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.customviews.CircularLineProgressBar;


public class WebBuyFromRetailers extends WebFragment {
    public static final String TAG = WebBuyFromRetailers.class.getName();

    @Override
    protected String getWebUrl() {
        Bundle bundle = getArguments();
        return bundle.getString(IAPConstant.IAP_BUY_URL);
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.RETAILER_WEB_PAGE_NAME);
        setTitle(getArguments().getString(IAPConstant.IAP_STORE_NAME));
        initializeWebView();
    }

    public static WebBuyFromRetailers createInstance(Bundle args, AnimationType animType) {
        WebBuyFromRetailers fragment = new WebBuyFromRetailers();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    private void initializeWebView() {
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setWebViewClient(new WebViewClient() {
            int webViewPreviousState;
            final int PAGE_STARTED = 0x1;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webViewPreviousState = PAGE_STARTED;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mWebView.canGoBack()) {
                    handleBackEvent();
                }
            }
        });
    }


    @Override
    public boolean handleBackEvent() {
        // hideKeyboard();
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

}