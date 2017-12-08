/**
 * ProductReview INAPP WebPage
 *
 * @author : Naveen AH
 * @since : 30 November 2015
 * <p>
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.fragments.rateandreview.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;


public class ProductReviewFragment extends DigitalCareBaseFragment {

    private static final String PRODUCT_REVIEW_URL = "https://%s%s/%s";
    private View mProductReviewView = null;
    private WebView mProductReviewWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String TAG = ProductReviewFragment.class.getSimpleName();
    private String productPageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DigiCareLogger.i(TAG, "Launching the Product Review Screen");
        if (mProductReviewView == null) {
            mProductReviewView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }
        return mProductReviewView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        DigitalCareConfigManager.getInstance().getTaggingInterface().
                trackPageWithInfo(AnalyticsConstants.PAGE_REVIEW_WRITING,
                        getPreviousName(), getPreviousName());
        initView();
        productPageUri = getProductPageUri();
        loadProductpage(productPageUri);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    public void loadProductpage(String productPageUri) {
        if (productPageUri == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            setWebSettingForWebview(getProductPageUri(), mProductReviewWebView, mProgressBar);
        }
    }

    private void initView() {
        mProductReviewWebView = (WebView) mProductReviewView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mProductReviewView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Product Review URL Page
     *
     * @return
     */
    protected String getProductPageUri() {
        return DigitalCareConfigManager.getInstance().getProductReviewUrl();
    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.dcc_write_review);
        DigiCareLogger.i(TAG, "Philips Product Review Page title : " + title);
        return title;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_REVIEW_WRITING;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProductReviewWebView != null) {
            mProductReviewWebView = null;
        }
    }

}
