package com.philips.cdp.digitalcare.social.facebook;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;

public class FacebookWebFragment extends DigitalCareBaseFragment {

    private final String TAG = FacebookWebFragment.class.getSimpleName();
    private View mView = null;
    private WebView mWebView = null;
    // private ProgressDialog mProgressDialog = null;
    private ProgressBar mProgressBar = null;
    private String FacebookURL = "http://www.facebook.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            if (mView != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
            mView = inflater.inflate(R.layout.common_webview, container, false);

        } catch (InflateException e) {
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        loadInAppFacebook();
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_FACEBOOK,
                getPreviousName());
    }

    private void loadInAppFacebook() {
        mWebView.loadUrl(getFacebookUrl());
        mWebView.getSettings().setJavaScriptEnabled(true);
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
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getFacebookUrl() {
        return FacebookURL
                + getActivity().getString(R.string.facebook_product_page);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.contact_us);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACTUS_FACEBOOK;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }

}