/**
 * FAQ Detailed Screen renders the FAQ QUESTION & Answer Webpage with the required data.
 * Some of the response content will be hided dynamically after the response data renders completly
 *
 * @author naveen@philips.com
 * @Created 13-Apr-16.
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.faq.fragments;

import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.*;
import android.util.DisplayMetrics;
import android.view.*;
import android.webkit.*;
import android.widget.*;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.*;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.*;


public class FaqDetailedScreen extends DigitalCareBaseFragment {

    private View mView = null;
    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private Configuration mConfiguration = null;

    private String FAQ_PAGE_URL = null;
    private String TAG = FaqDetailedScreen.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }

        Map<String, Object> contextData = new HashMap<String, Object>();
        contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_FAQ);
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_FAQ_QUESTION_ANSWER,
                getPreviousName(), contextData);

        return mView;
    }

    public void setFaqWebUrl(String url) {
        FAQ_PAGE_URL = url;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mWebView = null;
        initView();
        loadFaq();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
        initView();
        loadFaq();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mView != null) ((WebView) mView.findViewById(R.id.webView)).saveState(outState);
    }

    @Override
    public void onPause() {
        mWebView.loadUrl("about:blank");
        clearWebViewData();
        super.onPause();
    }

    private void loadFaq() {
        if (FAQ_PAGE_URL == null) {
            mProgressBar.setVisibility(View.GONE);
        } else {
            //DigiCareLogger.d("URLTest", getPhilipsProductPageUrl());
            DigiCareLogger.d(TAG, FAQ_PAGE_URL);
            String url = FAQ_PAGE_URL;
            mWebView.getSettings().setJavaScriptEnabled(true);
            mProgressBar.setVisibility(View.VISIBLE);

            mWebView.setVisibility(View.INVISIBLE);

            mWebView.setWebChromeClient(new WebChromeClient());

            mWebView.getSettings().setStandardFontFamily("file:///android_asset/digitalcarefonts/CentraleSans-Book.otf");
          /*  mWebView.getSettings().setDefaultFontSize((int) getActivity().getResources().
                    getDimension(R.dimen.title_text_size_small));*/


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
                mWebView.getSettings().setAllowFileAccessFromFileURLs(true);
                mWebView.getSettings().setDomStorageEnabled(true);
            }
            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress > 80) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            });
            mWebView.setWebViewClient(new WebViewClient() {

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    DigiCareLogger.e("browser", description);
                    mProgressBar.setVisibility(View.GONE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    DigiCareLogger.d(TAG, "OnPage Finished invoked with URL " + url);
                    mProgressBar.setVisibility(View.GONE);

                  /*  mWebView.getSettings().setDefaultFontSize((int) getActivity().getResources().
                            getDimension(R.dimen.title_text_size_small));*/
//                    mWebView.loadUrl("javascript:try{document.getElementsByClassName('group faqfeedback_group')[0].style.display='none'}catch(e){}");
//                    Inject javascript code to the url given
                    //Not display the element
                    /*try {*/
                    setPaddingForWebdata();
                    mWebView.loadUrl("javascript:(function(){" + "document.getElementsByClassName('group faqfeedback_group')[0].remove();})()");
                    mWebView.loadUrl("javascript:window.CallToAnAndroidFunction.setVisible()");
                    /*} catch (NullPointerException ex) {
                        DigiCareLogger.e(TAG, "JavaScript Injection issue : " + ex);
                    }*/
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                }

            });
            //  mWebView.addJavascriptInterface(new JsObject(mWebView, mWebView), "CallToAnAndroidFunction");
            //Add a JavaScriptInterface, so I can make calls from the web to Java methods
            mWebView.addJavascriptInterface(new myJavaScriptInterface(), "CallToAnAndroidFunction");
            mWebView.loadUrl(FAQ_PAGE_URL);
        }
    }

    private void setPaddingForWebdata() {
        if (mWebView == null) initView();
        // mWebView.loadUrl("javascript:document.body.style.setProperty(\"color\", \"#003478\");");
        mWebView.loadUrl("javascript:document.body.style.setProperty(\"font-size\", \"100%\");");
        mWebView.loadUrl("javascript:document.body.style.setProperty(\"margin-top\", \"2%\");");
        mWebView.loadUrl("javascript:document.body.style.setProperty(\"margin-bottom\", \"2%\");");

       /* if (isTablet() && mConfiguration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mWebView.loadUrl("javascript:document.body.style.setProperty(\"margin-left\", \"20%\");");
            mWebView.loadUrl("javascript:document.body.style.setProperty(\"margin-right\", \"20%\");");
        } else {*/
        mWebView.loadUrl("javascript:document.body.style.setProperty(\"margin-left\", \"10%\");");
        mWebView.loadUrl("javascript:document.body.style.setProperty(\"margin-right\", \"10%\");");
        //}
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.QUESTION_KEY);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_FAQ_QUESTION_ANSWER;
    }

    private boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        return diagonalInches >= 6.5;
    }

    private void clearWebViewData() {
        mWebView.stopLoading();
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.clearFormData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView = null;
        }
    }

    private class myJavaScriptInterface {
        @JavascriptInterface
        public void setVisible() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
