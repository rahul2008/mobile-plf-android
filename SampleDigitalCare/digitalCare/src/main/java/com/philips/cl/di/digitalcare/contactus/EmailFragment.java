package com.philips.cl.di.digitalcare.contactus;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.philips.cl.di.digitalcare.ConsumerProductInfo;
import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.util.DigiCareLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * EMail WebView Fragment
 *
 * @author : pawan.kumar.deshpande@philips.com
 *         Since  17 july 2015.
 */
public class EmailFragment extends DigitalCareBaseFragment {

    private View mView = null;
    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;
    //http://www.philips.nl/content/B2C/nl_NL/support-home/support-contact-form.html?param1=ESPRESSO_CA
    private String EMAIL_URL = "http://www.philips.com/content/%s/%s_%s/support-home/support-contact-form.html?param1=ESPRESSO_CA";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_EMAIL,
                getPreviousName(), contextData);
        initView();
        loadEmail();
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }


    private void loadEmail() {
        if (getEmailUrl() == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            String url = getEmailUrl() + "?origin=15_global_en_" + getAppName() + "-app_" + getAppName() + "-app";
            DigiCareLogger.d("URLTest", getEmailUrl());
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

    private String getEmailUrl() {
        if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseLocale() == null)
            return null;
        String language = DigitalCareConfigManager.getInstance().getLocaleMatchResponseLocale()
                .getLanguage().toLowerCase();

        String country = DigitalCareConfigManager.getInstance().getLocaleMatchResponseLocale()
                .getCountry().toUpperCase();

        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();

        return String.format(EMAIL_URL, consumerProductInfo.getSector(),
                language, country, consumerProductInfo.getCtn());
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
        return AnalyticsConstants.PAGE_CONTACT_US;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }
}
