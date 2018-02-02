package com.philips.platform.mya.csw.description;

import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.R;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PrivacyNoticeFragment extends CswBaseFragment {
    private String privacyNoticeUrl;

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {

    }

    @Override
    public int getTitleResourceId() {
        return R.string.csw_privacy_settings;
    }

    public void setUrl(String url) {
        privacyNoticeUrl = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View privacyNoticeView = inflater.inflate(R.layout.csw_privacy_notice_view, container, false);
        handleOrientation(privacyNoticeView);

        WebView privacyNoticeWebView = privacyNoticeView.findViewById(R.id.privacy_web_view);
        privacyNoticeWebView.setWebViewClient(new WebViewClient());
        privacyNoticeWebView.loadUrl(privacyNoticeUrl);
        return privacyNoticeView;
    }
}
