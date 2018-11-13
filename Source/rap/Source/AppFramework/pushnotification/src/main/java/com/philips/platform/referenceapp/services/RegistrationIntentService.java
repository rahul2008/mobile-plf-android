/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.utils.PNLog;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "PushNotification";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

            // Get updated InstanceID token.
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String token = instanceIdResult.getToken();
            // Fetch updated token and notify our app's server of any changes (if applicable).
            PushNotificationManager.getInstance().saveToken(token,new SecureStorageInterface.SecureStorageError());
            PushNotificationManager.getInstance().startPushNotificationRegistration(getApplicationContext(),new SecureStorageInterface.SecureStorageError());
            PNLog.i(TAG, "FireBase Registration Token: " + token);
        });

    }



}