/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.di.iap.R;
import com.philips.platform.uid.view.widget.ProgressBar;

public abstract class WebFragment extends InAppBaseFragment {

    public static final String TAG = WebFragment.class.getName();
    protected WebView mWebView;
    private String mUrl;
    private ProgressBar mProgress;
    private boolean mShowProgressBar = true;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.iap_web_payment, container, false);

        mWebView = (WebView) viewGroup.findViewById(R.id.wv_payment);
        mWebView.setWebViewClient(new IAPWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(false);

        mProgress = createCustomProgressBar(viewGroup,MEDIUM);
        mProgress.setVisibility(View.GONE);

        mUrl = getWebUrl();
        return viewGroup;
    }

    void initProgressBar(){

    }
    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    protected abstract String getWebUrl();

    protected boolean shouldOverrideUrlLoading(final String url) {
        return false;
    }

    private boolean shouldHandleError(final int errorCode) {
        return (errorCode == WebViewClient.ERROR_CONNECT
                || errorCode == WebViewClient.ERROR_BAD_URL
                || errorCode == WebViewClient.ERROR_TIMEOUT
                || errorCode == WebViewClient.ERROR_HOST_LOOKUP);
    }

    private class IAPWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            return WebFragment.this.shouldOverrideUrlLoading(url);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return WebFragment.this.shouldOverrideUrlLoading(request.getUrl().toString());
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgress.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            if (rerr != null && shouldHandleError(rerr.getErrorCode())) {
                if (isVisible()) {
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                }
            }
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            if (mProgress != null && mShowProgressBar) {
                mShowProgressBar = false;
                mProgress.setVisibility(View.GONE);
            }
            super.onPageFinished(view, url);
        }
    }
}
