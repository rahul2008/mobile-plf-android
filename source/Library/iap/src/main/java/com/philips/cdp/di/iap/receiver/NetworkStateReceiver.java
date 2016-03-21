/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without 
the written consent of the copyright holder.

Project           : SaecoAvanti

File Name         : NetworkStateReceiver.java

Description       : Network State change listener.
Revision History: version 1: 
    Date: Aug 8, 2014
    Original author: Vinayak Udikeri
    Description: Initial version    
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;

public class NetworkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected && !NetworkUtility.getInstance().isOnline()) {
            NetworkUtility.getInstance().setOnline(true);
            EventHelper.getInstance().notifyEventOccurred(NetworkConstants.IS_ONLINE);
        } else if (!isConnected && NetworkUtility.getInstance().isOnline()) {
            NetworkUtility.getInstance().setOnline(false);
            EventHelper.getInstance().notifyEventOccurred(NetworkConstants.IS_ONLINE);
        }
    }
}
