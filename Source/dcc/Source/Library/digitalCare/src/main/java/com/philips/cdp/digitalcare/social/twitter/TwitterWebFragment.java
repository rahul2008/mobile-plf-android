package com.philips.cdp.digitalcare.social.twitter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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

@SuppressWarnings("serial")
public class TwitterWebFragment extends DigitalCareBaseFragment {

    private WebView mTwitterWebView = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private ProgressBar mProgressBar = null;
    private String mTwitterPageName = null;
    private String TWITTTERURL = "https://twitter.com/intent/tweet?source=webclient&text=@";
    private static String TAG = TwitterWebFragment.class.getSimpleName();

    public TwitterWebFragment() {

    }

    public TwitterWebFragment(String twitterPageIndex) {
        this.mTwitterPageName = twitterPageIndex;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        loadInAppTwitter();
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                (AnalyticsConstants.PAGE_CONTACTUS_TWITTER,
                        getPreviousName(), getPreviousName());
    }

    private void loadInAppTwitter() {
        setWebSettingForWebview(getTwitterUrl(), mTwitterWebView, mProgressBar);
    }

    private void initView(View view) {
        mTwitterWebView = (WebView) view.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) view
                .findViewById(R.id.common_webview_progress);
        mActionBarMenuIcon = (ImageView) view.findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) view.findViewById(R.id.back_to_home_img);
        mProgressBar.setVisibility(View.GONE);
    }

    private String getTwitterUrl() {

        return TWITTTERURL + mTwitterPageName + getProductInformation();
    }

    protected String getProductInformation() {
        String productname = DigitalCareConfigManager.getInstance()
                .getViewProductDetailsData().getProductName();
        productname = (productname == null) ? "" : productname;

        String ctnName = DigitalCareConfigManager.getInstance()
                .getViewProductDetailsData().getCtnName();
        ctnName = (ctnName == null) ? "" : ctnName;


        return " " + getActivity().getResources().getString(
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
        initView(getView());
        loadInAppTwitter();
    }


    @Override
    public void onPause() {
        mTwitterWebView.loadUrl("about:blank");
        clearWebViewData(mTwitterWebView);
        super.onPause();
    }

    protected void clearWebViewData(WebView webView) {
        webView.stopLoading();
        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
    }

}