/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.philips.platform.referenceapp.utils.PNLog;

/**
 * Created by philips on 08/03/17.
 */

public class PlatformInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "PushNotification";

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        PNLog.d(TAG,"On token referesh");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
