package com.philips.cdp.digitalcare.locatephilips.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;

/**
 * Created by 310166779 on 12/14/2016.
 */

public class ServiceLocatorFragment extends DigitalCareBaseFragment {

    private View mServiceLocatorView = null;
    private WebView mServiceLocatorWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String TAG = ServiceLocatorFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DigiCareLogger.i(TAG, "Launching the Product Review Screen");
        if (mServiceLocatorView == null) {
            mServiceLocatorView = inflater.inflate(R.layout.consumercare_common_webview, container, false);
        }
        return mServiceLocatorView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        initView();
        loadServiceLocatorPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    private void loadServiceLocatorPage() {
        mServiceLocatorWebView.getSettings().setDomStorageEnabled(true);
        mServiceLocatorWebView.getSettings().setBuiltInZoomControls(true);
        Utils.loadWebPageContent(getServiceLocatorUrl(), mServiceLocatorWebView, mProgressBar);
    }

    private void initView() {
        mServiceLocatorWebView = (WebView) mServiceLocatorView.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) mServiceLocatorView.findViewById(R.id.common_webview_progress);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.find_philips_near_you);
        DigiCareLogger.i(TAG, "Philips Service Locator Page title : " + title);
        return title;
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_SERVICE_LOCATOR;
    }

    @Override
    public void onClick(View view) {

    }

    private String getServiceLocatorUrl() {
        String serviceLocatorLink = null;
        serviceLocatorLink = getResources().getString(R.string.service_locator_url);
        DigiCareLogger.i(TAG, "Service locator Url Link : " + serviceLocatorLink);
        return serviceLocatorLink;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mServiceLocatorWebView != null) {
            mServiceLocatorWebView = null;
        }
    }
}
