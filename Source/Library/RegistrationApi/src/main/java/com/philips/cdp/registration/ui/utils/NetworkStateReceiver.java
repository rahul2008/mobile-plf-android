/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.settings.RegistrationHelper;

import javax.inject.Inject;

public  class NetworkStateReceiver extends BroadcastReceiver {

    @Inject
    NetworkUtility networkUtility;

    public NetworkStateReceiver() {
        URInterface.getComponent().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOnline = networkUtility.isNetworkAvailable();
        if (null != RegistrationHelper.getInstance().getNetworkStateListener()) {
            RegistrationHelper.getInstance().getNetworkStateListener()
                    .notifyEventOccurred(isOnline);
        }
    }
}
