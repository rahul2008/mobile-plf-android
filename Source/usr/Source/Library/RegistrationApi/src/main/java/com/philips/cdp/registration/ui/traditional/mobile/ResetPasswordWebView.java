package com.philips.cdp.registration.ui.traditional.mobile;

import android.content.Context;
import android.content.res.Configuration;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.registration.ProgressAlertDialog;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.traditional.RegistrationBaseFragment;
import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Created by 310190722 on 6/21/2016.
 */
public class ResetPasswordWebView extends RegistrationBaseFragment {

    public static final String TEST_RESET_PASS = "https://tst.philips.com.cn/c-w/user-registration/apps/login.html";
    public static final String STAGE_RESET_PASS = "https://acc.philips.com.cn/c-w/user-registration/apps/login.html";
    public static final String PROD_RESET_PASS = "https://www.philips.com.cn/c-w/user-registration/apps/login.html";
    private static final String TAG = "ResetPasswordWebView";
    private Context mContext;

    @Override
    protected void setViewParams(Configuration config, int width) {
      //NOP
    }

    @Override
    protected void handleOrientation(View view) {
        //NOP
    }

    @Override
    public int getTitleResourceId() {
        return R.string.USR_DLS_SigIn_TitleTxt;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_mobile_reset_password_webview, null);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        final String redirectUriKey = "redirectUriValue";
        WebView mWebView = (WebView) view.findViewById(R.id.reg_wv_reset_password_webview);
        showWebViewSpinner();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        String url = getURL(redirectUriKey);
        RLog.d("MobileVerifyCodeFragment ", "response val 2 token url " + url);

        mWebView.loadUrl(url);
        mWebView.clearHistory();
        mWebView.measure(100, 100);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                String url = "https://stg.philips.com.cn/c-w/user-registration/apps/login.html";
                if (url.contains("login.html")) {
                    //getRegistrationFragment().replaceWithHomeFragment();
                    getRegistrationFragment().onBackPressed();
                }

                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
                error.getCertificate();
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                hideWebViewSpinner();
            }

        });

    }

    private String getURL(String redirectUriKey) {
        Bundle bundle = getArguments();
        RLog.d(TAG, "getURL: bundle size " + bundle.size());
        String redirectUri = bundle.getString(redirectUriKey);

        String url;
        if (redirectUri != null && redirectUri.length() > 0) {
            //Reset Password SMS URL
            url = redirectUri;
        } else {
            //Reset Password EMAIL URL
            url = initializeResetPasswordLinks(RegistrationConfiguration.getInstance().getRegistrationEnvironment());
        }
        return url;
    }

    private void showWebViewSpinner() {
        if (!(getActivity().isFinishing())) showProgressDialog();
    }

    private void hideWebViewSpinner() {
        hideProgressDialog();
    }

    private String initializeResetPasswordLinks(String registrationEnv) {

        if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.PRODUCTION.getValue())) {
            return PROD_RESET_PASS;
        }
        if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.STAGING.getValue())) {
            return STAGE_RESET_PASS;
        }
        if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.TESTING.getValue())) {
            return TEST_RESET_PASS;
        }
        return null;
    }


    public ProgressAlertDialog mProgressDialog;

    public void showProgressDialog() {
        if (this.isVisible()) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressAlertDialog(getContext(), R.style.reg_Custom_loaderTheme);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public void notificationInlineMsg(String msg) {

    }
}
