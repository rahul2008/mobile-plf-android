/**
 * Product Specific PhilipsProduct Manual.
 *
 * @author : naveen@philips.com
 * @since : 16 Nov 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.productdetails;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;



public class ProductManualFragment extends DigitalCareBaseFragment {

    private View mView = null;
    private WebView mWebView = null;
    private ScrollView mScrollView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String mUrl = null;

    private String PRODUCT_PAGE_URL = "http://docs.google.com/gview?embedded=true&url=%s";

    private String TAG = ProductManualFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }
        mUrl = getPhilipsProductPageUrl();

        /*AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_VIEW_PRODUCT_MANUAL,
                getPreviousName());*/
        DigitalCareConfigManager.getInstance().getTaggingInterface().
                trackPageWithInfo(AnalyticsConstants.PAGE_VIEW_PRODUCT_MANUAL,
                        getPreviousName(), getPreviousName());

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        initView();
        loadPdfManual();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    @SuppressWarnings("deprecation")
    private void loadPdfManual() {
        if (getPhilipsProductPageUrl() == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            //DigiCareLogger.d("URLTest", getPhilipsProductPageUrl());
            DigiCareLogger.d(TAG, getPhilipsProductPageUrl());
            mWebView.getSettings().setJavaScriptEnabled(true);
            if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) &&
                    (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO)) {
                mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            }
            mWebView.getSettings().setAllowFileAccess(true);
            mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            mWebView.setVerticalScrollBarEnabled(false);
            mWebView.setHorizontalScrollBarEnabled(false);
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(View.GONE);
                }

            });

            mWebView.loadUrl(mUrl);

        }
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mScrollView = (ScrollView) mView.findViewById(R.id.digitalcare_webview_scrollview);
        mScrollView.setScrollContainer(false);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getPhilipsProductPageUrl() {

        String manualUrl = DigitalCareConfigManager
                .getInstance().getViewProductDetailsData().getManualLink();

        //  return manualUrl;
        return String.format(PRODUCT_PAGE_URL, manualUrl);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.product_info);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_VIEW_PRODUCT_MANUAL;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.setHorizontalScrollBarEnabled(false);

        initView();
        loadPdfManual();
    }
}
