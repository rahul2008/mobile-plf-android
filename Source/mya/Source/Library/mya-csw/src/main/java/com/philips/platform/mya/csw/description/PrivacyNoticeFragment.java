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
        mProgressDialog = new ProgressDialog(getActivity());
        WebView privacyNoticeWebView = privacyNoticeView.findViewById(R.id.privacy_web_view);
        privacyNoticeWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                dismissProgressDialog();
            }
        });
        showProgressDialog();
        retrieveAndSetPrivacyNoticeUrl(privacyNoticeWebView);
        return privacyNoticeView;
    }

    private void retrieveAndSetPrivacyNoticeUrl(final WebView privacyNoticeWebView) {
        AppInfraInterface appInfra = CswInterface.get().getDependencies().getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
        serviceDiscoveryInterface.getServiceUrlWithLanguagePreference(PRIVACY_URL_DISCOVERY_KEY,
                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onSuccess(URL url) {
                        privacyNoticeWebView.loadUrl(url.toString());
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        dismissProgressDialog();
                    }
                });
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !getActivity().isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog != null && !mProgressDialog.isShowing() && !getActivity().isFinishing()) {
            mProgressDialog.setMessage(getResources().getString(R.string.reg_Loading_Text));
            mProgressDialog.show();
        }
    }
}
