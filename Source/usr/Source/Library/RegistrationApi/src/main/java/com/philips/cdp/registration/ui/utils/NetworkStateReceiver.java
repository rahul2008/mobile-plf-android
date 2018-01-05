/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.content.*;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.settings.RegistrationHelper;

import javax.inject.Inject;

public  class NetworkStateReceiver extends BroadcastReceiver {

    @Inject
    NetworkUtility networkUtility;

    public NetworkStateReceiver() {
        RegistrationConfiguration.getInstance().getComponent().inject(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isOnline = networkUtility.isNetworkAvailable();
        ThreadUtils.postInMainThread(context, () -> {
            if (null != RegistrationHelper.getInstance().getNetworkStateListener()) {
                RegistrationHelper.getInstance().getNetworkStateListener()
                        .notifyEventOccurred(isOnline);
            }
        });
    }
}
