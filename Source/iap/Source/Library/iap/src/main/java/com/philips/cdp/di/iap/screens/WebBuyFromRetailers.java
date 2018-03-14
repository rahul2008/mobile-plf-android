/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
        mProgress.setVisibility(View.VISIBLE);
        mUrl = getArguments().getString(IAPConstant.IAP_BUY_URL);
        initializeWebView(group);
        return group;
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
        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgress.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                if(url == null) return false;

                try {
                    if (url.startsWith("http:") || url.startsWith("https:"))
                    {
                        view.loadUrl(url);
                        return true;
                    }
                    else
                    {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    // Avoid crash due to not installed app which can handle the specific url scheme
                    return false;
                }
            }
        });

        mWebView.loadUrl(mUrl);
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