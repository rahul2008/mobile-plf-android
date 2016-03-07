/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.session.NetworkConstants;

public class WebPaymentFragment extends BaseAnimationSupportFragment {
    public final static String FRAGMENT_TAG = "WebPaymentFragment";

    private static final String SUCCESS_KEY = "successURL";
    private static final String PENDING_KEY = "pendingURL";
    private static final String FAILURE_KEY = "failureURL";
    private static final String CANCEL_KEY = "cancelURL";

    private static final String PAYMENT_SUCCESS_CALLBACK_URL = "http://www.philips.com/paymentSuccess";
    private static final String PAYMENT_PENDING_CALLBACK_URL = "http://www.philips.com/paymentPending";
    private static final String PAYMENT_FAILURE_CALLBACK_URL = "http://www.philips.com/paymentFailure";
    private static final String PAYMENT_CANCEL_CALLBACK_URL = "http://www.philips.com/paymentCancel";

    private WebView mPaymentWebView;
    private String mUrl;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mPaymentWebView = new WebView(getContext());
        mPaymentWebView.setWebViewClient(new PaymentWebViewClient());
        mPaymentWebView.getSettings().setJavaScriptEnabled(true);
        mUrl = getPaymentURL();
        return mPaymentWebView;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPaymentWebView.loadUrl(mUrl);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPaymentWebView.onResume();
        setTitle(R.string.iap_payment);
        setBackButtonVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPaymentWebView.onPause();
    }

    public static WebPaymentFragment createInstance(Bundle args, AnimationType animType) {
        WebPaymentFragment fragment = new WebPaymentFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    private String getPaymentURL() {
        Bundle arguments = getArguments();
        if (arguments == null || !arguments.containsKey(ModelConstants.WEBPAY_URL)) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(arguments.getString(ModelConstants.WEBPAY_URL));
        builder.append("&" + SUCCESS_KEY + "=" + PAYMENT_SUCCESS_CALLBACK_URL);
        builder.append("&" + PENDING_KEY + "=" + PAYMENT_PENDING_CALLBACK_URL);
        builder.append("&" + FAILURE_KEY + "=" + PAYMENT_FAILURE_CALLBACK_URL);
        builder.append("&" + CANCEL_KEY + "=" + PAYMENT_CANCEL_CALLBACK_URL);
        return builder.toString();
    }

    private Bundle createSuccessBundle(String url) {
        String orderNum = "";
        if(!TextUtils.isEmpty(url)) {
            // TODO: 04-03-2016 Need to fix with proper logic
            String[] temp = url.split("%5E");
            if(temp != null && temp.length >2) {
                temp = temp[2].split("-");
                if(temp != null && temp.length >0) {
                    orderNum = temp[0];
                }
            }
        }
        Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.ORDER_NUMBER, orderNum);
        return bundle;
    }

    private void launchConfirmationScreen(Bundle bundle) {
        replaceFragment(PaymentConfirmationFragment.createInstance(bundle, AnimationType.NONE), null);
    }

    private void goBackToOrderSummary() {
        getFragmentManager().popBackStack();
    }

    private class PaymentWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {

            return verifyResultCallBacks(url);
        }

        private boolean verifyResultCallBacks(String url) {
            boolean match = true;
            if (url.startsWith(PAYMENT_SUCCESS_CALLBACK_URL)) {
                launchConfirmationScreen(createSuccessBundle(url));
            } else if (url.startsWith(PAYMENT_PENDING_CALLBACK_URL)) {
                goBackToOrderSummary();
            } else if (url.startsWith(PAYMENT_FAILURE_CALLBACK_URL)) {
                goBackToOrderSummary();
            } else if (url.startsWith(PAYMENT_CANCEL_CALLBACK_URL)) {
                goBackToOrderSummary();
            } else {
                match = false;
            }
            return match;
        }
    }
}