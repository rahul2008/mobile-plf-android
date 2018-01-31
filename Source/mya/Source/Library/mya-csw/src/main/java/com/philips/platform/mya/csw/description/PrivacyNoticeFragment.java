package com.philips.platform.mya.csw.description;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.mya.csw.CswBaseFragment;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.R;

import java.net.URL;

public class PrivacyNoticeFragment extends CswBaseFragment {
    private static final String PRIVACY_URL_DISCOVERY_KEY = "app.privacynotice";
    private AppInfraInterface appInfra;
    private ProgressDialog mProgressDialog;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View privacyNoticeView = inflater.inflate(R.layout.csw_privacy_notice_view, container, false);
        handleOrientation(privacyNoticeView);

        WebView privacyNoticeWebView = privacyNoticeView.findViewById(R.id.privacy_web_view);
        privacyNoticeWebView.setWebViewClient(new WebViewClient());
        showProgressDialog();
        retrieveAndSetPrivacyNoticeUrl(privacyNoticeWebView);
        return privacyNoticeView;
    }

    public void setAppInfra(AppInfraInterface appInfra) {
        this.appInfra = CswInterface.get().getDependencies().getAppInfra();
    }

    private void retrieveAndSetPrivacyNoticeUrl(final WebView privacyNoticeWebView) {
        AppInfraInterface appInfra = CswInterface.get().getDependencies().getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference(PRIVACY_URL_DISCOVERY_KEY,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        privacyNoticeWebView.loadUrl(url.toString());
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        hideProgressDialog();
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        hideProgressDialog();
                    }
                });
    }

    private void showProgressDialog() {
        if (!(getActivity().isFinishing())) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
                mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
