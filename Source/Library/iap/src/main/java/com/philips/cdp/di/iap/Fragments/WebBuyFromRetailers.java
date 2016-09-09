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

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.customviews.CircularLineProgressBar;


public class WebBuyFromRetailers extends InAppBaseFragment {
    public static final String TAG = WebBuyFromRetailers.class.getName();
    private CircularLineProgressBar mProgress;
    private WebView mWebView;
    private String mUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.iap_web_payment, container, false);

        mWebView = (WebView) group.findViewById(R.id.wv_payment);
        mProgress = (CircularLineProgressBar) group.findViewById(R.id.cl_progress);
        mProgress.startAnimation(70);
        mUrl = getArguments().getString(IAPConstant.IAP_BUY_URL);
        initializeWebView();
        return group;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getArguments().getString(IAPConstant.IAP_STORE_NAME);
        IAPAnalytics.trackPage(IAPAnalyticsConstant.RETAILER_WEB_PAGE_NAME);
        setTitleAndBackButtonVisibility(title, true);
        mWebView.onResume();
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
                mProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgress != null) {
                    mProgress.setVisibility(View.GONE);
                }
                if (mWebView.canGoBack()) {
                    handleBackEvent();
                }
            }
        });
    }


    @Override
    public boolean handleBackEvent() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

}