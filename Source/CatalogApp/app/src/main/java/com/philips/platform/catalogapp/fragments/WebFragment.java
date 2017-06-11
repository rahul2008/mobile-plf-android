package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentWebviewBinding;

public class WebFragment extends BaseFragment {
    public static final String KEY_URL = "url";
    private WebView webView;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentWebviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        webView = binding.webview;
        Bundle arguments = getArguments();
        url = arguments.getString(KEY_URL);
        setWebViewClient();
        webView.loadUrl(url);
        return binding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return 0;
    }

    public void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(getActivity() != null) {
                    (getActivity()).setTitle(view.getTitle());
                }
            }
        });
    }
}