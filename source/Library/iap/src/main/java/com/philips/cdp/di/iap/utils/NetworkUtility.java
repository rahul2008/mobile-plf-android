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

import com.philips.cdp.di.iap.Fragments.ErrorDialogFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import static com.philips.cdp.di.iap.R.string.iap_check_connection;

public class NetworkUtility {

    private static NetworkUtility mNetworkUtility;
    private static ErrorDialogFragment mModalAlertDemoFragment;
    private boolean isOnline;

    public static NetworkUtility getInstance() {
        synchronized (NetworkUtility.class) {
            if (mNetworkUtility == null) {
                mNetworkUtility = new NetworkUtility();
                mModalAlertDemoFragment = new ErrorDialogFragment();
            }
        }
        return mNetworkUtility;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void showErrorDialog(FragmentManager pFragmentManager, String pButtonText, String pErrorString, String pErrorDescription) {
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.MODEL_ALERT_BUTTON_TEXT, pButtonText);
        bundle.putString(IAPConstant.MODEL_ALERT_ERROR_TEXT, pErrorString);
        bundle.putString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION, pErrorDescription);
        //In case Fragment is active already here exception is thrown
        try {
            mModalAlertDemoFragment.setArguments(bundle);
            mModalAlertDemoFragment.show(pFragmentManager, "dialog");
        } catch (Exception e) {
            if (mModalAlertDemoFragment.isAdded())
                mModalAlertDemoFragment.dismiss();
        }

    }

    public void showErrorMessage(final Message msg,FragmentManager pFragmentManager, Context mContext){
        /*
         *  Dismiss The Dialog if it not yet dismissed as Error Occured
         */
        if(Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();

        /*
         * Check if The Error is of the type IAPNetworkError , else display something went wrong
         */
        if (msg.obj instanceof IAPNetworkError) {
            IAPNetworkError error = (IAPNetworkError) msg.obj;
            String errorMessage = "";

            if(error!=null) {
                errorMessage = error.getMessage();
            }

            //If error message is null or "" - We dont know the exact error, hence display something went wrong
            if(errorMessage.equalsIgnoreCase("") || null==errorMessage){
                NetworkUtility.getInstance().showErrorDialog(pFragmentManager, mContext.getString(R.string.iap_ok),
                        mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
                return;
            }

            //If error message is of the Type Network Error display Network Error
            if(errorMessage.equalsIgnoreCase(mContext.getString(iap_check_connection))){
                NetworkUtility.getInstance().showErrorDialog(pFragmentManager, mContext.getString(R.string.iap_ok),
                        mContext.getString(R.string.iap_network_error), mContext.getString(R.string.iap_check_connection));
                return;
            }

            //If error message is of the Type Timeout Error display Timeout Error
            if(errorMessage.equalsIgnoreCase(mContext.getString(R.string.iap_time_out_error))){
                NetworkUtility.getInstance().showErrorDialog(pFragmentManager, mContext.getString(R.string.iap_ok),
                        mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_time_out_error));
                return;
            }

            //Default Case
            NetworkUtility.getInstance().showErrorDialog(pFragmentManager, mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), error.getMessage());

        }else {
            NetworkUtility.getInstance().showErrorDialog(pFragmentManager, mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }
    }
}
