/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.screens;

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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.analytics.IAPAnalytics;
import com.ecs.demouapp.ui.analytics.IAPAnalyticsConstant;
import com.ecs.demouapp.ui.session.NetworkConstants;
import com.ecs.demouapp.ui.utils.IAPConstant;
import com.ecs.demouapp.ui.utils.IAPUtility;

import java.net.MalformedURLException;
import java.net.URL;

public class WebBuyFromRetailers extends InAppBaseFragment {
    public static final String TAG = WebBuyFromRetailers.class.getName();
    private WebView mWebView;
    private String mUrl;
    private boolean isPhilipsShop = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.iap_web_payment, container, false);
        createCustomProgressBar(group,BIG);
        mUrl = getArguments().getString(IAPConstant.IAP_BUY_URL);
        isPhilipsShop = getArguments().getBoolean(IAPConstant.IAP_IS_PHILIPS_SHOP);
        initializeWebView(group);
        return group;
    }

    @Override
    public void onResume() {
        super.onResume();
        String title = getArguments().getString(IAPConstant.IAP_STORE_NAME);
        setTitleAndBackButtonVisibility(title, true);
        setCartIconVisibility(false);
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
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressBar();

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String tagUrl = url ;
                if(isPhilipsShop){
                   tagUrl = getPhilipsFormattedUrl(url);
                }
                IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,IAPAnalyticsConstant.KEY_EXIT_LINK_RETAILER,tagUrl);
                super.onPageStarted(view, url, favicon);
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

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view,handler,error);
                handler.proceed(); // Ignore SSL certificate errors
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideProgressBar();
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                if (rerr != null && shouldHandleError(rerr.getErrorCode())) {
                    if (isVisible()) {
                        onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                    }
                }
            }
        });

        mWebView.loadUrl(mUrl);
    }

    public String getPhilipsFormattedUrl(String url) {

        String appName = IAPUtility.getInstance().getAppName();
        String localeTag = IAPUtility.getInstance().getLocaleTag();
        Uri.Builder builder = new Uri.Builder().appendQueryParameter("origin", String.format(IAPAnalyticsConstant.PHILIPS_EXIT_LINK_PARAMETER,localeTag,appName,appName));

          if(isParameterizedURL(url)){
              return  url + "&" + builder.toString().replace("?","");
          }else{
              return url + builder.toString();
          }
    }

    private boolean isParameterizedURL(String url) {

        try {
            URL urlString  = new URL(url);
            return urlString.getQuery() != null || urlString.getQuery().length()!=0;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean shouldHandleError(final int errorCode) {
        return (errorCode == WebViewClient.ERROR_CONNECT
                || errorCode == WebViewClient.ERROR_BAD_URL
                || errorCode == WebViewClient.ERROR_TIMEOUT
                || errorCode == WebViewClient.ERROR_HOST_LOOKUP);
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
