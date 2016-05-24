/**
 * EMail WebView Fragment
 *
 * @author : pawan.kumar.deshpande@philips.com
 * Since  17 july 2015.
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.fragments;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.HashMap;
import java.util.Map;


public class EmailFragment extends DigitalCareBaseFragment {

    private View mView = null;
    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String EMAIL_URL = "https://%s/content/%s/%s_%s/support-home/support-contact-form-mrs.html?param1=%s";
    private String TAG = EmailFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_EMAIL);
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_EMAIL,
                getPreviousName(), contextData);
        initView();
        loadEmail();
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        mProgressBar.setVisibility(View.GONE);
    }


    private void loadEmail() {
        if (getEmailUrl() == null) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            String url = getEmailUrl() + "&origin=15_global_en_" + getAppName() + "-app_" + getAppName() + "-app";
            DigiCareLogger.d(TAG, url);
            setWebSettingForWebview(url, mWebView, mProgressBar);
           /* mWebView.setWebViewClient(new WebViewClient() {

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

            });*/
        }
    }

    private String getEmailUrl() {
        if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() == null)
            return null;
        String language = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getLanguage().toLowerCase();

        String country = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getCountry().toUpperCase();

        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();

        return String.format(EMAIL_URL, LocaleMatchHandler.getPRXUrl(language + "_" + country), consumerProductInfo.getSector(),
                language, country, consumerProductInfo.getCategory());
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.send_us_email);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACTUS_EMAIL;
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }
}
