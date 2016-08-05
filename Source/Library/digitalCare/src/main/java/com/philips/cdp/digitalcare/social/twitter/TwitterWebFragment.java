package com.philips.cdp.digitalcare.social.twitter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.InflateException;
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
import com.philips.cdp.digitalcare.util.Utils;

public class TwitterWebFragment extends DigitalCareBaseFragment {

    private final String TAG = TwitterWebFragment.class.getSimpleName();
    private View mTwitterScreenView = null;
    private WebView mTwitterWebView = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    // private ProgressDialog mProgressDialog = null;
    private ProgressBar mProgressBar = null;
    private String TWITTTERURL = "https://twitter.com/intent/tweet?source=webclient&text=";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            mTwitterScreenView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        } catch (InflateException e) {
        }
        return mTwitterScreenView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        initView();
        loadInAppTwitter();
        /*AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_TWITTER,
                getPreviousName());*/
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                (AnalyticsConstants.PAGE_CONTACTUS_TWITTER,
                        getPreviousName(), getPreviousName());
    }

    private void loadInAppTwitter() {
        Utils.loadWebPageContent(getTwitterUrl(), mTwitterWebView, mProgressBar);
    }

    private void initView() {
        mTwitterWebView = (WebView) mTwitterScreenView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mTwitterScreenView
                .findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getTwitterUrl() {
        return TWITTTERURL
                + getProductInformation();
    }

    protected String getProductInformation() {

        String productname = DigitalCareConfigManager.getInstance()
                .getViewProductDetailsData().getProductName();
        productname = (productname == null) ? "" : productname;

        String ctnName = DigitalCareConfigManager.getInstance()
                .getViewProductDetailsData().getCtnName();
        ctnName = (ctnName == null) ? "" : ctnName;


        return "@" + getActivity().getString(R.string.twitter_page) + " " + getActivity().getResources().getString(
                R.string.support_productinformation)
                + " "
                + productname
                + " "
                + ctnName;
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
        return AnalyticsConstants.PAGE_CONTACTUS_TWITTER;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mTwitterWebView != null) {
            mTwitterWebView = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
        initView();
        loadInAppTwitter();
    }


    @Override
    public void onPause() {
        mTwitterWebView.loadUrl("about:blank");
        clearWebViewData();
        super.onPause();
    }

    private void clearWebViewData() {
        mTwitterWebView.stopLoading();
        mTwitterWebView.clearCache(true);
        mTwitterWebView.clearHistory();
        mTwitterWebView.clearFormData();
    }

}