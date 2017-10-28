/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.intake.THSNoticeOfPrivacyPracticesFragment;
import com.philips.platform.ths.intake.THSNoticeOfPrivacyPracticesPresenter;
import com.philips.platform.uid.view.widget.Label;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class THSTermsAndConditionsFragment extends THSNoticeOfPrivacyPracticesFragment{

    public static final String TAG = THSTermsAndConditionsFragment.class.getSimpleName();

    public Label legalTextsLabel;
    THSNoticeOfPrivacyPracticesPresenter mThsTermsAndConditionsPresenter;
    private WebView mWebview;
    private RelativeLayout mRelativeLayoutNopContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_terms_and_conditions, container, false);
        mWebview = (WebView) view.findViewById(R.id.ths_webview);
        mRelativeLayoutNopContainer = (RelativeLayout) view.findViewById(R.id.nop_container);
        mThsTermsAndConditionsPresenter = new THSNoticeOfPrivacyPracticesPresenter(this);
        mThsTermsAndConditionsPresenter.getTermsAndConditions();
        return view;
    }

    public void showTermsAndConditions(String url) {
        mWebview.setVisibility(View.VISIBLE);
        if (url.endsWith(".pdf")) {
            try {
                /*String urlEncoded = URLEncoder.encode(url, "UTF-8");
                url = "http://docs.google.com/viewer?url=" + urlEncoded;*/
                mWebview.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
