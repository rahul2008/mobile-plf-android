package com.philips.cdp.digitalcare.faq;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * FaqFragment Webview
 *
 * @author : pawan.kumar.deshpande@philips.com
 * @since : 25 june 2015
 */
public class FaqFragment extends DigitalCareBaseFragment {

    private View mView = null;
    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;

    private String FAQ_URL = "http://www.philips.com/content/%s/%s_%s/standalone-faqs/%s.html";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.common_webview, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_FAQ);
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_FAQ,
                getPreviousName(), contextData);
        initView();

        loadFaq();
    }

    private void loadFaq() {
        if (getFaqUrl() == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            //DigiCareLogger.d("URLTest", getFaqUrl());
            String url = getFaqUrl() + "?origin=15_global_en_" + getAppName() + "-app_" + getAppName() + "-app";
            DigiCareLogger.d("URLTest", getFaqUrl());
            mWebView.loadUrl(url);
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
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getFaqUrl() {
        if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithLanguageFallBack() == null)
            return null;
        String language = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithLanguageFallBack()
                .getLanguage().toLowerCase();

        String country = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithLanguageFallBack()
                .getCountry().toUpperCase();

        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();

        return String.format(FAQ_URL, consumerProductInfo.getSector(),
                language, country, consumerProductInfo.getCtn());
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.view_faq);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_FAQ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }

}
