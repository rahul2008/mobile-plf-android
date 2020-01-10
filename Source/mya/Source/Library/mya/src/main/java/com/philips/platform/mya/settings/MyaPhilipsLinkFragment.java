package com.philips.platform.mya.settings;

import android.content.Context;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.platform.mya.R;
import com.philips.platform.mya.base.MyaBaseFragment;


public class MyaPhilipsLinkFragment extends MyaBaseFragment {

    private WebView webview;
    public static String PHILIPS_LINK = "philips_link";

    @Override
    public int getActionbarTitleResId() {
        return R.string.MYA_My_account;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return context.getString(R.string.MYA_My_account);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_philips_link, container, false);
        webview = view.findViewById(R.id.webView);
        WebSettings settings = webview.getSettings();
        settings.setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.clearHistory();
        webview.measure(100, 100);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        String philips_link = null;
        if (getArguments() != null) {
            philips_link = getArguments().getString(PHILIPS_LINK);
        }
        if (getArguments() != null) {
            webview.loadUrl(philips_link);
        }
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (request.getUrl() != null) {
                    view.loadUrl(request.getUrl().toString());
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                error.getCertificate();
                Log.d("MyPhilipsAccount", "onReceivedSslError : "+error.getCertificate().toString());
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                Log.d("MyPhilipsAccount", "Page Finished");
            }

        });
        return view;
    }
}
