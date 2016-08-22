/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

public class WebPaymentFragment extends WebFragment implements TwoButtonDialogFragment.TwoButtonDialogListener {

    public static final String TAG = WebPaymentFragment.class.getName();
    private TwoButtonDialogFragment mDialogFragment;

    private static final String SUCCESS_KEY = "successURL";
    private static final String PENDING_KEY = "pendingURL";
    private static final String FAILURE_KEY = "failureURL";
    private static final String CANCEL_KEY = "cancelURL";

    private static final String PAYMENT_SUCCESS_CALLBACK_URL = "http://www.philips.com/paymentSuccess";
    private static final String PAYMENT_PENDING_CALLBACK_URL = "http://www.philips.com/paymentPending";
    private static final String PAYMENT_FAILURE_CALLBACK_URL = "http://www.philips.com/paymentFailure";
    private static final String PAYMENT_CANCEL_CALLBACK_URL = "http://www.philips.com/paymentCancel";

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.WORLD_PAY_PAGE_NAME);
        setTitleAndBackButtonVisibility(R.string.iap_payment, false);
        //setBackButtonVisibility(false);
    }

    public static WebPaymentFragment createInstance(Bundle args, AnimationType animType) {
        WebPaymentFragment fragment = new WebPaymentFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getWebUrl() {
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

    private Bundle createSuccessBundle() {
        String orderNum = CartModelContainer.getInstance().getOrderNumber();
        Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.ORDER_NUMBER, orderNum);
        bundle.putBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, true);
        return bundle;
    }

    private Bundle createErrorBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, false);
        return bundle;
    }

    private void launchConfirmationScreen(Bundle bundle) {
        addFragment(PaymentConfirmationFragment.createInstance(bundle, AnimationType.NONE), null);
    }

    @Override
    protected boolean shouldOverrideUrlLoading(final String url) {
        return verifyResultCallBacks(url);
    }

    private boolean verifyResultCallBacks(String url) {
        boolean match = true;
        if (url.startsWith(PAYMENT_SUCCESS_CALLBACK_URL)) {
            launchConfirmationScreen(createSuccessBundle());
        } else if (url.startsWith(PAYMENT_PENDING_CALLBACK_URL)) {
            launchConfirmationScreen(createErrorBundle());
        } else if (url.startsWith(PAYMENT_FAILURE_CALLBACK_URL)) {
            launchConfirmationScreen(createErrorBundle());
        } else if (url.startsWith(PAYMENT_CANCEL_CALLBACK_URL)) {
            //Track Payment cancelled action
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.CANCELLED);
            moveToPreviousFragment();
        } else {
            match = false;
        }
        return match;
    }

    @Override
    public boolean handleBackEvent() {
        ShowDialogOnBackPressed();
        return true;
    }

    private void ShowDialogOnBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.MODEL_ALERT_CONFIRM_DESCRIPTION, getString(R.string.cancelPaymentMsg));
        if (mDialogFragment == null) {
            mDialogFragment = new TwoButtonDialogFragment();
            mDialogFragment.setArguments(bundle);
            mDialogFragment.setOnDialogClickListener(this);
            mDialogFragment.setShowsDialog(false);
        }
        try {
            mDialogFragment.show(getFragmentManager(), "TwoButtonDialog");
            mDialogFragment.setShowsDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogOkClick() {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.CANCELLED);
        launchProductCatalog();
    }

    @Override
    public void onDialogCancelClick() {
    }

}