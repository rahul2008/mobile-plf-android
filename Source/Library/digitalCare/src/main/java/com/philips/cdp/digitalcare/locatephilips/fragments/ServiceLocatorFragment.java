package com.philips.cdp.digitalcare.locatephilips.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * Created by 310166779 on 12/14/2016.
 */

public class ServiceLocatorFragment extends DigitalCareBaseFragment {

    private WebView mServiceLocatorWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String TAG = ServiceLocatorFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.consumercare_webview_noscroll, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        loadServiceLocatorPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    private void loadServiceLocatorPage() {
        mServiceLocatorWebView.getSettings().setDomStorageEnabled(true);
        mServiceLocatorWebView.getSettings().setBuiltInZoomControls(true);
        loadWebPageContent(getServiceLocatorUrl(), mServiceLocatorWebView, mProgressBar);
    }

    private void initView(View view) {
        mActionBarMenuIcon = (ImageView) view.findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) view.findViewById(R.id.back_to_home_img);
        mServiceLocatorWebView = (WebView) view.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.find_philips_near_you);
        DigiCareLogger.i(TAG, "Philips Service Locator Page title : " + title);
        return title;
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_SERVICE_LOCATOR;
    }

    @Override
    public void onClick(View view) {

    }

    private String getServiceLocatorUrl() {
        String serviceLocatorLink = null;
        serviceLocatorLink = getResources().getString(R.string.service_locator_url);
        DigiCareLogger.i(TAG, "Service locator Url Link : " + serviceLocatorLink);
        return serviceLocatorLink;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceLocatorWebView != null) {
            mServiceLocatorWebView = null;
        }
    }

    protected String loadWebPageContent(final String webpageUrl, final WebView webView, final ProgressBar progressBar) {
        webView.loadUrl(webpageUrl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

        });

        return webView.getUrl();
    }
}
