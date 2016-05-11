/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : SaecoAvanti

File Name         : NetworkUtility.java

Description       : Network State Utility.
Revision History: version 1: 
    Date: Aug 18, 2014
    Original author: Vinayak Udikeri
    Description: Initial version    
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.philips.cdp.di.iap.Fragments.ErrorDialogFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.tagging.Tagging;

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

    public void showErrorDialog(FragmentManager pFragmentManager, String pButtonText, String pErrorString, String pErrorDescription) {

        //Track pop up
        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.IN_APP_NOTIFICATION_POP_UP, pErrorDescription);
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

            showErrorDialog(pFragmentManager, context.getString(R.string.iap_ok),
                    getErrorTitleMessageFromErrorCode(context, error.getIAPErrorCode()),
                    getErrorDescriptionMessageFromErrorCode(context, error));
        } else {
            NetworkUtility.getInstance().showErrorDialog(pFragmentManager, context.getString(R.string.iap_ok),
                    context.getString(R.string.iap_server_error), context.getString(R.string.iap_something_went_wrong));
        }
    }

    private String getErrorTitleMessageFromErrorCode(final Context context, int errorCode) {
        String errorMessage = null;
        if (errorCode == IAPConstant.IAP_ERROR_NO_CONNECTION) {
            errorMessage = context.getString(R.string.iap_network_error);
        } else {
            errorMessage = context.getString(R.string.iap_server_error);
        }
        return errorMessage;
    }

    private String getErrorDescriptionMessageFromErrorCode(final Context context,
                                                           IAPNetworkError error) {
        if (error.getIAPErrorCode() != IAPConstant.IAP_ERROR_NO_CONNECTION
                && !TextUtils.isEmpty(error.getMessage())) {
            return error.getMessage();
        }
        //Proceed with custom error message
        String errorMessage = null;
        int errorCode = error.getIAPErrorCode();
        if (errorCode == IAPConstant.IAP_ERROR_NO_CONNECTION) {
            errorMessage = context.getString(R.string.iap_check_connection);
        } else if (errorCode == IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT) {
            errorMessage = context.getString(R.string.iap_time_out_error);
        } else if (errorCode == IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE) {
            errorMessage = context.getString(R.string.iap_authentication_failure);
        } else {
            errorMessage = context.getString(R.string.iap_something_went_wrong);
        }
        return errorMessage;
    }
}