package com.philips.cdp.digitalcare.locatephilips.fragments;

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
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;

/**
 * Created by 310166779 on 12/14/2016.
 */

public class ServiceLocatorFragment extends DigitalCareBaseFragment {

    private WebView mServiceLocatorWebView = null;
    private ProgressBar mProgressBar = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String TAG = ServiceLocatorFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.consumercare_webview_noscroll, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        loadServiceLocatorPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    private void loadServiceLocatorPage() {

        setWebSettingForWebview(getServiceLocatorUrl(), mServiceLocatorWebView, mProgressBar);
    }

    private void initView(View view) {
        mActionBarMenuIcon = (ImageView) view.findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) view.findViewById(R.id.back_to_home_img);
        mServiceLocatorWebView = (WebView) view.findViewById(R.id.webView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.common_webview_progress);
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

    protected String getServiceLocatorUrl() {

        DigitalCareConfigManager.getInstance().getAPPInfraInstance().getServiceDiscovery().getServiceUrlWithCountryPreference(DigitalCareConstants.SERVICE_ID_CC_ATOS, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                DigiCareLogger.v(TAG, "Response from Service Discovery : Service ID : 'cc.atos' - " + url);
                DigitalCareConfigManager.getInstance().setAtosUrl(url.toString());
            }

            @Override
            public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
                DigiCareLogger.v(TAG, "Error Response from Service Discovery :" + s);
                DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo(AnalyticsConstants.ACTION_SET_ERROR, AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR, s);
            }
        });
        return DigitalCareConfigManager.getInstance().getAtosUrl();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mServiceLocatorWebView != null) {
            mServiceLocatorWebView = null;
        }
    }
}
