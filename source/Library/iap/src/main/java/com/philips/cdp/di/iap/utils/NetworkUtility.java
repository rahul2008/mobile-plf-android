package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.philips.cdp.di.iap.Fragments.ErrorDialogFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.IAPNetworkError;

public class NetworkUtility {
    private static NetworkUtility mNetworkUtility;
    private ErrorDialogFragment mModalAlertDemoFragment;
    private boolean isOnline;

    public static NetworkUtility getInstance() {
        synchronized (NetworkUtility.class) {
            if (mNetworkUtility == null) {
                mNetworkUtility = new NetworkUtility();
            }
        }
        return mNetworkUtility;
    }

    public void dismissErrorDialog() {
        if (null != mModalAlertDemoFragment && mModalAlertDemoFragment.isAdded()) {
            mModalAlertDemoFragment.dismissAllowingStateLoss();
            mModalAlertDemoFragment = null;
        }
    }


    public void showErrorDialog(Context context, FragmentManager pFragmentManager,
                                String pButtonText, String pErrorString, String pErrorDescription) {

        //Track pop up
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.IN_APP_NOTIFICATION_POP_UP, pErrorDescription);
        if (!((Activity) context).isFinishing()) {
            if (mModalAlertDemoFragment == null) {
                mModalAlertDemoFragment = new ErrorDialogFragment();
                mModalAlertDemoFragment.setShowsDialog(false);
            }

            if (mModalAlertDemoFragment.getShowsDialog()) {
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString(IAPConstant.MODEL_ALERT_BUTTON_TEXT, pButtonText);
            bundle.putString(IAPConstant.MODEL_ALERT_ERROR_TEXT, pErrorString);
            bundle.putString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION, pErrorDescription);
            try {
                mModalAlertDemoFragment.setArguments(bundle);
                mModalAlertDemoFragment.show(pFragmentManager, "NetworkErrorDialog");
                mModalAlertDemoFragment.setShowsDialog(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showErrorMessage(final Message msg, FragmentManager pFragmentManager, Context context) {
        if (context == null) return;
        /*
         *  Dismiss The Dialog if it not yet dismissed as Error Occured
         */
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();

        /*
         * Check if The Error is of the type IAPNetworkError , else display something went wrong
         */
        if (msg.obj instanceof IAPNetworkError) {
            IAPNetworkError error = (IAPNetworkError) msg.obj;
            showErrorDialog(context, pFragmentManager, context.getString(R.string.iap_ok),
                    getErrorTitleMessageFromErrorCode(context, error.getIAPErrorCode()),
                    getErrorDescriptionMessageFromErrorCode(context, error));
        } else {
            showErrorDialog(context, pFragmentManager, context.getString(R.string.iap_ok),
                    context.getString(R.string.iap_server_error), context.getString(R.string.iap_something_went_wrong));
        }
    }

    public String getErrorTitleMessageFromErrorCode(final Context context, int errorCode) {
        String errorMessage = null;
        if (errorCode == IAPConstant.IAP_ERROR_NO_CONNECTION) {
            errorMessage = context.getString(R.string.iap_you_are_offline);
        } else {
            errorMessage = context.getString(R.string.iap_server_error);
        }
        return errorMessage;
    }

    public String getErrorDescriptionMessageFromErrorCode(final Context context,
                                                           IAPNetworkError error) {
        if (error.getIAPErrorCode() != IAPConstant.IAP_ERROR_NO_CONNECTION
                && !TextUtils.isEmpty(error.getMessage())) {
            return error.getMessage();
        }
        //Proceed with custom error message
        String errorMessage = null;
        int errorCode = error.getIAPErrorCode();
        if (errorCode == IAPConstant.IAP_ERROR_NO_CONNECTION) {
            errorMessage = context.getString(R.string.iap_no_internet);
        } else if (errorCode == IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT) {
            errorMessage = context.getString(R.string.iap_time_out_error);
        } else if (errorCode == IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE) {
            errorMessage = context.getString(R.string.iap_authentication_failure);
        } else {
            errorMessage = context.getString(R.string.iap_something_went_wrong);
        }
        return errorMessage;
    }

    public boolean isNetworkAvailable(Context pContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}