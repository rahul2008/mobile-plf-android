/**
 * ProductReview INAPP WebPage
 *
 * @author : Naveen AH
 * @since : 30 November 2015
 * <p>
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.net.Uri;
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
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
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

        /*AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_REVIEW_WRITING,
                getPreviousName());*/
        DigitalCareConfigManager.getInstance().getTaggingInterface().
                trackPageWithInfo(AnalyticsConstants.PAGE_REVIEW_WRITING,
                        getPreviousName(), getPreviousName());

        initView();

        loadProductpage();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    private void loadProductpage() {
        if (getProductPageUri() == null) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            DigiCareLogger.d(TAG, getProductPageUri().toString());
            mProductReviewWebView.getSettings().setDomStorageEnabled(true);
            mProductReviewWebView.getSettings().setBuiltInZoomControls(true);
            Utils.loadWebPageContent(getProductPageUri().toString(), mProductReviewWebView, mProgressBar);
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
    protected Uri getProductPageUri() {
        String language = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getLanguage().toLowerCase();

        String country = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack()
                .getCountry().toUpperCase();
        String countryFallBack = LocaleMatchHandler.getPRXUrl(language + "_" + country);
        String productPageLink = DigitalCareConfigManager.getInstance().getViewProductDetailsData().getProductInfoLink();

        Uri uri = Uri.parse(String.format(PRODUCT_REVIEW_URL, countryFallBack,
                productPageLink, getLocalizedReviewUrl(countryFallBack)));
        DigiCareLogger.i(TAG, "Product Review page link : " + uri);
        return uri;
    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.feedback);
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


    protected String getLocalizedReviewUrl(String countryUrl) {

        String reviewAwards = null;

        switch (countryUrl) {
            case "www.philips.co.in":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.usa":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.fr":
                reviewAwards = "recompenses";
                break;
            case "www.philips.de":
                reviewAwards = "testberichte";
                break;
            case "www.philips.co.kr":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.nl":
                reviewAwards = "reviewenawards";
                break;
            case "www.philips.com.br":
                reviewAwards = "premios-e-reviews";
                break;
            case "www.philips.ru":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.com.tw":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.it":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.com.cn":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.pl":
                reviewAwards = "recenzje-i-nagrody";
                break;
            case "www.philips.es":
                reviewAwards = "valoracionesyresenas";
                break;
            case "www.philips.com.hk":
                reviewAwards = "reviewandawards";
                break;
            case "www.philips.dk":
                reviewAwards = "priser-og-anmeldelser";
                break;
            case "www.philips.fi":
                reviewAwards = "palkinnot-ja-arvostelut";
                break;
            case "www.philips.no":
                reviewAwards = "priser-og-anmelselser";
                break;
            case "www.philips.se":
                reviewAwards = "recensioner-och-utmarkelser";
                break;
            case "www.philips.co.uk":
                reviewAwards = "reviewandawards";
                break;
            default:
                reviewAwards = "reviewandawards";
                break;
        }
        return reviewAwards;
    }

}
