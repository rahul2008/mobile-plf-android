/**
 * ProductReview INAPP WebPage
 *
 * @author : Naveen AH
 * @since : 30 November 2015
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.rateandreview.fragments;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandler;
import com.philips.cdp.digitalcare.util.DigiCareLogger;


public class ProductReviewFragment extends DigitalCareBaseFragment {

    private static final String PRODUCT_REVIEW_URL = "https://%s%s/%s";
    private View mView = null;
    private WebView mWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String TAG = ProductReviewFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_REVIEW_WRITING,
                getPreviousName());

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
            mWebView.loadUrl(getProductPageUri().toString());
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.getSettings().setBuiltInZoomControls(true);
            mWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mProgressBar.setVisibility(View.GONE);
                }

            });
        }
    }

    private void initView() {
        mWebView = (WebView) mView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mView
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
        DigiCareLogger.v(TAG, "Country FallBack Url : " + countryFallBack);
        DigiCareLogger.v(TAG, "Country Specific Review&Rewards Url : " + getActivity().getResources().getString(R.string.reviewandrewards));

        return Uri.parse(String.format(PRODUCT_REVIEW_URL, countryFallBack,
                productPageLink, getLocalizedReviewUrl(countryFallBack)));
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.feedback);
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

        if (mWebView != null) {
            mWebView = null;
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
