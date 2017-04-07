package com.philips.platform.pushnotification;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by philips on 08/03/17.
 */

public class PlatformInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "PushNotification";

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Log.d(TAG,"On token referesh");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
