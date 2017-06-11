package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
    private String pageTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentWebviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, container, false);
        webView = binding.webview;
        Bundle arguments = getArguments();
        url = arguments.getString(KEY_URL);
        setWebViewClient();
        if (savedInstanceState == null) {
            webView.loadUrl(url);
        }
        return binding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(pageTitle != null) {
            (getActivity()).setTitle(pageTitle);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.setGroupVisible(R.id.main_menus, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @Override
    public boolean handleBackPress() {
        if(webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.handleBackPress();
    }

    private void setWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if(getActivity() != null) {
                    pageTitle = view.getTitle();
                    (getActivity()).setTitle(pageTitle);
                }
            }
        });
    }
}