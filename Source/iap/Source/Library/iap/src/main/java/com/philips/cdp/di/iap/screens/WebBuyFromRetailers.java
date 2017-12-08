/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.uid.view.widget.ProgressBar;

public class WebBuyFromRetailers extends InAppBaseFragment {
    public static final String TAG = WebBuyFromRetailers.class.getName();
    private ProgressBar mProgress;
    private WebView mWebView;
    private String mUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.iap_web_payment, container, false);


        mProgress = createCustomProgressBar(group,MEDIUM);
        mProgress.setVisibility(View.GONE);
        mUrl = getArguments().getString(IAPConstant.IAP_BUY_URL);
        initializeWebView(group);
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
        setTitleAndBackButtonVisibility(title, true);
        IAPAnalytics.trackPage(IAPAnalyticsConstant.RETAILER_WEB_PAGE);
        mWebView.onResume();
    }

    public static WebBuyFromRetailers createInstance(Bundle args, AnimationType animType) {
        WebBuyFromRetailers fragment = new WebBuyFromRetailers();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    void initializeWebView(View group) {

        mWebView = (WebView) group.findViewById(R.id.wv_payment);
        mWebView.setInitialScale(1);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptEnabled(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.setScrollbarFadingEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            int webViewPreviousState;
            final int PAGE_STARTED = 0x1;

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                webViewPreviousState = PAGE_STARTED;
                if (mProgress != null) {
                    mProgress.setVisibility(View.VISIBLE);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (mProgress != null) {
                    mProgress.setVisibility(View.GONE);
                }
                super.onPageFinished(view,url);
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