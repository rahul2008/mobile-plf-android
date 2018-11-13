/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.referenceapp.services;

import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.referenceapp.utils.PNLog;

/**
 * Created by philips on 08/03/17.
 */

public class PlatformFcmListenerService extends FirebaseMessagingService {
    private static final String TAG = "PushNotification";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        PNLog.d(TAG,"Push Notification received");
        PushNotificationManager.getInstance().sendPayloadToCoCo(remoteMessage.getData());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        PNLog.d(TAG,"On token refresh");
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
