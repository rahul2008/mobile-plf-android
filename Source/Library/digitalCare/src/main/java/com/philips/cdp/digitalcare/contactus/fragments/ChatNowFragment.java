/**
 * ChatNowFragment will help to inflate chat webpage on the screen.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 16 Jan 2015
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;

import java.util.HashMap;
import java.util.Map;


@SuppressLint("SetJavaScriptEnabled")
public class ChatNowFragment extends DigitalCareBaseFragment {

    private static final String TAG = ChatNowFragment.class.getSimpleName();

    private View mView = null;
    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;
    private String mUrl = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DigiCareLogger.i(TAG, "Showing Chat Now Screen");

        if (mView == null) {
            mView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }
        setChatEndPoint(getChatUrl() + "?origin=15_global_en_" + getAppName() + "-app_" + getAppName() + "-app");
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<String, String> contextData = new HashMap<String, String>();
        contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.PAGE_CONTACTUS_CHATNOW);
        contextData.put(AnalyticsConstants.PAGE_CONTACTUS_CHATNOW, getPreviousName());
      /*  AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_CHATNOW,
                getPreviousName(), contextData);*/
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                (AnalyticsConstants.PAGE_CONTACTUS_CHATNOW, contextData);
        initView();

        loadChat();
    }

    private void loadChat() {
        if (getChatUrl() == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {

            Utils.loadWebPageContent(getChatEndPoint(), mWebView, mProgressBar);
        }
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getChatUrl() {
        String chatLink = null;
        DigiCareLogger.i(TAG, "Chat Url Link : " + chatLink);
        chatLink = getResources().getString(R.string.live_chat_url);
        return chatLink;
    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.chat_now);
        DigiCareLogger.i(TAG, "Title : " + title);
        return title;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACTUS_CHATNOW;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }

    public String getChatEndPoint() {
        return mUrl;
    }

    public void setChatEndPoint(final String url) {

        if (url.startsWith("http://") || url.startsWith("https://"))
            mUrl = url;
    }

    @Override
    public void onStop() {
        super.onStop();
        clearWebViewData();
    }

    private void clearWebViewData() {

        mWebView.saveState(new Bundle());
    }
}
