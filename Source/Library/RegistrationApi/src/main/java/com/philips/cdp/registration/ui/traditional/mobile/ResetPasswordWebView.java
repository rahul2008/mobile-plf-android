package com.philips.cdp.registration.ui.traditional.mobile;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Created by 310190722 on 6/21/2016.
 */
public class ResetPasswordWebView extends Fragment {

    private WebView mWebView;
    private ProgressDialog mProgressDialog;

    public static final String TEST_RESET_PASSWORD = "https://tst.philips.com.cn/c-w/user-registration/apps/login.html";
    public static final String STAGE_RESET_PASSWORD = "https://acc.philips.com.cn/c-w/user-registration/apps/login.html";
    public static final String PROD_RESET_PASSWORD = "https://www.philips.com.cn/c-w/user-registration/apps/login.html";
    private String redirectUri;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reg_mobile_reset_password_webview, null);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        final String redirectUriKey = "redirectUri";
        mWebView = (WebView) view.findViewById(R.id.reg_wv_reset_password_webview);

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
        }
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        showWebViewSpinner();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        String url = getURL(redirectUriKey);
        RLog.i("MobileVerifyCodeFragment ", "response val 2 token url "+url);

        mWebView.loadUrl(url);
        //mWebView.loadUrl("https://acc.philips.co.in/myphilips/reset-password.html?cl=mob&loc=en_IN&code=q5sybj87nbsr4d");
        //mWebView.loadUrl("https://www.philips.co.in/myphilips/reset-password.html?cl=mob&loc=en_IN&code=q5sybj87nbsr4d");
        mWebView.clearView();
        mWebView.measure(100, 100);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
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

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
            }
        });

    }

    private String getURL(String redirectUriKey) {
        Bundle bundle = getArguments();
        RLog.i("MobileVerifyCodeFragment" , "bundle size "+bundle.size());
        redirectUri = bundle.getString(redirectUriKey);

        String url;
        if(redirectUri!=null && redirectUri.length()>0){
            //Reset Password SMS URL
            url = redirectUri;
        }else{
            //Reset Password EMAIL URL
            url = initializeResetPasswordLinks(RegistrationConfiguration.getInstance().getRegistrationEnvironment());
        }
        return url;
    }

    private void showWebViewSpinner() {
        if (!(getActivity().isFinishing()) && (mProgressDialog != null)) mProgressDialog.show();
    }

    private void hideWebViewSpinner() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
    private String initializeResetPasswordLinks(String registrationEnv) {

        if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.PRODUCTION.getValue())) {
            return PROD_RESET_PASSWORD;
        }
        if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.STAGING.getValue())) {
            return STAGE_RESET_PASSWORD;
        }
        if (registrationEnv.equalsIgnoreCase(com.philips.cdp.registration.configuration.Configuration.TESTING.getValue())) {
            return TEST_RESET_PASSWORD;
        }
        return null;
    }


}
