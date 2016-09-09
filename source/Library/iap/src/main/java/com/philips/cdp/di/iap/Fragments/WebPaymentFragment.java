/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

public class WebPaymentFragment extends WebFragment implements
        TwoButtonDialogFragment.TwoButtonDialogListener {

    public static final String TAG = WebPaymentFragment.class.getName();
    private Context mContext;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.WORLD_PAY_PAGE_NAME);
        setTitleAndBackButtonVisibility(R.string.iap_payment, true);
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
        if (arguments == null || !arguments.containsKey(ModelConstants.WEB_PAY_URL)) {
            throw new RuntimeException("URL must be provided");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(arguments.getString(ModelConstants.WEB_PAY_URL));
        builder.append("&" + SUCCESS_KEY + "=" + PAYMENT_SUCCESS_CALLBACK_URL);
        builder.append("&" + PENDING_KEY + "=" + PAYMENT_PENDING_CALLBACK_URL);
        builder.append("&" + FAILURE_KEY + "=" + PAYMENT_FAILURE_CALLBACK_URL);
        builder.append("&" + CANCEL_KEY + "=" + PAYMENT_CANCEL_CALLBACK_URL);
        return builder.toString();
    }

    private Bundle createSuccessBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(ModelConstants.ORDER_NUMBER, CartModelContainer.getInstance().getOrderNumber());
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
            showTwoButtonDialog(mContext.getString(R.string.iap_payment_failed),
                    mContext.getString(R.string.iap_try_again),
                    mContext.getString(R.string.iap_cancel));
        } else if (url.startsWith(PAYMENT_CANCEL_CALLBACK_URL)) {
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
        showTwoButtonDialog(mContext.getString(R.string.cancelPaymentMsg),
                mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_cancel));
        return true;
    }

    private void showTwoButtonDialog(String description, String positiveText, String negativeText) {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.TWO_BUTTON_DIALOG_DESCRIPTION, description);
        bundle.putString(IAPConstant.TWO_BUTTON_DIALOG_POSITIVE_TEXT, positiveText);
        bundle.putString(IAPConstant.TWO_BUTTON_DIALOG_NEGATIVE_TEXT, negativeText);
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
    public void onPositiveButtonClicked() {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.PAYMENT_STATUS, IAPAnalyticsConstant.CANCELLED);
        Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
        if (fragment != null) {
            moveToVerticalAppByClearingStack();
        } else {
            showProductCatalogFragment();
        }
    }

    @Override
    public void onNegativeButtonClicked() {
    }
}