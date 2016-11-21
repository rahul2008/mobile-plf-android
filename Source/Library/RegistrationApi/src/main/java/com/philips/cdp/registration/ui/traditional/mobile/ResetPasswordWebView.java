package com.philips.cdp.registration.ui.traditional.mobile;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.registration.R;

/**
 * Created by 310190722 on 6/21/2016.
 */
public class ResetPasswordWebView extends Fragment {

    private WebView mWebView;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_mobile_reset_password_webview, null);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        mWebView = (WebView) view.findViewById(R.id.reg_wv_reset_password_webview);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        }
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        showWebViewSpinner();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mWebView.loadUrl("https://dev.philips.co.in/myphilips/reset-password.html?cl=mob&loc=en_IN&code=q5sybj87nbsr4d");
        //mWebView.loadUrl("https://acc.philips.co.in/myphilips/reset-password.html?cl=mob&loc=en_IN&code=q5sybj87nbsr4d");
        //mWebView.loadUrl("https://www.philips.co.in/myphilips/reset-password.html?cl=mob&loc=en_IN&code=q5sybj87nbsr4d");
        mWebView.clearView();
        mWebView.measure(100, 100);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                error.getCertificate();
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                hideWebViewSpinner();
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }
        });

    }

    private void showWebViewSpinner() {
        if (!(getActivity().isFinishing()) && (mProgressDialog != null)) mProgressDialog.show();
    }

    private void hideWebViewSpinner() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
