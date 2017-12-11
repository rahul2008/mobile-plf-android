/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.R;
import com.philips.platform.referenceapp.utils.PNLog;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "PushNotification";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            PushNotificationManager.getInstance().saveToken(token,getApplicationContext());
            PushNotificationManager.getInstance().startPushNotificationRegistration(getApplicationContext());
            PNLog.i(TAG, "GCM Registration Token: " + token);

        } catch (IOException e) {
            PNLog.d(TAG, "Failed to complete token refresh"+e.getMessage());
        }
    }



}