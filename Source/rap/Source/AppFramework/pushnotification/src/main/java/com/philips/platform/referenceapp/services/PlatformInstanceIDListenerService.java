package com.philips.platform.referenceapp.services;

import android.content.Intent;

import com.philips.platform.referenceapp.utils.PNLog;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.philips.platform.referenceapp.PushNotificationManager.TAG;

public class PlatformInstanceIDListenerService extends FirebaseInstanceIdService {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        PNLog.d(TAG,"On token refresh");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
