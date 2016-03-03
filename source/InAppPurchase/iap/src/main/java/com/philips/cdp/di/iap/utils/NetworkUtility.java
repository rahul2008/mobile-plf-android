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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.Fragments.ModalAlertDemoFragment;

/**
 * @author Vinayak Udikeri
 */
public class NetworkUtility {

    private static NetworkUtility mNetworkUtility;
    private static ModalAlertDemoFragment mModalAlertDemoFragment;

    private boolean isOnline;

    public static NetworkUtility getInstance() {
        synchronized (NetworkUtility.class) {
            if (mNetworkUtility == null) {
                mNetworkUtility = new NetworkUtility();
                mModalAlertDemoFragment = new ModalAlertDemoFragment();
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

    public void checkIsOnline(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        setOnline(true);
                        return;
                    }
                }
            }
        }
        setOnline(false);
    }

    public void showErrorDialog(FragmentManager pFragmentManager, String pButtonText, String pErrorString, String pErrorDescription){
        Bundle bundle = new Bundle();
        bundle.putString(IAPConstant.MODEL_ALERT_BUTTON_TEXT,pButtonText);
        bundle.putString(IAPConstant.MODEL_ALERT_ERROR_TEXT, pErrorString);
        bundle.putString(IAPConstant.MODEL_ALERT_ERROR_DESCRIPTION, pErrorDescription);
        //In case Fragment is active already here exception is thrown
        try {
            mModalAlertDemoFragment.setArguments(bundle);
            mModalAlertDemoFragment.show(pFragmentManager, "dialog");
        }catch (Exception e){

        }

    }
}
