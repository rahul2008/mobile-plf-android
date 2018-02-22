package com.philips.platform.mya.settings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webview.setWebViewClient(new WebViewClient());
        assert getArguments() != null;
        CharSequence philips_link = getArguments().getCharSequence(PHILIPS_LINK);
        assert philips_link != null;
        webview.loadUrl(philips_link.toString());
    }
}
