/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.screens;

import android.content.Context;
import android.os.Bundle;
import android.webkit.CookieManager;

import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.session.NetworkConstants;
import com.ecs.demouapp.ui.utils.AlertListener;
import com.ecs.demouapp.ui.utils.ECSLog;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.ecs.demouapp.ui.utils.NetworkUtility;
import com.ecs.demouapp.ui.utils.Utility;


public class WebPaymentFragment extends WebFragment implements AlertListener {

    public static final String TAG = WebPaymentFragment.class.getName();
    private Context mContext;
    private boolean mIsPaymentFailed;

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
        ECSAnalytics.trackPage(ECSAnalyticsConstant.WORLD_PAY_PAGE_NAME);
        setTitleAndBackButtonVisibility(R.string.iap_payment, true);
        Utility.isDelvieryFirstTimeUser=false;
        setCartIconVisibility(false);
    }

    @Override
    protected boolean isJavaScriptEnable() {
        return false;
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
            ECSLog.d(TAG,"URL must be provided");
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

    private Bundle createErrorBundle(Bundle bundle) {
        bundle.putBoolean(ModelConstants.PAYMENT_SUCCESS_STATUS, false);
        return bundle;
    }

    private void launchConfirmationScreen(Bundle bundle) {
        addFragment(PaymentConfirmationFragment.createInstance(bundle, AnimationType.NONE), null,true);
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
            Bundle bundle = new Bundle();
            launchConfirmationScreen(createErrorBundle(bundle));
        } else if (url.startsWith(PAYMENT_FAILURE_CALLBACK_URL)) {
            mIsPaymentFailed = true;
            ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                    ECSAnalyticsConstant.SPECIAL_EVENTS, ECSAnalyticsConstant.PAYMENT_FAILURE);
            NetworkUtility.getInstance().showDialogMessage(mContext.getString(R.string.iap_payment_failed_title),getString(R.string.iap_payment_failed_message),getFragmentManager(), mContext,this);
        } else if (url.startsWith(PAYMENT_CANCEL_CALLBACK_URL)) {
            ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                    ECSAnalyticsConstant.PAYMENT_STATUS, ECSAnalyticsConstant.CANCELLED);
            Bundle bundle = new Bundle();
            bundle.putBoolean(ModelConstants.PAYMENT_CANCELLED,true);
            launchConfirmationScreen(createErrorBundle(bundle));
        } else {
            match = false;
        }
        if(match){
            clearCookies();;
        }
        return match;
    }

    @Override
    public boolean handleBackEvent() {
        mIsPaymentFailed = false;
        Utility.showActionDialog(mContext, mContext.getString(R.string.iap_ok), mContext.getString(R.string.iap_cancel), mContext.getString(R.string.iap_cancel_order_title), mContext.getString(R.string.iap_cancel_payment), getFragmentManager(), this);
        return true;
    }

    private void handleNavigation() {
        if(shouldGiveCallBack()){
            sendCallback(false);
            return;
        }
        showProductCatalogFragment(WebPaymentFragment.TAG);
    }

    @Override
    public void onPositiveBtnClick() {
        ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                ECSAnalyticsConstant.SPECIAL_EVENTS, ECSAnalyticsConstant.CANCEL_PAYMENT);
        clearCookies();
        handleNavigation();
    }

    @Override
    public void onNegativeBtnClick() {
        if (mIsPaymentFailed) {
            handleNavigation();
        }
    }

    private void clearCookies(){
        CookieManager.getInstance().removeSessionCookies(null);
    }
}