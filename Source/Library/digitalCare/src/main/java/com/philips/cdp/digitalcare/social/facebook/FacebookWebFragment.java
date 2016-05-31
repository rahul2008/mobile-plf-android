/**
 * FaceBook Feature in the INAPP Webpage as a configurable data.
 *
 * @author naveen@philips.com
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.social.facebook;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.Utils;

public class FacebookWebFragment extends DigitalCareBaseFragment {

    private final String TAG = FacebookWebFragment.class.getSimpleName();
    private View mFacebookScreenView = null;
    private WebView mFacebookWebView = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    // private ProgressDialog mProgressDialog = null;
    private ProgressBar mProgressBar = null;
    private String FacebookURL = "http://www.facebook.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            mFacebookScreenView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        } catch (InflateException e) {
        }
        return mFacebookScreenView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        initView();
        loadInAppFacebook();
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_FACEBOOK,
                getPreviousName());
    }

    private void loadInAppFacebook() {
        Utils.loadWebPageContent(getFacebookUrl(), mFacebookWebView, mProgressBar);

    }

    private void initView() {
        mFacebookWebView = (WebView) mFacebookScreenView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mFacebookScreenView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getFacebookUrl() {
        return FacebookURL
                + getActivity().getString(R.string.facebook_product_page);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.contact_us);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACTUS_FACEBOOK;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFacebookWebView != null) {
            mFacebookWebView = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }
}