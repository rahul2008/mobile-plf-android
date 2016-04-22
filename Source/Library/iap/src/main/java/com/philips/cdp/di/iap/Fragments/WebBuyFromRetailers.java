package com.philips.cdp.di.iap.Fragments;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.tagging.Tagging;
import com.philips.cdp.uikit.customviews.CircularLineProgressBar;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class WebBuyFromRetailers extends BaseAnimationSupportFragment {
    private WebView mBuyFromRetailersWebView;
    private String mUrl;
    private CircularLineProgressBar mProgress;
    private boolean mShowProgressBar = true;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup group = (ViewGroup) inflater.inflate(R.layout.iap_web_payment, container, false);

        mBuyFromRetailersWebView = (WebView) group.findViewById(R.id.wv_payment);
        mProgress = (CircularLineProgressBar) group.findViewById(R.id.cl_progress);
        mProgress.startAnimation(100);
        mBuyFromRetailersWebView.setWebViewClient(new BuyFromRetailersWebViewClient());
        mBuyFromRetailersWebView.getSettings().setJavaScriptEnabled(true);
        Bundle bundle = getArguments();
        mUrl = bundle.getString(IAPConstant.IAP_BUY_URL);
        mBuyFromRetailersWebView.loadUrl(mUrl);
        return group;
    }

    public static WebBuyFromRetailers createInstance(Bundle args, AnimationType animType) {
        WebBuyFromRetailers fragment = new WebBuyFromRetailers();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }


    private class BuyFromRetailersWebViewClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // Handle the error
            if (isVisible()) {
                NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
            }
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            if (isVisible()) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            super.onPageFinished(view, url);
            if (mProgress != null && mShowProgressBar) {
                mShowProgressBar = false;
                mProgress.setVisibility(View.GONE);
            }
        }
    }
}
