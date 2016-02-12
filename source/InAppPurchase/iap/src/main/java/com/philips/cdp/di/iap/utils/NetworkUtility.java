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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author Vinayak Udikeri
 */
public class NetworkUtility {

    private static NetworkUtility mNetworkUtility;

    private boolean isOnline;

    /**
     * Network Utility
     */
    private NetworkUtility() {
    }

    /**
     * @return NetworkUtility sigle ton object.
     */
    public static NetworkUtility getInstance() {
//		if (mNetworkUtility == null) {
        synchronized (NetworkUtility.class) {
            if (mNetworkUtility == null) {
                mNetworkUtility = new NetworkUtility();
            }
        }
//		}
        return mNetworkUtility;
    }

    /**
     * @return the isOnline
     */
    public boolean isOnline() {
        return isOnline;
    }

    /**
     * @param isOnline the isOnline to set
     */
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * Called on start of app.
     *
     * @param context {@link Context}
     */
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
}
